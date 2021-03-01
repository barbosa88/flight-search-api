package pt.flightin.flightsearch.core.exception;

public abstract class BaseException extends Exception {

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
