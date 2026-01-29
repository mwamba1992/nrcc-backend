package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating user's own profile
 */
@Data
public class UpdateProfileRequest {

    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    private String phoneNumber;
}
