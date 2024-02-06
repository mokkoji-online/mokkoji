package online.mokkoji.result.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RollingPaper {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isEdited;

    public RollingPaper(Result result) {
        this.builder()
                .result(result)
                .build();
        result.setRollingpaper(this);
    }
}