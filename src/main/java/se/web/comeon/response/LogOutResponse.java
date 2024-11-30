package se.web.comeon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LogOutResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    private Long dailyTimeLimitSeconds; // in seconds
}
