package pavel.todobot.repository;

import org.springframework.data.repository.CrudRepository;
import pavel.todobot.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(long id);

    User findByTelegramUserName(String username);
}
