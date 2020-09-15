package exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, Long id) {
        super(type + " type enity not found with id: " + id);
    }
}
