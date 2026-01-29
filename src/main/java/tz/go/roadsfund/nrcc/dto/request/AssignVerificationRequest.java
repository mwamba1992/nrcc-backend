package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for assigning verification task to NRCC member
 */
@Data
public class AssignVerificationRequest {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private LocalDate visitDate;

    private String instructions;
}
