package pavel.todobot.bot.commands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pavel.todobot.domain.ToDoList;
import pavel.todobot.domain.User;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.exception.InvalidMessageFormatException;
import pavel.todobot.exception.NotImplementedException;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.service.ToDoListService;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class ShowTomorrowCommand implements Command {
    @Autowired
    private ToDoListService toDoListService;

    @Override
    public String execute(User currentUser, String message, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        return execute(currentUser, messageHandler);
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        ToDoList toDoList = toDoListService.getTomorrowList(currentUser.getId());
        if (toDoList == null) {
            return "На завтра еще не было запланировано ни одной задачи";
        }

        return messageHandler.produceToDoList(toDoListService.toDto(toDoList));
    }

    @Override
    public String execute(User currentUser) throws InvalidMessageFormatException, EntityDoesNotExistException {
        throw new NotImplementedException();
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
