package se.web.comeon.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
public class PlayerUpdateRequest {
    @NotNull(message = "Player Id is required.")
    private Long playerId;

    @NotNull(message = "Activate or Deactivate user")
    private Boolean isActive;

    @Range(message = "Time limit should be greater than or equal to 0.")
    private Integer timeLimitInMinutes;
}
