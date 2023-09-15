package pavel.todobot.service;

import pavel.todobot.bot.commands.CommandEnum;
import pavel.todobot.domain.User;
import pavel.todobot.domain.UserSession;

import java.time.Instant;

public interface UserSessionService {
    UserSession create(User user);

    void makeWaitCommand(UserSession userSession);

    void makeWaitInput(UserSession userSession, CommandEnum lastCommand);

    void makeInactive(UserSession userSession);

    UserSession getLastUsersActive(User user);
}
