package online.mokkoji.result.domain.RollingPaper;

import jakarta.persistence.*;
import lombok.*;
import online.mokkoji.result.domain.Result;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RollingPaper {

    @Id
    @GeneratedValue
    @Column(name = "rollingpaper_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isEdited;

    @OneToOne(mappedBy = "rollingPaper", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = false)
    private BackgroundTemplate backgroundTemplate;

    @OneToOne(mappedBy = "rollingPaper", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = false)
    private PostitTemplate postitTemplate;

    @Builder(builderMethodName = "buildWithResult")
    public RollingPaper(Result result, BackgroundTemplate backgroundTemplate, PostitTemplate postitTemplate) {
        this.result = result;
        setBackgroundTemplate(backgroundTemplate);
        setPostitTemplate(postitTemplate);
        result.setRollingpaper(this);
    }

    public void setBackgroundTemplate(BackgroundTemplate backgroundTemplate) {
        this.backgroundTemplate = backgroundTemplate;
    }

    public void setPostitTemplate(PostitTemplate postitTemplate) {
        this.postitTemplate = postitTemplate;
    }
}
