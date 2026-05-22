package Grocify.GrocifyNear.handler;

public class Failed extends RuntimeException {
    public Failed(String message) {
        super(message);
    }
    public Failed(String message, Throwable cause) {
        super(message, cause);
    }
}
