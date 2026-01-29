package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.ApplicantType;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for creating a new road reclassification application
 */
@Data
public class CreateApplicationRequest {

    @NotNull(message = "Applicant type is required")
    private ApplicantType applicantType;

    @NotNull(message = "Proposed class is required")
    private RoadClass proposedClass;

    // Fourth Schedule Form Data
    @NotBlank(message = "Road name is required")
    private String roadName;

    @NotNull(message = "Road length is required")
    private BigDecimal roadLength;

    @NotNull(message = "Current class is required")
    private RoadClass currentClass;

    @NotBlank(message = "Starting point is required")
    private String startingPoint;

    @NotBlank(message = "Terminal point is required")
    private String terminalPoint;

    @NotBlank(message = "Reasons for reclassification is required")
    private String reclassificationReasons;

    @NotBlank(message = "Surface type of carriageway is required")
    private String surfaceTypeCarriageway;

    private String surfaceTypeShoulders;

    @NotNull(message = "Carriageway width is required")
    private BigDecimal carriagewayWidth;

    @NotNull(message = "Formation width is required")
    private BigDecimal formationWidth;

    @NotNull(message = "Actual road reserve width is required")
    private BigDecimal actualRoadReserveWidth;

    @NotBlank(message = "Traffic level is required")
    private String trafficLevel;

    @NotBlank(message = "Traffic composition is required")
    private String trafficComposition;

    @NotBlank(message = "Towns and villages linked is required")
    private String townsVillagesLinked;

    private String principalNodes;

    @NotBlank(message = "Bus routes is required")
    private String busRoutes;

    @NotBlank(message = "Public services is required")
    private String publicServices;

    private String alternativeRoutes;

    // Eligibility criteria codes (R1-R7 or T1-T5)
    @NotNull(message = "At least one eligibility criterion must be selected")
    private List<EligibilityCriterionRequest> eligibilityCriteria;

    @Data
    public static class EligibilityCriterionRequest {
        @NotBlank(message = "Criterion code is required")
        private String criterionCode; // R1, R2, etc.

        private String details; // Supporting details

        private String evidenceDescription; // Description of evidence
    }
}
