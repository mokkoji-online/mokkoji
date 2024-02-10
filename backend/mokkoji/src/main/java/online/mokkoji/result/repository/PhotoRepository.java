package online.mokkoji.result.repository;

import online.mokkoji.result.domain.Photo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom{
    @Cacheable(value = "photos", key = "#resultId", cacheManager = "cacheManager")
    List<Photo> findAllByResultId(Long resultId);
}
