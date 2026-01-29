package tz.go.roadsfund.nrcc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Road response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadResponse {
    private Long id;
    private String name;
    private String roadNumber;
    private BigDecimal length;
    private String currentClass;
    private String startPoint;
    private String endPoint;
    private String region;
    private String district;
    private String surfaceType;
    private BigDecimal carriagewayWidth;
    private BigDecimal formationWidth;
    private BigDecimal roadReserveWidth;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
