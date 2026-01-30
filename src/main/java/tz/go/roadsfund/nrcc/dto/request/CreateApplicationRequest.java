package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.ApplicantType;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for creating a new road reclassification application (Fourth Schedule Form)
 */
@Data
@Schema(description = "Request payload for creating a road reclassification application")
public class CreateApplicationRequest {

    @NotNull(message = "Applicant type is required")
    @Schema(description = "Type of applicant",
            example = "INDIVIDUAL",
            allowableValues = {"INDIVIDUAL", "GROUP", "MEMBER_OF_PARLIAMENT", "REGIONAL_ROADS_BOARD"})
    private ApplicantType applicantType;

    @NotNull(message = "Proposed class is required")
    @Schema(description = "Proposed road classification after reclassification",
            example = "TRUNK",
            allowableValues = {"DISTRICT", "REGIONAL", "TRUNK"})
    private RoadClass proposedClass;

    @NotNull(message = "Current class is required")
    @Schema(description = "Current road classification",
            example = "REGIONAL",
            allowableValues = {"DISTRICT", "REGIONAL", "TRUNK"})
    private RoadClass currentClass;

    // Fourth Schedule Form Data
    @NotBlank(message = "Road name is required")
    @Schema(description = "Official name of the road", example = "Morogoro - Dodoma Road")
    private String roadName;

    @NotNull(message = "Road length is required")
    @Schema(description = "Total length of road in kilometers", example = "125.5")
    private BigDecimal roadLength;

    @NotBlank(message = "Starting point is required")
    @Schema(description = "Starting point/location of the road", example = "Morogoro Town Center")
    private String startingPoint;

    @NotBlank(message = "Terminal point is required")
    @Schema(description = "End point/location of the road", example = "Dodoma Regional Boundary")
    private String terminalPoint;

    @NotBlank(message = "Reasons for reclassification is required")
    @Schema(description = "Justification for road reclassification",
            example = "Increased traffic volume, economic importance, connects two regional headquarters")
    private String reclassificationReasons;

    @NotBlank(message = "Surface type of carriageway is required")
    @Schema(description = "Type of road surface",
            example = "Tarmac",
            allowableValues = {"Tarmac", "Gravel", "Earth", "Concrete", "Paved"})
    private String surfaceTypeCarriageway;

    @Schema(description = "Surface type of road shoulders (optional)", example = "Gravel")
    private String surfaceTypeShoulders;

    @NotNull(message = "Carriageway width is required")
    @Schema(description = "Width of the carriageway in meters", example = "7.0")
    private BigDecimal carriagewayWidth;

    @NotNull(message = "Formation width is required")
    @Schema(description = "Total formation width in meters", example = "12.0")
    private BigDecimal formationWidth;

    @NotNull(message = "Actual road reserve width is required")
    @Schema(description = "Actual road reserve width in meters", example = "30.0")
    private BigDecimal actualRoadReserveWidth;

    @NotBlank(message = "Traffic level is required")
    @Schema(description = "Average daily traffic level",
            example = "Medium (500-2000 vpd)",
            allowableValues = {"Low (< 500 vpd)", "Medium (500-2000 vpd)", "High (2000-5000 vpd)", "Very High (> 5000 vpd)"})
    private String trafficLevel;

    @NotBlank(message = "Traffic composition is required")
    @Schema(description = "Types of vehicles using the road",
            example = "Mixed traffic including passenger vehicles, buses, and heavy trucks")
    private String trafficComposition;

    @NotBlank(message = "Towns and villages linked is required")
    @Schema(description = "List of towns and villages connected by the road",
            example = "Morogoro, Kilosa, Gairo, Dodoma")
    private String townsVillagesLinked;

    @Schema(description = "Principal nodes/junctions along the road (optional)",
            example = "Kilosa Junction, Gairo Crossroads")
    private String principalNodes;

    @NotBlank(message = "Bus routes is required")
    @Schema(description = "Bus routes operating on this road",
            example = "Morogoro-Dodoma Express, Local routes 101, 102")
    private String busRoutes;

    @NotBlank(message = "Public services is required")
    @Schema(description = "Public services accessible via this road",
            example = "Regional Hospital, District Courts, Secondary Schools, Markets")
    private String publicServices;

    @Schema(description = "Alternative routes available (optional)",
            example = "Old Morogoro Road via Kilosa")
    private String alternativeRoutes;

    @NotNull(message = "At least one eligibility criterion must be selected")
    @Schema(description = "Eligibility criteria for reclassification. Use R1-R7 for Regional roads, T1-T5 for Trunk roads")
    private List<EligibilityCriterionRequest> eligibilityCriteria;

    @Data
    @Schema(description = "Eligibility criterion with supporting evidence")
    public static class EligibilityCriterionRequest {

        @NotBlank(message = "Criterion code is required")
        @Schema(description = "Criterion code. For Regional: R1-R7, For Trunk: T1-T5",
                example = "R1",
                allowableValues = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "T1", "T2", "T3", "T4", "T5"})
        private String criterionCode;

        @Schema(description = "Details supporting this criterion",
                example = "Road connects Morogoro Regional HQ to Dodoma Regional HQ")
        private String details;

        @Schema(description = "Description of evidence/documentation provided",
                example = "Attached map showing regional boundaries and road alignment")
        private String evidenceDescription;
    }
}
