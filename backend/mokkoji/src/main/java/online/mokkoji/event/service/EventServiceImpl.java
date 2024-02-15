package online.mokkoji.event.service;

import io.openvidu.java.client.Session;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.EventErrorCode;
import online.mokkoji.common.exception.errorcode.ResultErrorCode;
import online.mokkoji.common.exception.errorcode.UserErrorCode;
import online.mokkoji.event.domain.Event;
import online.mokkoji.event.dto.request.MessageReqDto;
import online.mokkoji.event.repository.EventRepository;
import online.mokkoji.openvidu.dto.request.SessionReqDto;
import online.mokkoji.openvidu.dto.response.GroupSessionResDto;
import online.mokkoji.result.domain.Result;
import online.mokkoji.result.domain.rollingpaper.*;
import online.mokkoji.result.repository.BackgroundTemplateRepository;
import online.mokkoji.result.repository.PostitTemplateRepository;
import online.mokkoji.result.repository.ResultRepository;
import online.mokkoji.result.repository.RollingPaperRepository;
import online.mokkoji.user.domain.User;
import online.mokkoji.user.domain.UserAccount;
import online.mokkoji.user.dto.response.AccountResDto;
import online.mokkoji.user.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {


    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ResultRepository resultRepository;
    private final RollingPaperRepository rollingPaperRepository;
    private final BackgroundTemplateRepository backgroundTemplateRepository;
    private final PostitTemplateRepository postitTemplateRepository;
    private final RedisTemplate redisTemplate;


    // 호스트 Session 생성
    @Override
    public String createSession(SessionReqDto sessionDto) {

        //User 객체 가져오기
        User user = userRepository.findById(sessionDto.getUserId())
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        log.info("service user : {}", user.toString());

        // Event 객체 생성
        Event event = Event.createSession()
                .user(user)
                .sessionId(sessionDto.getSessionId())
                .startTime(sessionDto.getStartTime())
                .build();

        // repository에 저장
        Event savedEvent = eventRepository.save(event);

        // 빈 result도 생성
        Result result = new Result(savedEvent,user);
        Result savedResult = resultRepository.save(result);
        // 빈 rollingpaper 생성
        PostitTemplate postitTemplate = postitTemplateRepository.findByPostitName(PostitName.RAINBOW).orElseThrow(() -> new RestApiException(ResultErrorCode.POSTIT_NOT_FOUND));
        BackgroundTemplate backgroundTemplate = backgroundTemplateRepository.findByBackgroundName(BackgroundName.BASIC).orElseThrow(() -> new RestApiException(ResultErrorCode.BACKGROUND_NOT_FOUND));

        // rollingpaper 저장
        RollingPaper rollingPaper = RollingPaper.buildWithResult()
                .result(savedResult)
                .backgroundTemplate(backgroundTemplate)
                .postitTemplate(postitTemplate)
                .build();
        rollingPaperRepository.save(rollingPaper);

        return savedEvent.getSessionId();
    }


    // 호스트의 세션 status closed로 변경
    @Override
    public void deleteSession(String sessionId, SessionReqDto sessionReqDto) {

        // 세션의 호스트Id와 지금 전달받은 userId가 맞는지 확인
        Event event = eventRepository.findBySessionId(sessionId)
                .orElseThrow(()->new RestApiException(EventErrorCode.EVENT_NOT_FOUND));
        if (!event.getUser().getId().equals(sessionReqDto.getUserId())) {
            log.error("호스트Id가 아님"); //임시로 하는 거.
            throw new RestApiException(EventErrorCode.HOST_NOT_FOUND);
        }

        //session의 status를 CLOSED로 변경
        event.closeSession(sessionReqDto);

        //session 저장
        eventRepository.save(event);


    }

    // 롤링페이퍼 파일 받아서 유효성 검사
    @Override
    public Map<String, MultipartFile> createRollingpaperFileMap(MessageReqDto messageReqDto) {

        Map<String, MultipartFile> fileMap = new HashMap<>();
        // 음성이 있는 경우 map에 저장
        MultipartFile voice = messageReqDto.getVoice();
        if (voice != null && !voice.isEmpty()) {
            fileMap.put("voice", voice);
        }
        // 영상이 있는 경우 map에 저장
        MultipartFile video = messageReqDto.getVideo();
        if (video != null && !video.isEmpty()) {
            fileMap.put("video", video);
        }

        return fileMap;
    }

    // 행사 정보 하나 받아오기
    @Override
    public Event getEvent(String sessionId) {
        return eventRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RestApiException(EventErrorCode.EVENT_NOT_FOUND));
    }

    // redis에서 counter로 sessionId 생성 후 소그룹 정보 리턴
    @Override
    public Map<String, Object> getGroupSessionCounter(Map<String, Object> params) {

        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();


        // 미리 가공할 sessionId 뽑아놓기
        String sessionId = (String) params.get("customSessionId");

        String counterKey="counter::"+sessionId;

        // 키가 있든 없든 increment 메서드 호출
        valueOperations.increment(counterKey, 1);

        String count = String.valueOf(valueOperations.get(counterKey));


        params.remove("customSessionId");
        params.put("customSessionId", sessionId +"_"+count);


        return params;
    }

    // redis에 소그룹 정보 저장
    @Override
    public void createGroupSession(GroupSessionResDto groupSessionResDto) {



        ListOperations<String, GroupSessionResDto> listOperations = redisTemplate.opsForList();

        String key = "session::" + groupSessionResDto.getSessionId().substring(0,14);

        if (redisTemplate.hasKey(key)) {
            listOperations.rightPush(key, groupSessionResDto);
        } else {
            List<GroupSessionResDto> groupSessionList = new ArrayList<>();
            groupSessionList.add(groupSessionResDto);
            listOperations.rightPushAll(key, groupSessionList);
        }
    }

    // redis에서 소그룹 리스트 가져오기
    @Override
    public List<GroupSessionResDto> getGroupSession(String sessionId) {
        String key="session::"+sessionId;


        ListOperations<String, GroupSessionResDto> listOperations = redisTemplate.opsForList();

        Boolean b = redisTemplate.hasKey(key);


        if (redisTemplate.hasKey(key)) {
            List<GroupSessionResDto> range = listOperations.range(key, 0, -1);
            return range;
        } else {
            return new ArrayList<>();
        }
    }

    // 세션 정보, 호스트 이름, 계좌 정보
    @Override
    public Map<String, Object> getSession(Session session) {
        Map<String, Object> responseMap = new HashMap<>();

        User user = getEvent(session.getSessionId()).getUser();

        responseMap.put("session", session);
        responseMap.put("hostName", user.getName());

        return responseMap;
    }

    // 호스트 계좌 정보 얻기
    @Override
    public AccountResDto getHostAccount(String sessionId) {

        Event event = getEvent(sessionId);
        UserAccount userAccount = event.getUser().getUserAccount();

        if (userAccount != null) {
            return AccountResDto.builder()
                    .bank(userAccount.getBank())
                    .accountNumber(userAccount.getNumber())
                    .build();
        }

        return null;
    }
}
