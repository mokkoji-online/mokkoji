package online.mokkoji.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 10)
    @Size(max = 10)
    private String bank;

    @Column(nullable = false, length = 20)
    @Size(max = 20)
    private String number;

    @Builder
    public UserAccount(User user, String bank, String number) {
        this.user = user;
        this.bank = bank;
        this.number = number;
    }

    public void updateAccount(String bank, String accountNumber) {
        this.bank = bank;
        this.number = accountNumber;
    }
}
