package pavel.todobot.bot.commands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pavel.todobot.domain.ToDoList;
import pavel.todobot.domain.User;
import pavel.todobot.dto.ToDoListDto;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.exception.InvalidMessageFormatException;
import pavel.todobot.exception.NotImplementedException;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.service.ToDoListService;

import java.time.LocalDate;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class PlanCommand implements Command {

    @Autowired
    private ToDoListService toDoListService;
    @Override
    public String execute(User currentUser, String message, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        ToDoListDto toDoListDto = messageHandler.parseToDoList(message);
        toDoListDto.setPlannedOn(LocalDate.now().plusDays(1));
        toDoListDto.setUser(currentUser);
        ToDoList toDoList = toDoListService.getTomorrowList(currentUser.getId());
        if (toDoList == null) {
            toDoListService.create(toDoListDto);
            return "Новый список на завтра успешно создан";
        }

        toDoListService.add(toDoList.getId(), toDoListDto);
        return "Завтрашний список успешно обновлен";
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        throw new NotImplementedException();
    }

    @Override
    public String execute(User currentUser) throws InvalidMessageFormatException, EntityDoesNotExistException {
        throw new NotImplementedException();
    }

    @Override
    public boolean requireParams() {
        return true;
    }

    @Override
    public String getRequiredParamsMessage() {
        return "Введите список из задач в выбранном формате";
    }
}
