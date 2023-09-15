package pavel.todobot.bot;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SystemMessagesEnum {
    EMPTY_MESSAGE("Сообщение не может быть пустым"),
    INVALID_COMMAND("Команда не распознана. Для отображения доступных команд введите /start"),
    INVALID_INPUT_FORMAT("Неверный формат сообщения. Для выбора формата введите /choose_format"),

    SMTH_BROKE("Что-то сломалось( Мы уже присутпили к починке");

    private final String stringRep;

    @Override
    public String toString() {
        return stringRep;
    }
}
