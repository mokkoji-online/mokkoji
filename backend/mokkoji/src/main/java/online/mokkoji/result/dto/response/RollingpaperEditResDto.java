package online.mokkoji.result.dto.response;

import lombok.*;
import online.mokkoji.result.domain.rollingpaper.BackgroundTemplate;
import online.mokkoji.result.domain.rollingpaper.PostitTemplate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RollingpaperEditResDto {
    private BackgroundTemplate backgroundTemplate;
    private PostitTemplate postitTemplate;

}
