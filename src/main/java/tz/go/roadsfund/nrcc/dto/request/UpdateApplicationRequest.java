package tz.go.roadsfund.nrcc.dto.request;

import lombok.Data;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for updating an existing application (only allowed in DRAFT or RETURNED_FOR_CORRECTION status)
 */
@Data
public class UpdateApplicationRequest {

    private RoadClass proposedClass;

    // Fourth Schedule Form Data
    private String roadName;
    private BigDecimal roadLength;
    private RoadClass currentClass;
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

    // Eligibility criteria
    private List<CreateApplicationRequest.EligibilityCriterionRequest> eligibilityCriteria;
}
