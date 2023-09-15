package pavel.todobot.bot.commands;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommandEnum {
    START("/start"),
    LOG("/log"),

    PLAN("/plan"),

    SHOW_TODAY("/show_today"),

    SHOW_TOMORROW("/show_tomorrow"),

    SHOW_WEEK("/show_week"),

    SNOOZE("/snooze");

    private final String commandStringRep;

    @Override
    public String toString() {
        return commandStringRep;
    }
}
