package pavel.todobot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pavel.todobot.bot.commands.Command;
import pavel.todobot.bot.commands.CommandEnum;
import pavel.todobot.bot.commands.LogCommand;
import pavel.todobot.bot.commands.PlanCommand;
import pavel.todobot.bot.commands.ShowTodayCommand;
import pavel.todobot.bot.commands.ShowTomorrowCommand;
import pavel.todobot.bot.commands.ShowWeekCommand;
import pavel.todobot.bot.commands.SnoozeCommand;
import pavel.todobot.bot.commands.StartCommand;
import pavel.todobot.config.BotConfig;
import pavel.todobot.domain.Reminder;
import pavel.todobot.domain.UserSession;
import pavel.todobot.dto.ReminderDto;
import pavel.todobot.dto.UserDto;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.exception.InvalidMessageFormatException;
import pavel.todobot.messagehandler.MessageHandler;
import pavel.todobot.messagehandler.SimpleToDoListHandler;
import pavel.todobot.reminder.comands.LogReminderCommand;
import pavel.todobot.reminder.comands.ReminderCommand;
import pavel.todobot.reminder.comands.ReminderCommandEnum;
import pavel.todobot.reminder.comands.ShowTasksReminderCommand;
import pavel.todobot.service.ReminderService;
import pavel.todobot.service.UserService;
import pavel.todobot.service.UserSessionService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private final BotConfig botConfig;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserSessionService userSessionService;

    @Autowired
    private final ApplicationContext applicationContext;

    @Autowired
    private final ReminderService reminderService;

    private final Map<CommandEnum, Command> commandEnumCommandMap;

    private final Map<String, CommandEnum> stringCommandEnumMap = Map.of(
            CommandEnum.START.toString(), CommandEnum.START,
            CommandEnum.LOG.toString(), CommandEnum.LOG,
            CommandEnum.PLAN.toString(), CommandEnum.PLAN,
            CommandEnum.SHOW_TODAY.toString(), CommandEnum.SHOW_TODAY,
            CommandEnum.SHOW_TOMORROW.toString(), CommandEnum.SHOW_TOMORROW,
            CommandEnum.SHOW_WEEK.toString(), CommandEnum.SHOW_WEEK,
            CommandEnum.SNOOZE.toString(), CommandEnum.SNOOZE
    );

    private final MessageHandler messageHandler = new SimpleToDoListHandler();

    private final int timeZoneOffset = 3;

    private final Map<ReminderCommandEnum, ReminderCommand> reminderCommandEnumReminderCommandMap;

    public TelegramBot(@Value("${bot.token}") String botToken,
                       BotConfig botConfig,
                       UserService userService,
                       UserSessionService userSessionService,
                       ReminderService reminderService,
                       ApplicationContext applicationContext) {
        super(botToken);
        this.botConfig = botConfig;
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.reminderService = reminderService;
        this.applicationContext = applicationContext;

        this.commandEnumCommandMap = Map.of(
                CommandEnum.START, applicationContext.getBean(StartCommand.class),
                CommandEnum.LOG, applicationContext.getBean(LogCommand.class),
                CommandEnum.PLAN, applicationContext.getBean(PlanCommand.class),
                CommandEnum.SHOW_TODAY, applicationContext.getBean(ShowTodayCommand.class),
                CommandEnum.SHOW_TOMORROW, applicationContext.getBean(ShowTomorrowCommand.class),
                CommandEnum.SHOW_WEEK, applicationContext.getBean(ShowWeekCommand.class),
                CommandEnum.SNOOZE, applicationContext.getBean(SnoozeCommand.class)
        );

        this.reminderCommandEnumReminderCommandMap = Map.of(
                ReminderCommandEnum.LOG, applicationContext.getBean(LogReminderCommand.class),
                ReminderCommandEnum.SHOW_TASKS, applicationContext.getBean(ShowTasksReminderCommand.class)
        );
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();

        pavel.todobot.domain.User user = getUser(update);
        UserSession userSession = getSession(user);

        if (!(update.hasMessage() && update.getMessage().hasText())) {
            sendMessage(chatId, SystemMessagesEnum.EMPTY_MESSAGE.toString());
            return;
        }

        String receivedMessage = update.getMessage().getText();

        if (!userSession.isWaitCommand()) {
            String response;
            Command command = getLastCommand(userSession);
            try {
                response = command.execute(user, receivedMessage, messageHandler);
            } catch (InvalidMessageFormatException e) {
                response = SystemMessagesEnum.INVALID_INPUT_FORMAT.toString();
            } catch (Exception e) {
                response = SystemMessagesEnum.SMTH_BROKE.toString();
            }

            sendMessage(chatId, response);
            userSessionService.makeWaitCommand(userSession);
            return;
        }

        if (!stringCommandEnumMap.containsKey(receivedMessage)) {
            sendMessage(chatId, SystemMessagesEnum.INVALID_COMMAND.toString());
            return;
        }

        CommandEnum commandEnum = stringCommandEnumMap
                        .get(receivedMessage);

        Command command = commandEnumCommandMap.get(commandEnum);

        if (!command.requireParams()) {
            sendMessage(chatId, command.execute(user, null, messageHandler));
            return;
        }

        sendMessage(chatId, command.getRequiredParamsMessage());
        userSessionService.makeWaitInput(userSession, commandEnum);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private pavel.todobot.domain.User getUser(Update update) throws RuntimeException {
        User user = update.getMessage().getFrom();
        if (user == null) {
            throw new RuntimeException();
        }

        Long chatId = update.getMessage().getChatId();

        String username = user.getUserName();
        String name = user.getFirstName();
        pavel.todobot.domain.User currentUser;
        try {
            currentUser = userService.getUserByUsername(username);
            if (!currentUser.getMostRecentChatId().equals(chatId)) {
                currentUser.setMostRecentChatId(chatId);
                currentUser = userService.update(currentUser);
            }
        } catch (EntityDoesNotExistException e) {
            UserDto userDto = new UserDto();
            userDto.setTelegramUserName(username);
            userDto.setTelegramName(name);
            userDto.setMostRecentChatId(chatId);
            currentUser = userService.create(userDto);

            createReminders(currentUser);
        }

        return currentUser;
    }

    private UserSession getSession(pavel.todobot.domain.User user) {
        UserSession userSession = userSessionService.getLastUsersActive(user);
        if (userSession == null) {
            return userSessionService.create(user);
        }

        Instant expiredAt = userSession
                .getLastUpdatedAt()
                .plus((long) userSession.getSessionExpirationTimeInMin(), ChronoUnit.MINUTES);

        if (Instant.now().isAfter(expiredAt)) {
            userSessionService.makeInactive(userSession);
            return getSession(user);
        }

        return userSession;
    }

    private void createReminder(pavel.todobot.domain.User user, ReminderCommandEnum reminderCommandEnum, Instant remindAt) {
        ReminderDto logReminderDto = new ReminderDto();
        logReminderDto.setUser(user);
        logReminderDto.setReminderCommand(reminderCommandEnum);
        logReminderDto.setRemindAt(remindAt);
        reminderService.create(logReminderDto);
    }

    private void createReminders(pavel.todobot.domain.User user) {
        Instant defaultLogReminderTime = LocalDate
                .now()
                .atStartOfDay()
                .toInstant(ZoneOffset.ofHours(timeZoneOffset))
                .plusSeconds((24 + 18) * 3600);

        Instant defaultShowTaskReminderTime = LocalDate
                .now()
                .atStartOfDay()
                .toInstant(ZoneOffset.ofHours(timeZoneOffset))
                .plusSeconds((24 + 10) * 3600);

        createReminder(user, ReminderCommandEnum.LOG, defaultLogReminderTime);
        createReminder(user, ReminderCommandEnum.SHOW_TASKS, defaultShowTaskReminderTime);
    }

    private Command getLastCommand(UserSession userSession) {
        return commandEnumCommandMap.get(userSession.getLastCommand());
    }

    //todo: create commands
    @Scheduled(fixedDelay = 900000)
    private void checkReminder() {
        for (pavel.todobot.domain.User user : userService.getAll()) {
            for (Reminder reminder : user.getReminders()) {
                if (reminder
                        .getRemindAt()
                        .isAfter(
                                LocalDateTime
                                        .now()
                                        .atOffset(ZoneOffset.UTC)
                                        .plusHours(timeZoneOffset)
                                        .toInstant())) {
                    continue;
                }

                ReminderCommand reminderCommand = reminderCommandEnumReminderCommandMap
                        .get(reminder.getReminderCommand());

                sendMessage(user.getMostRecentChatId(), reminderCommand.execute(user, messageHandler));
                long reminderOffset = 3600 * 24;
                int weekDay = LocalDate.now().getDayOfWeek().getValue();
                if (weekDay >= 5) {
                    reminderOffset += reminderOffset * (7 - weekDay);
                }

                reminderService.updateTime(reminder.getId(), reminder.getRemindAt().plusSeconds(reminderOffset));
            }
        }
    }
}
