package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.ActivityStatus;

import java.math.BigDecimal;

/**
 * DTO for updating activity progress
 */
@Data
public class UpdateActivityProgressRequest {

    private ActivityStatus status;

    @Min(value = 0, message = "Progress cannot be less than 0")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private Integer progressPercent;

    private BigDecimal actualCost;

    private String comments;
}
