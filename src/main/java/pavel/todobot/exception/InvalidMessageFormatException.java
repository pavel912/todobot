package pavel.todobot.exception;

public class InvalidMessageFormatException extends RuntimeException {
    public InvalidMessageFormatException(String message) {
        super(ErrorMessageProducer.InvalidMessageFormatExceptionMessageProducer.apply(message));
    }
}
