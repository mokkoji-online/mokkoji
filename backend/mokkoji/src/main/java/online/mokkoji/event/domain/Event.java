package online.mokkoji.event.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.EventErrorCode;
import online.mokkoji.openvidu.dto.request.SessionReqDto;
import online.mokkoji.result.domain.Result;
import online.mokkoji.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

import static online.mokkoji.event.domain.EventStatus.*;

@Entity
@Table(name = "event")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@ToString(of = {"id", "participantCount", "status", "startTime", "endTime"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_id", length = 100)
    @Size(max = 100)
    private String sessionId;

    @ColumnDefault("0")
    @Column(name = "participant_count")
    private int participantCount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus status = ACTIVE;


    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Result result;

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getEvents().add(this);
    }

    public void setResult(Result result) {
        this.result = result;
    }


    //==생성자==//
    @Builder(builderMethodName = "createSession")
    public Event(User user, String sessionId, LocalDateTime startTime) {
        this.user = user;
        this.sessionId = sessionId;
        this.startTime = startTime;
    }

    // Session 종료 시 정보 추가, CLOSED로 변경
    public void closeSession(SessionReqDto sessionReqDto) {

        // 이미 끝나있는 세션이라면
        if (this.getStatus() == CLOSED) {
            throw new RestApiException(EventErrorCode.ALREADY_CLOSED_EVENT);
        }

        this.status = CLOSED;
        this.participantCount = sessionReqDto.getParticipantCount();
        this.endTime = LocalDateTime.now();
    }


}
