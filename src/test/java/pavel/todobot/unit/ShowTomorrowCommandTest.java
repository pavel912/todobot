package pavel.todobot.unit;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import pavel.todobot.bot.commands.Command;
import pavel.todobot.bot.commands.PlanCommand;
import pavel.todobot.bot.commands.ShowTomorrowCommand;
import pavel.todobot.domain.User;
import pavel.todobot.dto.UserDto;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.messagehandler.SimpleToDoListHandler;
import pavel.todobot.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ShowTomorrowCommandTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserService userService;

    private Command createCommand;

    private Command showCommand;

    private User user;

    private String message;

    private MessageHandler messageHandler;

    @BeforeEach
    void beforeEach() {
        createCommand = applicationContext.getBean(PlanCommand.class);
        showCommand = applicationContext.getBean(ShowTomorrowCommand.class);
        UserDto userDto = new UserDto();
        userDto.setTelegramName("Name");
        userDto.setTelegramUserName("@Name");
        userDto.setMostRecentChatId(1L);
        user = userService.create(userDto);
        message = "- fdsfsddf - 5\n- fsdfsdfsf - 10";
        messageHandler = new SimpleToDoListHandler();
    }

    @Test
    void testShow() {
        createCommand.execute(user, message, messageHandler);
        String response = showCommand.execute(user, messageHandler);
        assertThat(response).isEqualTo(message + "\n");
    }

    @Test
    void testShowEmpty() {
        String response = showCommand.execute(user, messageHandler);
        assertThat(response).isEqualTo("На завтра еще не было запланировано ни одной задачи");
    }
}
