package online.mokkoji.user.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.EventErrorCode;
import online.mokkoji.event.domain.Event;
import online.mokkoji.event.repository.EventRepository;
import online.mokkoji.user.domain.Record;
import online.mokkoji.user.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    private final EventRepository eventRepository;


    //마이페이지 활동 기록 업데이트
    @Override
    public void updateRecord(String sessionId) {
        Event event = eventRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RestApiException(EventErrorCode.EVENT_NOT_FOUND));

        Record record = event.getUser().getRecord();

        //회의 시간 계산
        long totalTime = ChronoUnit.HOURS.between(event.getStartTime(), event.getEndTime());

        int participantCount = event.getParticipantCount();
        int totalMessage = event.getResult().getRollingpaper().getMessages().size();

        record.updateRecord(totalTime,participantCount,totalMessage);
        recordRepository.save(record);
    }
}
