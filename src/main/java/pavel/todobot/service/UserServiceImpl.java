package pavel.todobot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pavel.todobot.domain.User;
import pavel.todobot.dto.UserDto;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setTelegramUserName(userDto.getTelegramUserName());
        user.setTelegramName(userDto.getTelegramName());
        user.setMostRecentChatId(userDto.getMostRecentChatId());

        return user;
    }

    @Override
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setTelegramUserName(user.getTelegramUserName());
        userDto.setTelegramName(user.getTelegramName());
        userDto.setMostRecentChatId(user.getMostRecentChatId());

        return userDto;
    }

    @Override
    public List<User> getAll() {
        return IterableUtils.toList(userRepository.findAll());
    }

    @Override
    public User getUserByUsername(String username) throws EntityDoesNotExistException {
        if (username == null) {
            throw new EntityDoesNotExistException(username);
        }

        User user = userRepository.findByTelegramUserName(username);

        if (user == null) {
            throw new EntityDoesNotExistException(username);
        }

        return user;
    }

    @Override
    public User create(UserDto userDto) {
        User user = fromDto(userDto);

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
