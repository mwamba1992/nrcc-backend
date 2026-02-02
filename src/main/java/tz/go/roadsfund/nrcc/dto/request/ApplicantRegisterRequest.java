package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.ApplicantType;

@Data
@Schema(description = "Applicant self-registration request")
public class ApplicantRegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    @Schema(description = "Full name of the applicant", example = "John Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+255|0)[0-9]{9}$", message = "Phone number must be valid Tanzanian number")
    @Schema(description = "Phone number (Tanzanian format)", example = "+255755123456")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "Password (min 8 characters)", example = "SecurePass@123")
    private String password;

    @Schema(description = "Applicant type", example = "INDIVIDUAL",
            allowableValues = {"INDIVIDUAL", "MEMBER_OF_PARLIAMENT", "REGIONAL_ROADS_BOARD", "GROUP"})
    private ApplicantType applicantType;

    @Schema(description = "National ID number (optional)", example = "19900101-12345-00001-01")
    private String nationalId;

    @Schema(description = "Physical address", example = "123 Main Street, Dar es Salaam")
    private String address;
}
