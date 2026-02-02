package tz.go.roadsfund.nrcc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.DashboardStatsResponse;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;
import tz.go.roadsfund.nrcc.enums.ApplicationStatus;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.repository.*;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.util.SecurityUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RoadRepository roadRepository;
    private final ActionPlanRepository actionPlanRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final OrganizationRepository organizationRepository;

    @GetMapping("/stats")
    @RequirePermission(Permission.REPORT_VIEW)
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {

        DashboardStatsResponse stats = DashboardStatsResponse.builder()
                .totalApplications(applicationRepository.count())
                .draftApplications(applicationRepository.countByStatus(ApplicationStatus.DRAFT))
                .submittedApplications(countSubmittedApplications())
                .approvedApplications(applicationRepository.countByStatus(ApplicationStatus.GAZETTED))
                .rejectedApplications(countRejectedApplications())
                .pendingApplications(countPendingApplications())
                .totalUsers(userRepository.count())
                .totalRoads(roadRepository.count())
                .totalRegions(regionRepository.count())
                .totalDistricts(districtRepository.count())
                .totalOrganizations(organizationRepository.count())
                .totalActionPlans(actionPlanRepository.count())
                .activeActionPlans(actionPlanRepository.countByStatus(ActionPlanStatus.IN_PROGRESS))
                .build();

        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved", stats));
    }

    @GetMapping("/stats/applications")
    @RequirePermission(Permission.REPORT_VIEW)
    public ResponseEntity<ApiResponse<Map<String, Long>>> getApplicationStats() {
        Map<String, Long> stats = new HashMap<>();

        for (ApplicationStatus status : ApplicationStatus.values()) {
            stats.put(status.name(), applicationRepository.countByStatus(status));
        }

        return ResponseEntity.ok(ApiResponse.success("Application stats retrieved", stats));
    }

    @GetMapping("/stats/my")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyStats() {
        Long userId = SecurityUtil.getCurrentUserId();

        Map<String, Object> stats = new HashMap<>();
        stats.put("myApplications", applicationRepository.countByApplicantId(userId));
        stats.put("myDraftApplications", applicationRepository.countByApplicantIdAndStatus(userId, ApplicationStatus.DRAFT));
        stats.put("mySubmittedApplications", applicationRepository.countByApplicantIdAndStatus(userId, ApplicationStatus.SUBMITTED));
        stats.put("myApprovedApplications", applicationRepository.countByApplicantIdAndStatus(userId, ApplicationStatus.GAZETTED));

        return ResponseEntity.ok(ApiResponse.success("My stats retrieved", stats));
    }

    @GetMapping("/stats/action-plans")
    @RequirePermission(Permission.ACTION_PLAN_READ)
    public ResponseEntity<ApiResponse<Map<String, Long>>> getActionPlanStats() {
        Map<String, Long> stats = new HashMap<>();

        for (ActionPlanStatus status : ActionPlanStatus.values()) {
            stats.put(status.name(), actionPlanRepository.countByStatus(status));
        }

        return ResponseEntity.ok(ApiResponse.success("Action plan stats retrieved", stats));
    }

    private long countSubmittedApplications() {
        return applicationRepository.countByStatus(ApplicationStatus.SUBMITTED) +
               applicationRepository.countByStatus(ApplicationStatus.UNDER_MINISTER_REVIEW) +
               applicationRepository.countByStatus(ApplicationStatus.UNDER_RAS_REVIEW) +
               applicationRepository.countByStatus(ApplicationStatus.UNDER_RC_REVIEW);
    }

    private long countRejectedApplications() {
        return applicationRepository.countByStatus(ApplicationStatus.DISAPPROVED_REFUSED) +
               applicationRepository.countByStatus(ApplicationStatus.DISAPPROVED_DESIGNATED);
    }

    private long countPendingApplications() {
        return applicationRepository.countByStatus(ApplicationStatus.WITH_NRCC_CHAIR) +
               applicationRepository.countByStatus(ApplicationStatus.VERIFICATION_IN_PROGRESS) +
               applicationRepository.countByStatus(ApplicationStatus.NRCC_REVIEW_MEETING) +
               applicationRepository.countByStatus(ApplicationStatus.RECOMMENDATION_SUBMITTED) +
               applicationRepository.countByStatus(ApplicationStatus.PENDING_GAZETTEMENT);
    }
}
