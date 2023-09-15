package pavel.todobot.reminder.comands;

import pavel.todobot.domain.User;
import pavel.todobot.messagehandler.MessageHandler;

public interface ReminderCommand {
    String execute();

    String execute(User currentUser, MessageHandler messageHandler);
}
