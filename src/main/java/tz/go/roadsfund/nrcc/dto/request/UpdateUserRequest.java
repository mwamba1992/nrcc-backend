package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.UserRole;

/**
 * DTO for updating user information
 */
@Data
public class UpdateUserRequest {

    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    private UserRole role;

    private Long organizationId;

    private Long districtId;

    private String userType;

    private String status;
}
