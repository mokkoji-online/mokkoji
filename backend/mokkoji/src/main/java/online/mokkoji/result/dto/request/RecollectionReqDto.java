package online.mokkoji.result.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecollectionReqDto {

    @Size(max = 10)
    private String name;

    @Size(max = 35)
    private String memo;
}
