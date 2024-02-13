package online.mokkoji.result.repository;

import online.mokkoji.result.domain.rollingpaper.BackgroundName;
import online.mokkoji.result.domain.rollingpaper.BackgroundTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackgroundTemplateRepository extends JpaRepository<BackgroundTemplate, Integer> {
    Optional<BackgroundTemplate> findByBackgroundName(BackgroundName backgroundName);
}
