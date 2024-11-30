package se.web.comeon.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserHandleException extends RuntimeException {
    private final String error;
    private final HttpStatus statusCode;
}
