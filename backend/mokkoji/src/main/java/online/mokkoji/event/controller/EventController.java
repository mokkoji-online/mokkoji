package online.mokkoji.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.s3.S3ServiceImpl;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.EventErrorCode;
import online.mokkoji.event.domain.Event;
import online.mokkoji.event.dto.request.MessageReqDto;
import online.mokkoji.event.dto.response.PhotoResDto;
import online.mokkoji.event.repository.EventRepository;
import online.mokkoji.event.service.EventService;
import online.mokkoji.result.domain.Result;
import online.mokkoji.result.dto.response.MessageResDto;
import online.mokkoji.result.service.ResultService;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.dto.response.AccountResDto;
import online.mokkoji.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/events")
public class EventController {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final EventService eventService;
    private final S3ServiceImpl s3Service;
    private final JwtUtil jwtUtil;
    private final ResultService resultService;

    /**
     * 사진 추가
     * @param sessionId 세션 ID
     * @param photo 사진 파일
     * @return 완료 안내 텍스트
     * @throws IOException
     */
    @PostMapping("/{sessionId}/photos")
    public ResponseEntity<String> addPhoto(@PathVariable("sessionId") String sessionId,
                                           MultipartFile photo) throws IOException {

        // 사진 업로드
        Event event = eventRepository.findBySessionId(sessionId)
                .orElseThrow(()->new RestApiException(EventErrorCode.EVENT_NOT_FOUND));
        Long hostId = event.getUser().getId();
        Result result = event.getResult();
        PhotoResDto photoResDto = s3Service.uploadOnePhoto(photo, hostId, result);

        // db에 저장
        resultService.createPhoto(photoResDto);

        return new ResponseEntity<>("사진 업로드 완료", HttpStatus.OK);
    }

    /**
     * 롤링페이퍼 메시지 추가
     * @param sessionId 세션 ID
     * @param voice 음성 파일
     * @param video 비디오 파일
     * @param messageReqDto 작성자, 편지
     * @return 완료 안내 텍스트
     * @throws IOException
     */
    @PostMapping("/{sessionId}/rollingpapers")
    public ResponseEntity<String> addRollingpaper(@PathVariable("sessionId") String sessionId,
                                                  @RequestPart(value = "audio", required = false) MultipartFile voice,
                                                  @RequestPart(value = "video", required = false) MultipartFile video,
                                                  @RequestPart("writerAndText") MessageReqDto messageReqDto) throws IOException {


        Event event = eventRepository.findBySessionId(sessionId)
                .orElseThrow(()->new RestApiException(EventErrorCode.EVENT_NOT_FOUND));

        Long hostId = event.getUser().getId();
        Long resultId = event.getResult().getId();
        Long paperId = event.getResult().getRollingpaper().getId();

        messageReqDto.setVoice(voice);
        messageReqDto.setVideo(video);

        // 파일들 유효성 검사 후 map에 담음
        Map<String, MultipartFile> fileMap = eventService.createRollingpaperFileMap(messageReqDto);

        // 유효성 검사 후 파일 S3에 업로드
        Map<String, String> urlMap = s3Service.uploadRollingpaper(fileMap, hostId, resultId);
        MessageResDto messageResDto = new MessageResDto(paperId, messageReqDto.getWriter(), messageReqDto.getText(), urlMap);
        resultService.createMessage(messageResDto);
        return new ResponseEntity<>("롤링페이퍼 업로드 완료", HttpStatus.OK);

    }

    /**
     * 호스트 계좌 조회
     * @param sessionId 세션 ID
     * @return 호스트 계좌 정보
     */
    @GetMapping("/{sessionId}/accounts")
    public ResponseEntity<AccountResDto> getAccount(@PathVariable("sessionId") String sessionId) {
        AccountResDto accountResDto=eventService.getHostAccount(sessionId);

        return new ResponseEntity<>(accountResDto,HttpStatus.OK);
    }
}
