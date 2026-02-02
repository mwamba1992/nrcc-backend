package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request OTP for login")
public class OtpRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+255|0)[0-9]{9}$", message = "Phone number must be valid Tanzanian number")
    @Schema(description = "Phone number to send OTP", example = "0755123456")
    private String phoneNumber;
}
