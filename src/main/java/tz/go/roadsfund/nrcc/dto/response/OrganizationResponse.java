package tz.go.roadsfund.nrcc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Organization response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String code;
    private String name;
    private String organizationType;
    private String description;
    private String contactPerson;
    private String email;
    private String phoneNumber;
    private String address;
    private String region;
    private String district;
    private String status;
}
