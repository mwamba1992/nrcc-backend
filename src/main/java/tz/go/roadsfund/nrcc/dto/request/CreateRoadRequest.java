package tz.go.roadsfund.nrcc.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.math.BigDecimal;

/**
 * DTO for creating a new road
 */
@Data
public class CreateRoadRequest {

    @NotBlank(message = "Road name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 50, message = "Road number must not exceed 50 characters")
    private String roadNumber;

    @DecimalMin(value = "0.0", inclusive = false, message = "Length must be greater than 0")
    private BigDecimal length;

    @NotNull(message = "Road class is required")
    private RoadClass currentClass;

    @Size(max = 500, message = "Start point must not exceed 500 characters")
    private String startPoint;

    @Size(max = 500, message = "End point must not exceed 500 characters")
    private String endPoint;

    @Size(max = 100, message = "Region must not exceed 100 characters")
    private String region;

    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;

    @Size(max = 100, message = "Surface type must not exceed 100 characters")
    private String surfaceType;

    @DecimalMin(value = "0.0", inclusive = false, message = "Carriageway width must be greater than 0")
    private BigDecimal carriagewayWidth;

    @DecimalMin(value = "0.0", inclusive = false, message = "Formation width must be greater than 0")
    private BigDecimal formationWidth;

    @DecimalMin(value = "0.0", inclusive = false, message = "Road reserve width must be greater than 0")
    private BigDecimal roadReserveWidth;

    private String description;
}
