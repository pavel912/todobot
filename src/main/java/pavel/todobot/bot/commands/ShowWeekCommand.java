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

import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class ShowWeekCommand implements Command {
    @Autowired
    private final ToDoListService toDoListService;


    @Override
    public String execute(User currentUser, String message, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        return execute(currentUser, messageHandler);
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        List<ToDoList> toDoLists = toDoListService.getPreviousWeekLists(currentUser.getId());
        if (toDoLists.isEmpty()) {
            return "За неделю не было залогировано ни одной задачи";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Задачи за неделю по дням:\n");

        toDoLists
                .stream()
                .map(toDoListService::toDto)
                .map(messageHandler::produceToDoList)
                .forEach(x -> {
                    stringBuilder.append(x);
                    stringBuilder.append("\n\n");
                });

        return stringBuilder.toString();
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
