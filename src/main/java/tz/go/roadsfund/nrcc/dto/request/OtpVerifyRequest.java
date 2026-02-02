package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Verify OTP and login")
public class OtpVerifyRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+255|0)[0-9]{9}$", message = "Phone number must be valid Tanzanian number")
    @Schema(description = "Phone number", example = "0755123456")
    private String phoneNumber;

    @NotBlank(message = "OTP code is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    @Schema(description = "6-digit OTP code", example = "123456")
    private String otp;
}
