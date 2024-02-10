package online.mokkoji.result.repository;

import online.mokkoji.result.domain.Result;
import online.mokkoji.result.domain.RollingPaper.Message;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    Page<Message> findAllByRollingPaper_Id(Long rollingpaperId, Pageable pageable);
}
