package pt.flightin.flightsearch.core.exception;

public class ResourceNotFoundException extends BaseException {

    static final String MSG = "%s not found";

    public ResourceNotFoundException(String message) {
        super(String.format(MSG, message));
    }
}
