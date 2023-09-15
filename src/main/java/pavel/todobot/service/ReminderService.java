package pavel.todobot.service;

import pavel.todobot.domain.Reminder;
import pavel.todobot.dto.ReminderDto;

import java.time.Instant;

public interface ReminderService {
    Reminder fromDto(ReminderDto reminderDto);
    Reminder create(ReminderDto reminderDto);
    Reminder updateTime(long id, Instant remindAt);
}
