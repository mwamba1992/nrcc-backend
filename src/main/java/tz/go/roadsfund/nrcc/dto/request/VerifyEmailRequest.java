package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for email verification request
 */
@Data
public class VerifyEmailRequest {

    @NotBlank(message = "Verification token is required")
    private String token;
}
