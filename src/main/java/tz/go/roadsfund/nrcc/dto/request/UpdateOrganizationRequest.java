package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating organization information
 */
@Data
public class UpdateOrganizationRequest {

    @Size(max = 100, message = "Code must not exceed 100 characters")
    private String code;

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 100, message = "Organization type must not exceed 100 characters")
    private String organizationType;

    private String description;

    @Size(max = 255, message = "Contact person must not exceed 255 characters")
    private String contactPerson;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Size(max = 50, message = "Phone number must not exceed 50 characters")
    private String phoneNumber;

    private String address;

    private Long districtId;

    private String status;
}
