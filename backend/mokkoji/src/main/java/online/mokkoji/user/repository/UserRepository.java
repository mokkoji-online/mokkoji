package online.mokkoji.user.repository;


import online.mokkoji.user.domain.Provider;
import online.mokkoji.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByProviderAndEmail(Provider provider, String email);
}
