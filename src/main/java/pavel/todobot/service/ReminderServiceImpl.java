package pavel.todobot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pavel.todobot.domain.Reminder;
import pavel.todobot.dto.ReminderDto;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.repository.ReminderRepository;

import java.time.Instant;

@Service
@Transactional
@AllArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    @Autowired
    private final ReminderRepository reminderRepository;

    @Override
    public Reminder fromDto(ReminderDto reminderDto) {
        Reminder reminder = new Reminder();
        reminder.setUser(reminderDto.getUser());
        reminder.setReminderCommand(reminderDto.getReminderCommand());
        reminder.setRemindAt(reminderDto.getRemindAt());
        return reminder;
    }

    @Override
    public Reminder create(ReminderDto reminderDto) {
        Reminder reminder = fromDto(reminderDto);

        return reminderRepository.save(reminder);
    }

    @Override
    public Reminder updateTime(long id, Instant remindAt) {
        Reminder reminder = reminderRepository.findById(id);

        if (reminder == null) {
            throw new EntityDoesNotExistException(id);
        }

        reminder.setRemindAt(remindAt);

        return reminderRepository.save(reminder);
    }
}
