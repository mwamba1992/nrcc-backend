package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for submitting an appeal
 */
@Data
public class SubmitAppealRequest {

    @NotBlank(message = "Grounds for appeal are required")
    private String grounds;
}
