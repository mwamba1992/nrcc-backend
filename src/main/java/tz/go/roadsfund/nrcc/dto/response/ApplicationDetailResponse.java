package tz.go.roadsfund.nrcc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Detailed application response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailResponse {

    private Long id;
    private String applicationNumber;
    private String applicantType;
    private String applicantTypeDisplayName;

    // Applicant info
    private Long applicantId;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;

    // Status
    private String status;
    private String statusDisplayName;
    private Long currentOwnerId;
    private String currentOwnerName;
    private String currentOwnerRole;

    // Dates
    private LocalDate submissionDate;
    private LocalDate decisionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Form Data (Fourth Schedule)
    private FormDataResponse formData;

    // Eligibility Criteria
    private List<EligibilityCriterionResponse> eligibilityCriteria;

    // Workflow history
    private List<ApprovalActionResponse> approvalHistory;

    // Verification info
    private List<VerificationAssignmentResponse> verificationAssignments;

    // Decision info
    private RecommendationResponse recommendation;
    private MinisterDecisionResponse ministerDecision;
    private GazettementResponse gazettement;
    private AppealResponse appeal;

    private String remarks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormDataResponse {
        private String roadName;
        private BigDecimal roadLength;
        private String currentClass;
        private String proposedClass;
        private String startingPoint;
        private String terminalPoint;
        private String reclassificationReasons;
        private String surfaceTypeCarriageway;
        private String surfaceTypeShoulders;
        private BigDecimal carriagewayWidth;
        private BigDecimal formationWidth;
        private BigDecimal actualRoadReserveWidth;
        private String trafficLevel;
        private String trafficComposition;
        private String townsVillagesLinked;
        private String principalNodes;
        private String busRoutes;
        private String publicServices;
        private String alternativeRoutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EligibilityCriterionResponse {
        private String code;
        private String description;
        private String details;
        private String evidenceDescription;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalActionResponse {
        private Long id;
        private String action;
        private String fromStatus;
        private String toStatus;
        private String actorName;
        private String actorRole;
        private String comments;
        private LocalDateTime actionDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationAssignmentResponse {
        private Long id;
        private String memberName;
        private String assignedByName;
        private LocalDate dueDate;
        private LocalDate visitDate;
        private String status;
        private String instructions;
        private VerificationReportResponse report;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationReportResponse {
        private Long id;
        private String findings;
        private LocalDate visitDate;
        private LocalDateTime submittedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationResponse {
        private Long id;
        private String recommendationText;
        private String submittedByName;
        private LocalDateTime submittedDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MinisterDecisionResponse {
        private Long id;
        private String decision;
        private String disapprovalType;
        private String reason;
        private String decidedByName;
        private LocalDateTime decisionDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GazettementResponse {
        private Long id;
        private String gazetteNumber;
        private LocalDate gazetteDate;
        private String status;
        private String updatedByName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppealResponse {
        private Long id;
        private String grounds;
        private String status;
        private String decision;
        private LocalDateTime appealDate;
        private LocalDateTime decisionDate;
    }
}
