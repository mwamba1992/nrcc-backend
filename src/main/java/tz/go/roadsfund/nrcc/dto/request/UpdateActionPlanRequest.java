package tz.go.roadsfund.nrcc.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for updating an action plan
 */
@Data
public class UpdateActionPlanRequest {
    private String title;
    private String description;
    private BigDecimal totalBudget;
}
