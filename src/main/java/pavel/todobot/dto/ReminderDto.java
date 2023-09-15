package pavel.todobot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pavel.todobot.domain.User;
import pavel.todobot.reminder.comands.ReminderCommandEnum;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderDto {
    private User user;

    private ReminderCommandEnum reminderCommand;

    private Instant remindAt;
}
