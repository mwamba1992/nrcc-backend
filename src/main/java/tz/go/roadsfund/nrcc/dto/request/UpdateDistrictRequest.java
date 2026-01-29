package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating district information
 */
@Data
public class UpdateDistrictRequest {

    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private Long regionId;

    private String description;

    private String status;
}
