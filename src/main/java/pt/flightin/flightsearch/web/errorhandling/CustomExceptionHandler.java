package pt.flightin.flightsearch.web.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pt.flightin.flightsearch.core.exception.OutboundInvocationException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorTemplate> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.debug("Handling {}", ex.getClass().getSimpleName());
        return this.createErrorTemplateResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getServletPath());

    }

    private ResponseEntity<ErrorTemplate> createErrorTemplateResponseEntity(HttpStatus statusCode, String message, String path) {
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                                                   .message(message).path(path)
                                                   .build();
        return ResponseEntity.status(statusCode).body(errorTemplate);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorTemplate> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.debug("Handling {}", ex.getClass().getSimpleName());
        return this.createErrorTemplateResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(OutboundInvocationException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity<ErrorTemplate> handleOutboundInvocationException(OutboundInvocationException ex, HttpServletRequest request) {
        log.debug("Handling {}", ex.getClass().getSimpleName());
        return this.createErrorTemplateResponseEntity(HttpStatus.BAD_GATEWAY, ex.getMessage(), request.getServletPath());
    }
}
