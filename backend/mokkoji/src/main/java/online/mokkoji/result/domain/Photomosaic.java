package online.mokkoji.result.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
public class Photomosaic  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photomosaic_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

    @Column(length = 100)
    @Size(max = 100)
    private String path;

    public void updatePath(String path) {
        this.path = path;
    }
}

