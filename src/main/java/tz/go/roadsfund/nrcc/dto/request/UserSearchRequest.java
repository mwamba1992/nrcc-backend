package tz.go.roadsfund.nrcc.dto.request;

import lombok.Data;
import tz.go.roadsfund.nrcc.enums.UserRole;

/**
 * DTO for user search criteria
 */
@Data
public class UserSearchRequest {

    private String name;
    private String email;
    private UserRole role;
    private Long organizationId;
    private Long districtId;
    private Long regionId;
    private String status;
    private String userType;
}
