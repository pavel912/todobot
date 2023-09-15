package pavel.todobot.messagehandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pavel.todobot.dto.TaskDto;
import pavel.todobot.dto.ToDoListDto;
import pavel.todobot.exception.InvalidMessageFormatException;

import java.util.Arrays;

public class SimpleToDoListHandler implements MessageHandler {

    Logger logger = LoggerFactory.getLogger(SimpleToDoListHandler.class);
    @Override
    public ToDoListDto parseToDoList(String message) throws InvalidMessageFormatException {
        logger.info("Start parsing " + message);
        ToDoListDto toDoListDto = new ToDoListDto();
        for (String messagePart : message.split("\n")) {
            logger.info("Start parsing " + messagePart);
            if (messagePart.isBlank()) {
                continue;
            }

            TaskDto taskDto = parseTask(messagePart);
            logger.info("Finish parsing " + message);
            toDoListDto.getTasks().add(taskDto);
            logger.info("Added task");
        }

        if (toDoListDto.getTasks().isEmpty()) {
            logger.info("Tasks are empty");
            throw new InvalidMessageFormatException(message);
        }

        logger.info("Successfully parsed to do list");
        return toDoListDto;
    }

    private TaskDto parseTask(String taskMessage) throws InvalidMessageFormatException {
        TaskDto taskDto = new TaskDto();
        String [] messageParts = taskMessage.split("- ");
        try {
            logger.info("Split message parts" + Arrays.toString(messageParts));
            taskDto.setName(messageParts[1].strip());
            Integer loggedHours = null;
            if (messageParts.length > 2) {
                loggedHours = Integer.parseInt(messageParts[2].strip());
            }
            taskDto.setLoggedHours(loggedHours);
            return taskDto;
        } catch (IndexOutOfBoundsException e) {
            logger.info("Out of bounds");
            throw new InvalidMessageFormatException(taskMessage);
        } catch (NumberFormatException e) {
            logger.info("Can't parse integer");
            throw new InvalidMessageFormatException(taskMessage);
        } catch (Exception e) {
            logger.info("Smth broke" + e.toString());
            throw new RuntimeException();
        }
    }

    @Override
    public String produceToDoList(ToDoListDto toDoListDto) {
        return toDoListDto.toString();
    }
}
