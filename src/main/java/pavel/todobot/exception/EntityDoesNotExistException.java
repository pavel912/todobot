package pavel.todobot.exception;

public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(Long id) {
        super(ErrorMessageProducer.entityIdDoesNotExistExceptionMessageProducer.apply(id));
    }

    public EntityDoesNotExistException(String username) {
        super(ErrorMessageProducer.entityUsernameDoesNotExistExceptionMessageProducer.apply(username));
    }
}
