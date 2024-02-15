package online.mokkoji.result.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import online.mokkoji.common.domain.BaseEntity;
import online.mokkoji.event.domain.Event;
import online.mokkoji.result.domain.rollingpaper.RollingPaper;
import online.mokkoji.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(of = {"id","name", "content", "status","image"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.MEMORY;

    @Column(length = 15)
    @Size(max = 15)
    private String name;

    @Column(length = 40)
    @Size(max = 40)
    private String content;

    @Column(length = 255, columnDefinition = "varchar(255) default 'https://mokkoji-bucket.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EB%B3%B8+%EC%82%AC%EC%A7%84/%EB%8C%80%ED%91%9C%EC%9D%B4%EB%AF%B8%EC%A7%80_%EA%B8%B0%EB%B3%B8.png'")
    private String image;

    @OneToOne(mappedBy = "result", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private RollingPaper rollingpaper;

    @OneToOne(mappedBy = "result", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Photomosaic photomosaic;

    @OneToMany(mappedBy = "result")
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Result(Event event,User user) {
        this.event = event;
        this.user=user;
        this.image="https://mokkoji-bucket.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EB%B3%B8+%EC%82%AC%EC%A7%84/%EB%8C%80%ED%91%9C%EC%9D%B4%EB%AF%B8%EC%A7%80_%EA%B8%B0%EB%B3%B8.png";
        event.setResult(this);
        user.getResults().add(this);
    }

    public Result(User user) {
        this.user = user;
    }

    public void updateStatus(String name, String memo) {
        this.name = name;
        this.content = memo;
        this.status = Status.RECOLLECTION;
    }

    public void setRollingpaper(RollingPaper rollingPaper) {
        this.rollingpaper = rollingPaper;
    }
    public void setImage(String url) {this.image=url;}
}
