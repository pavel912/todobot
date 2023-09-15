package pavel.todobot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pavel.todobot.domain.ToDoList;

import java.time.LocalDate;

@Repository
public interface ToDoListRepository extends CrudRepository<ToDoList, Long> {
    ToDoList findById(long id);
    @Override
    Iterable<ToDoList> findAllById(Iterable<Long> longs);

    Iterable<ToDoList> findByUserIdAndPlannedOnBetweenOrderByPlannedOn(Long userId, LocalDate start, LocalDate end);
}