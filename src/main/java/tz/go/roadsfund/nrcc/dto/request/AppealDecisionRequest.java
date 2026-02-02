package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.DecisionType;

@Data
@Schema(description = "Request to record appeal decision")
public class AppealDecisionRequest {

    @NotNull(message = "Decision is required")
    @Schema(description = "Appeal decision (APPROVE to grant appeal, DISAPPROVE to reject)",
            example = "APPROVE", allowableValues = {"APPROVE", "DISAPPROVE"})
    private DecisionType decision;

    @NotBlank(message = "Reason is required")
    @Schema(description = "Reason for the appeal decision",
            example = "After reviewing additional evidence, the appeal is granted and the road will be reclassified as Regional.")
    private String reason;

    @Schema(description = "Additional comments", example = "Applicant provided valid documentation supporting the appeal.")
    private String comments;
}
