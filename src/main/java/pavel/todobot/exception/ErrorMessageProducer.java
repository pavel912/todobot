package pavel.todobot.exception;

import java.util.function.Function;

public class ErrorMessageProducer {
    public static final Function<Long, String> entityIdDoesNotExistExceptionMessageProducer =
            x -> String.format("Entity with id %d does not exist", x);

    public static final Function<String, String> entityUsernameDoesNotExistExceptionMessageProducer =
            x -> String.format("Entity with id %s does not exist", x);

    public static final Function<String, String> InvalidMessageFormatExceptionMessageProducer =
            x -> String.format("Message format of %s is not recognised as a valid format", x);
}
