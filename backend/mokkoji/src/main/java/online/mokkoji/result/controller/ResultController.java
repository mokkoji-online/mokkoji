package online.mokkoji.result.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.result.dto.request.RecollectionReqDto;
import online.mokkoji.result.service.PhotomosaicService;
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
import org.springframework.data.web.PageableDefault;
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
    private final PhotomosaicService photomosaicService;

    // 행사 리스트
    @GetMapping("/lists")
    public ResponseEntity<Map<String, Object>> getResultList(HttpServletRequest req) {

        Map<String, Object> result = resultService.getResultList(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 기억 편집화면
    @GetMapping("/{resultId}/memories")
    public ResponseEntity<Map<String, Object>> getRollingpaperAndPhotoEdit(@PathVariable Long resultId) {

        List<String> photoPath = resultService.getPhotoPath(resultId);
        RollingpaperEditResDto rollingpaperTemplate = resultService.getRollingpaperTemplate(resultId);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("photoPath", photoPath);
        responseMap.put("rollingpaperTemplate", rollingpaperTemplate);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    // 롤링페이퍼 편집 완료
    @PatchMapping("/{resultId}/memories/rollingpaper")
    public ResponseEntity<String> updateRollingpaper(@PathVariable Long resultId,
                                                     @RequestBody RollingPaperReqDto rollingPaperReqDto) {

        resultService.updateRollingpaper(resultId, rollingPaperReqDto);

        return new ResponseEntity<>("편집 완료", HttpStatus.OK);
    }


    // 사진첩 사진 추가
    @PostMapping("/{resultId}/memories/photos")
    public ResponseEntity<String> addPhotos(@PathVariable("resultId") Long resultId,
                                           HttpServletRequest req,
                                           @RequestParam("photos") List<MultipartFile> photoList) throws IOException {

        User user = userService.getByProviderAndEmail(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        // 사진 업로드
        List<PhotoResDto> photoResDtoList = s3Service.uploadPhotoList(photoList, user.getId(), resultId);

        // db에 저장
        resultService.createPhotoList(photoResDtoList);

        // redis cache에 저장
        resultService.updatePhotoPathCache(resultId, photoResDtoList);

        return new ResponseEntity<>("사진 업로드 완료", HttpStatus.OK);
    }


    // 대표이미지 설정
    @PatchMapping("/{resultId}/memories")
    public ResponseEntity<String> updateThumbnail(@PathVariable("resultId") Long resultId, @RequestBody String url) {
        resultService.updateThumbnail(resultId, url);
        return new ResponseEntity<>("대표이미지 설정 완료", HttpStatus.OK);
    }

    // 추억 결과물 보기(롤링페이퍼)
    @GetMapping("/recollections/{resultId}")
    public ResponseEntity<ResultResDto> getResult(@PathVariable Long resultId, @PageableDefault(page = 0, size = 9) Pageable pageable) {

        ResultResDto resultResDto = resultService.getResult(resultId, pageable);

        return new ResponseEntity<>(resultResDto, HttpStatus.OK);
    }

    // 추억 생성(S3 포토모자이크 링크 DB에 저장, 로컬 cellImages 삭제)
    @PostMapping("/{resultId}")
    public ResponseEntity<Map<String, Object>> addRecollection(@PathVariable Long resultId,
                                                               @RequestBody RecollectionReqDto recollectionReqDto, HttpServletRequest req) {
        resultService.createRecollection(resultId, recollectionReqDto);

        // S3에서 대표이미지 제외 사진 삭제
        s3Service.deletePhotos(resultId);

        Map<String, Object> result = resultService.getResultList(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        //로컬 셀 이미지 삭제
        String cellImagesDirectory = System.getProperty("user.home") + File.separator + "Downloads" +
                File.separator + "mokkoji" + File.separator + resultId;

        photomosaicService.deleteCellImages(cellImagesDirectory);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //대표사진 다운로드
    @GetMapping("/{resultId}/thumbnail")
    public ResponseEntity<Void> downloadThumbnail(@PathVariable Long resultId) {
        String thumbnailPath = resultService.getThumbnailPath(resultId);

        s3Service.downloadWithUrl(thumbnailPath);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{resultId}/photomosaic")
    public ResponseEntity<Void> downloadPhotomosaic(@PathVariable Long resultId) {
        String photomosaicPath = resultService.getPhotomosaicPath(resultId);

        s3Service.downloadWithUrl(photomosaicPath);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{resultId}/sharing/thumbnail")
    public ResponseEntity<String> shareThumbnail(@PathVariable Long resultId) {
        String imageFilename = resultService.getImageFileName(resultId);

        String downloadUrl = s3Service.createDownloadUrl(imageFilename);

        return new ResponseEntity<>(downloadUrl, HttpStatus.CREATED);
    }

    @GetMapping("{resultId}/sharing/photomosaic/")
    public ResponseEntity<String> sharePhotoMosaic(@PathVariable Long resultId) {
        String photomosaicFilename = resultService.getPhotoMosaicFileName(resultId);

        String downloadUrl = s3Service.createDownloadUrl(photomosaicFilename);

        return new ResponseEntity<>(downloadUrl, HttpStatus.CREATED);
    }

    //포토 모자이크 생성
    @PutMapping("{resultId}/photomosaic")
    public ResponseEntity<String> addPhotomosaic(@PathVariable("resultId") Long resultId) {
        //S3에 저장된 thumbnail, images 임시 다운로드(경로 확인 필요)
        String thumbnailPath = resultService.getThumbnailPath(resultId);

        String localThumbnail = s3Service.downloadWithUrl(thumbnailPath);
        String cellImagesPath = s3Service.downloadCellImages(resultId);

        //photomosaic 생성, 임시 경로에 저장
        String photomosaic = photomosaicService.createPhotomosaic(localThumbnail, cellImagesPath);

        //임시 경로에 저장된 포토 모자이크 S3로 업로드
        String photomosaicPath = s3Service.uploadPhotomosaic(photomosaic, resultId);
        resultService.updatePhotomosaic(resultId, photomosaicPath);

        return new ResponseEntity<>(photomosaicPath, HttpStatus.CREATED);
    }
}
