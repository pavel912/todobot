package pavel.todobot.unit;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import pavel.todobot.bot.commands.Command;
import pavel.todobot.bot.commands.StartCommand;
import lombok.AllArgsConstructor;
import pavel.todobot.domain.User;
import pavel.todobot.dto.UserDto;
import pavel.todobot.service.UserService;

@SpringBootTest
@Transactional
public class StartCommandTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserService userService;

    private Command command;

    private User user;

    @BeforeEach
    void beforeEach() {
        command = applicationContext.getBean(StartCommand.class);
        UserDto userDto = new UserDto();
        userDto.setTelegramName("Name");
        userDto.setTelegramUserName("@Name");
        userDto.setMostRecentChatId(1L);
        user = userService.create(userDto);
    }

    @Test
    void test() {
        command.execute(user);
    }
}
