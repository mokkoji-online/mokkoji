package online.mokkoji.result.domain.rollingpaper;

import jakarta.persistence.*;
import lombok.*;
import online.mokkoji.common.domain.BaseEntity;
import online.mokkoji.result.domain.Result;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RollingPaper extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rollingpaper_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isEdited;

    @OneToMany(mappedBy = "rollingPaper")
    List<Message> messages =new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "background_id")
    private BackgroundTemplate backgroundTemplate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "postit_id")
    private PostitTemplate postitTemplate;


    @Builder(builderMethodName = "buildWithResult")
    public RollingPaper(Result result, BackgroundTemplate backgroundTemplate, PostitTemplate postitTemplate) {
        this.result = result;
        updateTemplate(backgroundTemplate,postitTemplate);
        result.setRollingpaper(this);
    }

    public void updateTemplate(BackgroundTemplate backgroundTemplate, PostitTemplate postitTemplate) {
        this.backgroundTemplate=backgroundTemplate;
        this.postitTemplate=postitTemplate;
        this.isEdited=true;
    }




}
