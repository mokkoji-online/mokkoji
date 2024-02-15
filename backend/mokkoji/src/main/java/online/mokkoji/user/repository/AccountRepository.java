package online.mokkoji.user.repository;

import online.mokkoji.user.domain.Provider;
import online.mokkoji.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUser_ProviderAndUser_Email(Provider provider, String email);
}
