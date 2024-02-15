package online.mokkoji.openvidu.controller;

import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.CommonErrorCode;
import online.mokkoji.event.service.EventService;
import online.mokkoji.openvidu.dto.request.SessionReqDto;
import online.mokkoji.openvidu.dto.response.GroupSessionResDto;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.service.RecordService;
import online.mokkoji.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
//@CrossOrigin(origins = {"http://mokkoji.online:5173"})
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/meetings")
public class OpenviduController {

    private final EventService eventService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RecordService recordService;


    @Value("${openvidu.url}")
    private String openviduUrl;

    @Value("${openvidu.secret}")
    private String openviduSecret;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(openviduUrl, openviduSecret);
    }


    /**
     * 메인 행사 세션 생성
     * @param params 세션 생성 정보
     * @param req 유저 Access Token
     * @return 세션 ID
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    @PostMapping("/sessions")
    public ResponseEntity<String> addSession(@RequestBody(required = false) Map<String, Object> params,
                                              HttpServletRequest req
    ) throws OpenViduJavaClientException, OpenViduHttpException {

        User user=userService.searchUser(jwtUtil.getProvider(req),jwtUtil.getEmail(req));

        // request body 객체로 직렬화
        SessionProperties properties = SessionProperties.fromJson(params).build();

        // 세션 생성
        Session session = openvidu.createSession(properties);

        // DB에 저장할 Dto 생성
        SessionReqDto sessionReqDto = new SessionReqDto(user.getId(), session.getSessionId());

        // DB에 저장
         eventService.createSession(sessionReqDto);

        return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
    }


    /**
     * 메인 세션 정보 조회
     * @param sessionId 조회할 세션 ID
     * @return 세션 정보, 호스트 이름
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSession(@PathVariable("sessionId") String sessionId)
            throws OpenViduJavaClientException, OpenViduHttpException {

        openvidu.fetch();
        List<Session> activeSessions = openvidu.getActiveSessions();
        for (Session session : activeSessions) {
            if (session.getSessionId().equals(sessionId)) {

                Map<String, Object> responseMap = eventService.getSession(session);

                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }

        throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
    }


    /**
     * 메인 세션 삭제
     * @param sessionId 삭제할 세션 ID
     * @param req 유저 Access Token
     * @param sessionReqDto 회의 최대 참가자 수
     * @return 204 코드
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<String> deleteSession(@PathVariable("sessionId") String sessionId,
                                                HttpServletRequest req,
                                                @RequestBody(required = false) SessionReqDto sessionReqDto)
            throws OpenViduJavaClientException, OpenViduHttpException {


        User user=userService.searchUser(jwtUtil.getProvider(req),jwtUtil.getEmail(req));

        sessionReqDto.setUserId(user.getId());

        Session activeSession = openvidu.getActiveSession(sessionId);
        eventService.deleteSession(sessionId, sessionReqDto);

        // 활동기록 업데이트
        recordService.updateRecord(sessionId);

        activeSession.close();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * 세션 연결 토큰 생성
     * @param sessionId 연결할 세션 ID
     * @param params 연결 정보
     * @return Connection Token
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    @PostMapping("/sessions/{sessionId}/connections")
    public ResponseEntity<Map<String, String>> addConnection(@PathVariable("sessionId") String sessionId,
                                                             @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);

        log.info("연결 sessionId : {}", sessionId);

        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = session.createConnection(properties);

        // return값 담기
        Map<String, String> response = new HashMap<>();
        response.put("connectionToken", connection.getToken());
        response.put("message", "Session에 참여자 연결 성공");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 소그룹 세션 생성
     * @param params 소그룹 세션 정보(customSessionId==메인 세션의 sessionId)
     * @return 소그룹 sessionId
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    @PostMapping("/groupsessions")
    public ResponseEntity<String> addGroupSession(@RequestBody(required = false) Map<String, Object> params
    ) throws OpenViduJavaClientException, OpenViduHttpException {

        // sessionId 생성
        Map<String, Object> newParams = eventService.getGroupSessionCounter(params);

        // request body 객체로 직렬화
        SessionProperties properties = SessionProperties.fromJson(newParams).build();

        //세션 생성
        Session session = openvidu.createSession(properties);

        // 소그룹 세션 dto 생성
        GroupSessionResDto groupSessionResDto = new GroupSessionResDto(session.getSessionId(), (String) params.get("groupName"));

        // redis에 저장
        eventService.createGroupSession(groupSessionResDto);

        return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
    }

    /**
     * 소그룹 리스트 조회
     * @param sessionId 소그룹 세션 ID
     * @return 소그룹 세션 리스트
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    @GetMapping("/groupsessions/{sessionId}")
    public ResponseEntity<?> getGroupSession(@PathVariable("sessionId") String sessionId)
            throws OpenViduJavaClientException, OpenViduHttpException {

        List<GroupSessionResDto> groupSessions=eventService.getGroupSession(sessionId);

        if (groupSessions.size()<1) return new ResponseEntity<>("소그룹 없음", HttpStatus.OK);


        return new ResponseEntity<>(groupSessions, HttpStatus.OK);
    }


}
