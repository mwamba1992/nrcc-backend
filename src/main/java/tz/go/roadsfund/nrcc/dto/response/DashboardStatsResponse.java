package tz.go.roadsfund.nrcc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private Long totalApplications;
    private Long draftApplications;
    private Long submittedApplications;
    private Long approvedApplications;
    private Long rejectedApplications;
    private Long pendingApplications;
    private Long totalUsers;
    private Long totalRoads;
    private Long totalRegions;
    private Long totalDistricts;
    private Long totalOrganizations;
    private Long totalActionPlans;
    private Long activeActionPlans;
}
