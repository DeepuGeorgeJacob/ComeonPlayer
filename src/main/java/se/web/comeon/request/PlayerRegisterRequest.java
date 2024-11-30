package se.web.comeon.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerRegisterRequest {

    @NotNull(message = "Name is required.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String name;

    @NotNull(message = "Surname is required.")
    @Size(max = 100, message = "Surname cannot exceed 100 characters.")
    private String surname;

    @NotNull(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private final String email;

    @NotNull(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotNull(message = "Date of Birth is required.")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Date of Birth must be in the format YYYY-MM-DD."
    )
    private String dateOfBirth;

    @NotNull(message = "Address is required.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;

}
