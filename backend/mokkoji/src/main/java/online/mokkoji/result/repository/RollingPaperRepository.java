package online.mokkoji.result.repository;

import online.mokkoji.result.domain.rollingpaper.RollingPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RollingPaperRepository extends JpaRepository<RollingPaper, Long> {
    Optional<RollingPaper> findById(Long rollingpaperId);
}
