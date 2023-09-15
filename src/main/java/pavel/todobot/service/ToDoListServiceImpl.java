package pavel.todobot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pavel.todobot.domain.Task;
import pavel.todobot.domain.ToDoList;
import pavel.todobot.dto.ToDoListDto;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.repository.ToDoListRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ToDoListServiceImpl implements ToDoListService {

    @Autowired
    private final TaskService taskService;

    @Autowired
    private final ToDoListRepository toDoListRepository;

    @Override
    public ToDoList fromDto(ToDoListDto toDoListDto) {
        ToDoList toDoList = new ToDoList();
        toDoList.setPlannedOn(toDoListDto.getPlannedOn());
        toDoList.setTasks(
                toDoListDto
                        .getTasks()
                        .stream()
                        .map(taskService::fromDto)
                        .toList()
        );
        toDoList.setUser(toDoListDto.getUser());

        return toDoList;
    }

    @Override
    public ToDoListDto toDto(ToDoList toDoList) {
        ToDoListDto toDoListDto = new ToDoListDto();
        toDoListDto.setTasks(
                toDoList
                        .getTasks()
                        .stream()
                        .map(taskService::toDto)
                        .toList()
        );

        return toDoListDto;
    }

    @Override
    public ToDoList get(Long id) {
        ToDoList toDoList = toDoListRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException(id));

        return toDoList;
    }

    @Override
    public List<ToDoList> getAll() {
        List<ToDoList> toDoLists = IterableUtils.toList(toDoListRepository.findAll());

        return toDoLists;
    }

    @Override
    public ToDoList getYesterdayList(Long userId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<ToDoList> toDoLists = getAllInTimePeriod(userId, yesterday, yesterday);
        if (toDoLists.isEmpty()) {
            return null;
        }

        return toDoLists.get(0);
    }

    @Override
    public ToDoList getTodayList(Long userId) {
        LocalDate today = LocalDate.now();
        List<ToDoList> toDoLists = getAllInTimePeriod(userId, today, today);
        if (toDoLists.isEmpty()) {
            return null;
        }

        return toDoLists.get(0);
    }

    @Override
    public ToDoList getTomorrowList(Long userId) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<ToDoList> toDoLists = getAllInTimePeriod(userId, tomorrow, tomorrow);
        if (toDoLists.isEmpty()) {
            return null;
        }

        return toDoLists.get(0);
    }

    @Override
    public List<ToDoList> getPreviousWeekLists(Long userId) {
        LocalDate today = LocalDate.now();

        return getAllInTimePeriod(userId, today.minusWeeks(1), today.minusDays(1));
    }

    private List<ToDoList> getAllInTimePeriod(Long userId, LocalDate periodStart, LocalDate periodEnd) {
        List<ToDoList> toDoLists =
                IterableUtils.toList(
                        toDoListRepository
                                .findByUserIdAndPlannedOnBetweenOrderByPlannedOn(userId, periodStart, periodEnd)
                );

        return toDoLists;
    }

    @Override
    public void create(ToDoListDto toDoListDto) {
        ToDoList toDoList = fromDto(toDoListDto);

        toDoList = toDoListRepository.save(toDoList);

        for (Task task : toDoList.getTasks()) {
            task.setToDoList(toDoList);
            taskService.create(task);
        }
    }

    @Override
    public void add(Long id, ToDoListDto toDoListDto) {
        ToDoList toDoList = toDoListRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException(id));

        toDoListDto
                .getTasks()
                .stream()
                .map(taskService::fromDto).forEach(x -> {
                    x.setToDoList(toDoList);
                    taskService.create(x);
                });
    }

    @Override
    public void update(Long id, ToDoListDto toDoListDto) {
        ToDoList toDoList = toDoListRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException(id));
        toDoListRepository.delete(toDoList);

        create(toDoListDto);
    }
}
