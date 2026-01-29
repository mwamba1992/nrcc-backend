package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DTO for creating a new action plan
 */
@Data
public class CreateActionPlanRequest {

    @NotBlank(message = "Financial year is required")
    private String financialYear;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private BigDecimal totalBudget;

    @Valid
    private List<TargetRequest> targets = new ArrayList<>();

    @Data
    public static class TargetRequest {
        @NotBlank(message = "Target title is required")
        private String title;

        private LocalDate dueDate;
        private String indicator;
        private String baseline;
        private String targetValue;
        private Integer displayOrder;
        private BigDecimal subtotal;

        @Valid
        private List<ActivityRequest> activities = new ArrayList<>();
    }

    @Data
    public static class ActivityRequest {
        @NotBlank(message = "Activity description is required")
        private String description;

        private Set<String> quarterSchedule; // Q1, Q2, Q3, Q4
        private String expectedOutput;
        private String responsibleUnit;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer displayOrder;

        @Valid
        private List<CostItemRequest> costItems = new ArrayList<>();
    }

    @Data
    public static class CostItemRequest {
        @NotBlank(message = "Cost item description is required")
        private String description;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;

        private String fundingSource;
    }
}
