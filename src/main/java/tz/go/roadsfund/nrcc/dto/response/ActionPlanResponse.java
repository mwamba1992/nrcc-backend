package tz.go.roadsfund.nrcc.dto.response;

import lombok.Builder;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Basic action plan response DTO for list views
 */
@Data
@Builder
public class ActionPlanResponse {
    private Long id;
    private String financialYear;
    private String title;
    private String version;
    private ActionPlanStatus status;
    private BigDecimal totalBudget;
    private Long preparedById;
    private String preparedByName;
    private LocalDate preparedDate;
    private Long approvedById;
    private String approvedByName;
    private LocalDate approvedDate;
    private int targetCount;
    private int activityCount;
    private int completedActivityCount;
    private Integer overallProgress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
