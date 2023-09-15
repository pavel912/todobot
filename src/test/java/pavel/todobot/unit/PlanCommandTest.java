package pavel.todobot.unit;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import pavel.todobot.bot.commands.Command;
import pavel.todobot.bot.commands.PlanCommand;
import pavel.todobot.domain.User;
import pavel.todobot.dto.UserDto;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.messagehandler.SimpleToDoListHandler;
import pavel.todobot.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PlanCommandTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserService userService;

    private Command command;

    private User user;

    private String message;

    private MessageHandler messageHandler;

    @BeforeEach
    void beforeEach() {
        command = applicationContext.getBean(PlanCommand.class);
        UserDto userDto = new UserDto();
        userDto.setTelegramName("Name");
        userDto.setTelegramUserName("@Name");
        userDto.setMostRecentChatId(1L);
        user = userService.create(userDto);
        message = "- fdsfsddf - 5\n- fsdfsdfsf - 10";
        messageHandler = new SimpleToDoListHandler();
    }

    @Test
    void testCreate() {
        String response = command.execute(user, message, messageHandler);
        assertThat(response).isEqualTo("Новый список на завтра успешно создан");
    }

    @Test
    void testUpdate() {
        String createResponse = command.execute(user, message, messageHandler);
        assertThat(createResponse).isEqualTo("Новый список на завтра успешно создан");

        String updateResponse = command.execute(user, message, messageHandler);
        assertThat(updateResponse).isEqualTo("Завтрашний список успешно обновлен");
    }
}
