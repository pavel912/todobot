package pavel.todobot.reminder.comands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pavel.todobot.domain.User;
import pavel.todobot.messagehandler.MessageHandler;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class LogReminderCommand implements ReminderCommand {
    @Override
    public String execute() {
        return "Напоминаю, что можно залогировать задачи за сегодня.";
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) {
        return execute();
    }
}
