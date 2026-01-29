package tz.go.roadsfund.nrcc.dto.response;

import lombok.Builder;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;
import tz.go.roadsfund.nrcc.enums.ActivityStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Detailed action plan response DTO with targets and activities
 */
@Data
@Builder
public class ActionPlanDetailResponse {
    private Long id;
    private String financialYear;
    private String title;
    private String version;
    private String description;
    private ActionPlanStatus status;
    private BigDecimal totalBudget;

    private Long preparedById;
    private String preparedByName;
    private LocalDate preparedDate;

    private Long approvedById;
    private String approvedByName;
    private LocalDate approvedDate;
    private String approvalResolution;

    private List<TargetResponse> targets;

    private Integer overallProgress;
    private BigDecimal totalActualCost;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class TargetResponse {
        private Long id;
        private String title;
        private LocalDate dueDate;
        private String indicator;
        private String baseline;
        private String targetValue;
        private Integer displayOrder;
        private BigDecimal subtotal;
        private List<ActivityResponse> activities;
        private Integer targetProgress;
    }

    @Data
    @Builder
    public static class ActivityResponse {
        private Long id;
        private String description;
        private Set<String> quarterSchedule;
        private String expectedOutput;
        private String responsibleUnit;
        private ActivityStatus status;
        private Integer progressPercent;
        private LocalDate startDate;
        private LocalDate endDate;
        private String comments;
        private BigDecimal actualCost;
        private Integer displayOrder;
        private List<CostItemResponse> costItems;
        private BigDecimal budgetedCost;
    }

    @Data
    @Builder
    public static class CostItemResponse {
        private Long id;
        private String description;
        private BigDecimal amount;
        private String fundingSource;
    }
}
