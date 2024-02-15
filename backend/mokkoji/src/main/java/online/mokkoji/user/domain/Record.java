package online.mokkoji.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "eventCount", "totalTime", "totalParticipant", "totalMessage"})
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int eventCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalTime;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalParticipant;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalMessage;

    @Builder
    public Record(User user, int eventCount, int totalTime, int totalParticipant, int totalMessage) {
        this.user = user;
        this.eventCount = eventCount;
        this.totalTime = totalTime;
        this.totalParticipant = totalParticipant;
        this.totalMessage = totalMessage;
    }

    public void updateRecord(long totalTime, int participantCount, int totalMessage) {
        this.eventCount++;
        this.totalTime += (int) totalTime;
        this.totalParticipant += participantCount;
        this.totalMessage+=totalMessage;
    }
}
