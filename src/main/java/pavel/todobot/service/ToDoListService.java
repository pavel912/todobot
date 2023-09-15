package pavel.todobot.service;

import pavel.todobot.domain.ToDoList;
import pavel.todobot.dto.ToDoListDto;

import java.util.List;

public interface ToDoListService {
    ToDoList fromDto(ToDoListDto toDoListDto);
    ToDoListDto toDto(ToDoList toDoList);

    ToDoList get(Long id);

    List<ToDoList> getAll();

    ToDoList getYesterdayList(Long userId);

    ToDoList getTodayList(Long userId);

    ToDoList getTomorrowList(Long userId);

    List<ToDoList> getPreviousWeekLists(Long userId);

    void create(ToDoListDto toDoListDto);

    void add(Long id, ToDoListDto toDoListDto);

    void update(Long id, ToDoListDto toDoListDto);
}
