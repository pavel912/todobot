package pavel.todobot.service;

import pavel.todobot.domain.User;
import pavel.todobot.dto.UserDto;
import pavel.todobot.exception.EntityDoesNotExistException;

import java.util.List;

public interface UserService {

    User fromDto(UserDto userDto);

    UserDto toDto(User user);

    List<User> getAll();

    User getUserByUsername(String username) throws EntityDoesNotExistException;
    User create(UserDto userDto);

    User update(User user);

    void delete(User user);
}
