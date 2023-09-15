package pavel.todobot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pavel.todobot.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListDto {
    private LocalDate plannedOn;

    private List<TaskDto> tasks = new ArrayList<>();

    private User user;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TaskDto task : tasks) {
            stringBuilder.append(task.toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}
