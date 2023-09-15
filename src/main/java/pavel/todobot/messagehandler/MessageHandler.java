package pavel.todobot.messagehandler;

import pavel.todobot.dto.ToDoListDto;
import pavel.todobot.exception.InvalidMessageFormatException;

import java.util.List;

public interface MessageHandler {
    ToDoListDto parseToDoList(String message) throws InvalidMessageFormatException;

    String produceToDoList(ToDoListDto toDoListDto);
}
