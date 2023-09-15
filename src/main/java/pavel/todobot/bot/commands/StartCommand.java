package pavel.todobot.bot.commands;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pavel.todobot.domain.User;
import pavel.todobot.exception.EntityDoesNotExistException;
import pavel.todobot.exception.InvalidMessageFormatException;
import pavel.todobot.messagehandler.MessageHandler;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@AllArgsConstructor
public class StartCommand implements Command {
    @Override
    public String execute(User currentUser, String message, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        return execute(currentUser);
    }

    @Override
    public String execute(User currentUser, MessageHandler messageHandler) throws InvalidMessageFormatException, EntityDoesNotExistException {
        return execute(currentUser);
    }

    @Override
    public String execute(User currentUser) {
        return String.format("Привет, %s! Я бот для создания списков задач и логгирования времени. Доступные команды вы можете посмотреть в меню бота.\nПример формата ввода:\n- Задача1 - 5 (на задачу было потрачено/запланирвоано 5 часов)\n- Задача2\nНекоторые особенности боты:\n1. Команда log перезаписывает текущий список\n2. Команда plan добавляет новые задачи в существующий список", currentUser.getTelegramName());
    }

    @Override
    public boolean requireParams() {
        return false;
    }

    @Override
    public String getRequiredParamsMessage() {
        return "Нет требуемых параметров";
    }
}
