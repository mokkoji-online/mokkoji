package online.mokkoji.openvidu.controller;

import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.openvidu.dto.request.SessionReqDto;
import online.mokkoji.event.service.EventService;
import online.mokkoji.event.repository.EventRepository;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class OpenviduController {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    // Session 생성
    @PostMapping("/api/sessions")
    public ResponseEntity<Map<String, String>> addSession(@RequestBody(required = false) Map<String, Object> params,
                                                          HttpServletRequest req)
            throws OpenViduJavaClientException, OpenViduHttpException {

        User user = userService.getByProviderAndEmail(jwtUtil.getProvider(req), jwtUtil.getEmail(req));

        // request body 객체로 직렬화
        SessionProperties properties = SessionProperties.fromJson(params).build();

        //세션 생성
        Session session = openvidu.createSession(properties);

        // DB에 저장할 Dto 생성
        SessionReqDto sessionReqDto = new SessionReqDto(user.getId(), session.getSessionId(), session.createdAt());

        // DB에 저장
        eventService.createSession(sessionReqDto);

        // 리턴값(sessionId) 담는 map 생성
        Map<String, String> response = new HashMap<>();

        // map에 담기
        response.put("sessionId", session.getSessionId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Session 삭제
    @DeleteMapping("/api/sessions/{sessionId}")
    public ResponseEntity<String> deleteSession(@PathVariable("sessionId") String sessionId,
                                                HttpServletRequest req,
                                                @RequestBody(required = false) SessionReqDto sessionReqDto)
            throws OpenViduJavaClientException, OpenViduHttpException {

        User user = userService.getByProviderAndEmail(jwtUtil.getProvider(req), jwtUtil.getEmail(req));
        sessionReqDto.setUserId(user.getId());

        Session activeSession = openvidu.getActiveSession(sessionId);
        eventService.deleteSession(sessionId, sessionReqDto);

        activeSession.close();


        // TODO: 2024.01.28 redis에 남은 결과물 파일 저장(resultService)||아직 작성 중인 사람은?


        return new ResponseEntity<>("삭제 완료", HttpStatus.NO_CONTENT);
    }


    // Session에 Connect
    @PostMapping("/api/sessions/{sessionId}/connections")
    public ResponseEntity<Map<String, String>> addConnection(@PathVariable("sessionId") String sessionId,
                                                             @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);

        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = session.createConnection(properties);

        // return값 담기
        Map<String, String> response = new HashMap<>();
        response.put("connectionToken", connection.getToken());
        response.put("message", "Session에 참여자 연결 성공");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}