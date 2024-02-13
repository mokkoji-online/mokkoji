package online.mokkoji.event.service;

import online.mokkoji.event.domain.Event;
import online.mokkoji.event.dto.request.MessageReqDto;
import online.mokkoji.openvidu.dto.request.SessionReqDto;
import online.mokkoji.openvidu.dto.response.GroupSessionResDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface EventService {


    // Session 생성
    String createSession(SessionReqDto sessionReqDto);

    // Session 닫기
    void deleteSession(String sessionId, SessionReqDto sessionReqDto);

    Map<String, MultipartFile> createRollingpaperFileMap(MessageReqDto messageReqDto);

    Event getEvent(String sessionId);

    Map<String, Object> getGroupSessionCounter(Map<String, Object> groupSessionResDto);

    void createGroupSession(GroupSessionResDto groupSessionResDto);

    List<GroupSessionResDto> getGroupSession(String sessionId);
}
