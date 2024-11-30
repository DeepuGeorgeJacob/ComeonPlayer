package se.web.comeon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ResponseData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<String> errors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String info;
}
