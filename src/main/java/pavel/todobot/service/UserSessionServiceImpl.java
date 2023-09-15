package pavel.todobot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pavel.todobot.bot.commands.CommandEnum;
import pavel.todobot.domain.User;
import pavel.todobot.domain.UserSession;
import pavel.todobot.repository.UserSessionRepository;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {
    @Autowired
    private final UserSessionRepository userSessionRepository;

    @Override
    public UserSession create(User user) {
        UserSession userSession = new UserSession();
        userSession.setUser(user);

        return userSessionRepository.save(userSession);
    }

    @Override
    public void makeWaitCommand(UserSession userSession) {
        userSession.setWaitCommand(true);
        userSession.setLastCommand(null);

        userSessionRepository.save(userSession);
    }

    @Override
    public void makeWaitInput(UserSession userSession, CommandEnum lastCommand) {
        userSession.setWaitCommand(false);
        userSession.setLastCommand(lastCommand);

        userSessionRepository.save(userSession);
    }

    @Override
    public void makeInactive(UserSession userSession) {
        userSession.setActive(false);

        userSessionRepository.save(userSession);
    }

    @Override
    public UserSession getLastUsersActive(User user) {
        List<UserSession> userSessions = userSessionRepository
                .findByUserIdAndIsActiveOrderByCreatedAt(user.getId(), true);

        if (userSessions.isEmpty()) {
            return null;
        }

        return userSessions.get(userSessions.size() - 1);
    }
}
