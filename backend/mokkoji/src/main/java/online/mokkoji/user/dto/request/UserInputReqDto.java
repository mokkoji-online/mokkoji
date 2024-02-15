package online.mokkoji.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInputReqDto {

    @NotNull
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String image;

    private String bank;
    private String accountNumber;

    @Builder
    public UserInputReqDto(String email, String name, String image, String bank, String accountNumber) {
        this.email = email;
        this.name = name;
        this.image = image;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }
}
