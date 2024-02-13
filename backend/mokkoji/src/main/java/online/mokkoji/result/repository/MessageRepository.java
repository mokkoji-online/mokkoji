package online.mokkoji.result.repository;

import online.mokkoji.result.domain.rollingpaper.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    Page<Message> findAllByRollingPaper_Id(Long rollingpaperId, Pageable pageable);

    int countAllByRollingPaper_Id(Long rollingpaperId);
}
