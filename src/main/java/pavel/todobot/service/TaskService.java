package pavel.todobot.service;

import pavel.todobot.domain.Task;
import pavel.todobot.dto.TaskDto;

public interface TaskService {
    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);

    Task get(Long id);

    Task create(Task task);
}
