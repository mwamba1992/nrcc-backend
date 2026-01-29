package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating a new region
 */
@Data
public class CreateRegionRequest {

    @NotBlank(message = "Region code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Region name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private String description;
}
