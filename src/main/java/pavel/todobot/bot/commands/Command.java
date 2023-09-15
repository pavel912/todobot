package pavel.todobot.bot.commands;

import pavel.todobot.domain.User;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.exception.InvalidMessageFormatException;
import pavel.todobot.messagehandler.MessageHandler;

public interface Command {
    String execute(User currentUser, String message, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException;

    String execute(User currentUser, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException;

    String execute(User currentUser) throws InvalidMessageFormatException, EntityDoesNotExistException;

    boolean requireParams();

    String getRequiredParamsMessage();
}
