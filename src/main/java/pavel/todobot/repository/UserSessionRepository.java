package pavel.todobot.repository;

import org.springframework.data.repository.CrudRepository;
import pavel.todobot.domain.UserSession;

import java.util.List;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    UserSession findById(long id);

    List<UserSession> findByUserIdAndIsActiveOrderByCreatedAt(long userId, boolean isActive);
}
