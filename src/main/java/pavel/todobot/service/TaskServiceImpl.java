package pavel.todobot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pavel.todobot.domain.Task;
import pavel.todobot.dto.TaskDto;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.repository.TaskRepository;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Override
    public Task fromDto(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setLoggedHours(taskDto.getLoggedHours());

        return task;
    }

    @Override
    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(task.getName());
        taskDto.setLoggedHours(task.getLoggedHours());

        return taskDto;
    }

    @Override
    public Task get(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException(id));

        return task;
    }

    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }


}
