package pavel.todobot.reminder.comands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pavel.todobot.domain.ToDoList;
import pavel.todobot.domain.User;
import pavel.todobot.exception.NotImplementedException;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.service.ToDoListService;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class ShowTasksReminderCommand implements ReminderCommand {
    @Autowired
    private final ToDoListService toDoListService;

    @Override
    public String execute() {
        throw new NotImplementedException();
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) {
        String message = "Ваши задачи на сегодня:";
        ToDoList toDoList = toDoListService.getTodayList(currentUser.getId());
        if (toDoList == null) {
            message = "Ваши вчерашние задачи:";
            toDoList = toDoListService.getYesterdayList(currentUser.getId());
        }

        if (toDoList == null) {
            return "Котик заметил, что вчера вы не логировали задачи и не планировали задач на сегодня. Котик грустит(";
        }

        return message + "\n" + messageHandler.produceToDoList(toDoListService.toDto(toDoList));
    }
}
