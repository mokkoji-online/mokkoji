package online.mokkoji.event.repository;

import online.mokkoji.event.domain.Event;
import online.mokkoji.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findBySessionId(String sessionId);

}
