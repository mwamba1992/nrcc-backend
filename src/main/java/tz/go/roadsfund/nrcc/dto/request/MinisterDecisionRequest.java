package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.DecisionType;
import tz.go.roadsfund.nrcc.enums.DisapprovalType;

/**
 * DTO for Minister's final decision
 */
@Data
public class MinisterDecisionRequest {

    @NotNull(message = "Decision is required")
    private DecisionType decision;

    // Required only if decision is DISAPPROVE
    private DisapprovalType disapprovalType;

    private String reason;
}
