package online.mokkoji.user.dto.response;

import lombok.*;

@Getter @Setter
public class AccountResDto {
    private String bank;
    private String accountNumber;

    @Builder
    public AccountResDto(String bank, String accountNumber) {
        this.bank = bank;
        this.accountNumber = accountNumber;
    }
}
