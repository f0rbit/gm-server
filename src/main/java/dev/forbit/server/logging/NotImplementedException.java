package dev.forbit.server.logging;

public class NotImplementedException extends Exception {

    public NotImplementedException() {
        super("Not Implemented Error");
    }
    public NotImplementedException(String message) {
        super(message);
    }
}
