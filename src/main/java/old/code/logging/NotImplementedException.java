package old.code.logging;

/**
 * Error thrown when bodies of packet's haven't been implemented
 * These shouldn't be thrown because the functions that throw them should never be called.
 */
public class NotImplementedException extends Exception {
    public NotImplementedException() {
        super("Not Implemented Error");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
