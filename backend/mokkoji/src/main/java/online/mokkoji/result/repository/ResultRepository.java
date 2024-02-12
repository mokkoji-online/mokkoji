package online.mokkoji.result.repository;

import online.mokkoji.result.domain.Result;
import online.mokkoji.user.domain.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByUser_ProviderAndUser_EmailOrderByIdDesc(Provider provider, String email);

    Optional<Result> findById(Long resultId);

}