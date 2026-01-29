package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for workflow actions (approve, return, forward, etc.)
 */
@Data
public class WorkflowActionRequest {

    @NotBlank(message = "Comments are required")
    private String comments;
}
