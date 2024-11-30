package se.web.comeon.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.web.comeon.response.ResponseData;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({UserHandleException.class})
    public ResponseEntity<Object> handleRegistrationException(final UserHandleException exception) {
        log.error("e: ", exception);
        return ResponseEntity.status(exception.getStatusCode()).body(ResponseData.builder().errors(List.of(exception.getError())).build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleGeneralException(final MethodArgumentNotValidException exception) {
        log.error("e: ", exception);
        var errors = exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.status(exception.getStatusCode()).body(ResponseData.builder().errors(errors).build());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleMethodNotSupportException(final HttpRequestMethodNotSupportedException exception) {
        log.error("e: ", exception);
        return ResponseEntity.status(exception.getStatusCode()).body(ResponseData.builder().errors(List.of(exception.getMessage())).build());
    }
}
