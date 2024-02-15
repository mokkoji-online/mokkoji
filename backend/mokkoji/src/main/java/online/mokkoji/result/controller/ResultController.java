package online.mokkoji.result.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.result.dto.request.CoverImageReqDto;
import online.mokkoji.result.dto.request.RecollectionReqDto;
//import online.mokkoji.result.service.PhotomosaicService;
import online.mokkoji.s3.S3Service;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.result.dto.request.RollingPaperReqDto;
import online.mokkoji.result.dto.response.ResultResDto;
import online.mokkoji.result.dto.response.RollingpaperEditResDto;
import online.mokkoji.result.service.ResultService;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/results")
public class ResultController {

    private final ResultService resultService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final S3Service s3Service;
//    private final PhotomosaicService photomosaicService;

    /**
     * 결과물 리스트 조회
     * @param req 유저 Access Token
     * @return 기억 리스트, 추억 리스트
     */
    @GetMapping("/lists")
    public ResponseEntity<Map<String, Object>> getResultList(HttpServletRequest req) {

        Map<String, Object> result = resultService.getResultList(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 선택 결과물의 사진 리스트, 롤링페이퍼 템플릿 조회
     * @param resultId 결과물 ID
     * @return 사진 리스트, 롤링페이퍼 템플릿
     */
    @GetMapping("/{resultId}/memories")
    public ResponseEntity<Map<String, Object>> getRollingpaperAndPhotoEdit(@PathVariable Long resultId) {

        List<String> photoPath = resultService.getPhotoPath(resultId);
        RollingpaperEditResDto rollingpaperTemplate = resultService.getRollingpaperTemplate(resultId);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("photoPath", photoPath);
        responseMap.put("rollingpaperTemplate", rollingpaperTemplate);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    /**
     * 롤링페이퍼 템플릿 업데이트
     * @param resultId 결과물 ID
     * @param rollingPaperReqDto 배경 템플릿, 포스트잇 템플릿
     * @return 완료 안내 텍스트
     */
    @PatchMapping("/{resultId}/memories/rollingpaper")
    public ResponseEntity<String> updateRollingpaper(@PathVariable Long resultId,
                                                     @RequestBody RollingPaperReqDto rollingPaperReqDto) {

        resultService.updateRollingpaper(resultId, rollingPaperReqDto);

        return new ResponseEntity<>("편집 완료", HttpStatus.OK);
    }


    /**
     * 사진 추가
     * @param resultId 결과물 ID
     * @param req 유저 Access Token
     * @param photoList 추가하는 사진 파일
     * @return 완료 안내 텍스트
     * @throws IOException
     */
    @PostMapping("/{resultId}/memories/photos")
    public ResponseEntity<String> addPhotos(@PathVariable("resultId") Long resultId,
                                           HttpServletRequest req,
                                           @RequestParam("photos") List<MultipartFile> photoList) throws IOException {

        User user = userService.searchUser(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        // 사진 업로드
        List<PhotoResDto> photoResDtoList = s3Service.uploadPhotoList(photoList, user.getId(), resultId);

        // db에 저장
        resultService.createPhotoList(photoResDtoList);

        // redis cache에 저장
        resultService.updatePhotoPathCache(resultId, photoResDtoList);

        return new ResponseEntity<>("사진 업로드 완료", HttpStatus.OK);
    }


    /**
     * 대표이미지 변경
     * @param resultId 결과물 ID
     * @param coverImageReqDTO 대표이미지 url
     * @return 완료 안내 텍스트
     */
    @PatchMapping("/{resultId}/memories")
    public ResponseEntity<String> updateThumbnail(@PathVariable("resultId") Long resultId, @RequestBody CoverImageReqDto coverImageReqDTO) {
        resultService.updateThumbnail(resultId, coverImageReqDTO.getUrl());
        return new ResponseEntity<>("대표이미지 설정 완료", HttpStatus.OK);
    }

    /**
     * 선택 결과물 정보 조회 TODO : 경배오빠한테 물어보기
     * @param resultId 결과물 ID
     * @param pageable 페이징 페이지 번호
     * @return 결과물-롤링페이퍼, 포토모자이크, 간단 정보
     */
    @GetMapping("/recollections/{resultId}")
    public ResponseEntity<ResultResDto> getResult(@PathVariable Long resultId, Pageable pageable) {

        ResultResDto resultResDto = resultService.getResult(resultId, pageable);

        return new ResponseEntity<>(resultResDto, HttpStatus.OK);
    }

    /**
     * 결과물 기억에서 추억으로 변경, 사진 삭제
     * @param resultId 결과물 ID
     * @param recollectionReqDto 추억 제목, 내용 
     * @param req 유저 Access Token
     * @return 결과물 리스트-기억 리스트, 추억 리스트
     */
    @PostMapping("/{resultId}")
    public ResponseEntity<Map<String, Object>> addRecollection(@PathVariable Long resultId,
                                                               @RequestBody RecollectionReqDto recollectionReqDto, HttpServletRequest req) {
        resultService.createRecollection(resultId, recollectionReqDto);

        // S3에서 대표이미지 제외 사진 삭제
        s3Service.deletePhotos(resultId);

        Map<String, Object> result = resultService.getResultList(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        //로컬 이미지 삭제
        String imagesDirectory = "/opt/result" + File.separator;

//        photomosaicService.deleteImages(imagesDirectory);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * 대표이미지 다운로드
     * @param resultId 결과물 ID
     * @return 201 코드
     */
    @GetMapping("/{resultId}/thumbnail")
    public ResponseEntity<Void> downloadThumbnail(@PathVariable Long resultId) {
        String thumbnailPath = resultService.getThumbnailPath(resultId);
        s3Service.downloadWithUrl(thumbnailPath, "thumbnail");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 포토모자이크 다운로드
     * @param resultId 결과물 ID
     * @return 201 코드
     */
    @GetMapping("{resultId}/photomosaic")
    public ResponseEntity<Void> downloadPhotomosaic(@PathVariable Long resultId) {
        String photomosaicPath = resultService.getPhotomosaicPath(resultId);

        s3Service.downloadWithUrl(photomosaicPath, "photomosaic");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 대표이미지 다운로드 URL 생성
     * @param resultId 결과물 ID
     * @return 대표이미지 다운로드 URL
     */
    @GetMapping("{resultId}/sharing/thumbnail")
    public ResponseEntity<String> shareThumbnail(@PathVariable Long resultId) {
        String downloadUrl = resultService.getThumbnailPath(resultId);

        return new ResponseEntity<>(downloadUrl, HttpStatus.CREATED);
    }

    /**
     * 포토모자이크 다운로드 URL 생성
     * @param resultId 결과물 ID
     * @return 포토모자이크 다운로드 URL
     */
    @GetMapping("{resultId}/sharing/photomosaic/")
    public ResponseEntity<String> sharePhotoMosaic(@PathVariable Long resultId) {
        String downloadUrl = resultService.getPhotomosaicPath(resultId);

        return new ResponseEntity<>(downloadUrl, HttpStatus.CREATED);
    }

//    /**
//     * 포토모자이크 URL 생성
//     * @param resultId 결과물 ID
//     * @return 포토모자이크 URL
//     */
//    @PutMapping("{resultId}/photomosaic")
//    public ResponseEntity<String> addPhotomosaic(@PathVariable("resultId") Long resultId) {
//
//        log.info("포토 모자이크 생성 시작");
//
//        //S3에 저장된 thumbnail, images 임시 다운로드(경로 확인 필요)
//        String thumbnailPath = resultService.getThumbnailPath(resultId);
//
//
//
//        s3Service.downloadThumbnail(resultId, thumbnailPath);
//        log.info("대표사진 다운로드 완료");
//        s3Service.downloadCellImages(resultId);
//        log.info("셀 이미지 다운로드 완료");
//
//        //photomosaic 생성, 임시 경로에 저장
//        String photomosaic = photomosaicService.createPhotomosaic(resultId);
//
//        //임시 경로에 저장된 포토 모자이크 S3로 업로드
//        log.info("S3 업로드 시작");
//        String photomosaicPath = s3Service.uploadPhotomosaic(photomosaic, resultId);
//        resultService.updatePhotomosaic(resultId, photomosaicPath);
//        log.info("업로드 완료");
//
//        return new ResponseEntity<>(photomosaicPath, HttpStatus.CREATED);
//    }
}
