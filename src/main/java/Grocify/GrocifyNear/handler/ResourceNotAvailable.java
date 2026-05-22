package Grocify.GrocifyNear.handler;

public class ResourceNotAvailable extends RuntimeException {
    public ResourceNotAvailable(String message) {
        super(message);
    }
    public ResourceNotAvailable(String message, Throwable cause) {
        super(message, cause);
    }
}
