package tz.go.roadsfund.nrcc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * District response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponse {
    private Long id;
    private String code;
    private String name;
    private Long regionId;
    private String regionName;
    private String description;
    private String status;
}
