package pavel.todobot.bot.commands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pavel.todobot.domain.User;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.exception.InvalidMessageFormatException;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.service.ReminderService;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class SnoozeCommand implements Command {
    @Autowired
    private final ReminderService reminderService;

    @Override
    public String execute(User currentUser, String message, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        return execute(currentUser);
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        return execute(currentUser);
    }

    @Override
    public String execute(User currentUser) throws InvalidMessageFormatException, EntityDoesNotExistException {
        currentUser.getReminders().stream().forEach(x -> {
            reminderService
                    .updateTime(x.getId(), x.getRemindAt().plusSeconds(7 * 24 * 3600));
        });

        return "Уведомления заглушены на неделю";
    }

    @Override
    public boolean requireParams() {
        return false;
    }

    @Override
    public String getRequiredParamsMessage() {
        return "Нет требуемых параметров";
    }
}
