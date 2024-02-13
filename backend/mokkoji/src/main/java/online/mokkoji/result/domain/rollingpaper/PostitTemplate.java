package online.mokkoji.result.domain.rollingpaper;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@NoArgsConstructor
@ToString
public class PostitTemplate {

    @Id
    @GeneratedValue
    @Column(name = "postit_id")
    private int id;

    @Enumerated(EnumType.STRING)
    private PostitName postitName;
    private String postitPath;


    public PostitTemplate(PostitName postitName, String postitPath) {
        this.postitName = postitName;
        this.postitPath = postitPath;
    }
}