package pavel.todobot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pavel.todobot.domain.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    Task findById(long id);
    @Override
    Iterable<Task> findAllById(Iterable<Long> longs);
}