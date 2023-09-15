package pavel.todobot.exception;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("Method is not implemented");
    }
}
