package pavel.todobot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pavel.todobot.domain.Reminder;

@Repository
public interface ReminderRepository extends CrudRepository<Reminder, Long> {
    Reminder findById(long id);
}
