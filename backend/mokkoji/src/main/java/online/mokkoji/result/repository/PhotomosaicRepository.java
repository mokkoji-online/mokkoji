package online.mokkoji.result.repository;

import online.mokkoji.result.domain.Photomosaic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotomosaicRepository extends JpaRepository<Photomosaic, Long> {
    Optional<Photomosaic> findByResult_Id(Long resultId);
}

