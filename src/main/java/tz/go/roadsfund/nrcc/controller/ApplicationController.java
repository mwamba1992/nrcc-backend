package tz.go.roadsfund.nrcc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.request.*;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.ApplicationDetailResponse;
import tz.go.roadsfund.nrcc.dto.response.ApplicationResponse;
import tz.go.roadsfund.nrcc.enums.ApplicationStatus;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.ApplicationService;

import java.util.List;

/**
 * REST Controller for application management and workflow operations
 */
@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new application (FR-002)
     */
    @PostMapping
    @RequirePermission(Permission.APPLICATION_CREATE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> createApplication(
            @Valid @RequestBody CreateApplicationRequest request) {
        ApplicationDetailResponse application = applicationService.createApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application created successfully", application));
    }

    /**
     * Update an existing application (FR-003)
     */
    @PutMapping("/{id}")
    @RequirePermission(Permission.APPLICATION_UPDATE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationRequest request) {
        ApplicationDetailResponse application = applicationService.updateApplication(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application updated successfully", application));
    }

    /**
     * Get application by ID
     */
    @GetMapping("/{id}")
    @RequirePermission(Permission.APPLICATION_READ)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> getApplication(@PathVariable Long id) {
        ApplicationDetailResponse application = applicationService.getApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Application retrieved successfully", application));
    }

    /**
     * Get application by number
     */
    @GetMapping("/number/{applicationNumber}")
    @RequirePermission(Permission.APPLICATION_READ)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> getApplicationByNumber(
            @PathVariable String applicationNumber) {
        ApplicationDetailResponse application = applicationService.getApplicationByNumber(applicationNumber);
        return ResponseEntity.ok(ApiResponse.success("Application retrieved successfully", application));
    }

    /**
     * Get all applications (paginated) (FR-015)
     */
    @GetMapping
    @RequirePermission(Permission.APPLICATION_LIST)
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ApplicationResponse> applications = applicationService.getAllApplications(pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    /**
     * Get applications by status
     */
    @GetMapping("/status/{status}")
    @RequirePermission(Permission.APPLICATION_LIST)
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getApplicationsByStatus(
            @PathVariable ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ApplicationResponse> applications = applicationService.getApplicationsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    /**
     * Get current user's applications
     */
    @GetMapping("/my-applications")
    @RequirePermission(Permission.APPLICATION_READ)
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ApplicationResponse> applications = applicationService.getMyApplications(pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    /**
     * Get applications assigned to current user
     */
    @GetMapping("/my-assignments")
    @RequirePermission(Permission.APPLICATION_READ)
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getMyAssignedApplications() {
        List<ApplicationResponse> applications = applicationService.getMyAssignedApplications();
        return ResponseEntity.ok(ApiResponse.success("Assigned applications retrieved successfully", applications));
    }

    /**
     * Delete application (only DRAFT status)
     */
    @DeleteMapping("/{id}")
    @RequirePermission(Permission.APPLICATION_DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Application deleted successfully", null));
    }

    // ==================== WORKFLOW OPERATIONS ====================

    /**
     * Submit application (FR-004, FR-005, FR-006)
     */
    @PostMapping("/{id}/submit")
    @RequirePermission(Permission.APPLICATION_SUBMIT)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> submitApplication(@PathVariable Long id) {
        ApplicationDetailResponse application = applicationService.submitApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Application submitted successfully", application));
    }

    /**
     * RAS approves application (Workflow B - FR-007)
     */
    @PostMapping("/{id}/ras-approve")
    @RequirePermission(Permission.APPLICATION_APPROVE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> rasApprove(
            @PathVariable Long id,
            @Valid @RequestBody WorkflowActionRequest request) {
        ApplicationDetailResponse application = applicationService.rasApprove(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application approved by RAS", application));
    }

    /**
     * RC approves application (Workflow B - FR-007)
     */
    @PostMapping("/{id}/rc-approve")
    @RequirePermission(Permission.APPLICATION_APPROVE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> rcApprove(
            @PathVariable Long id,
            @Valid @RequestBody WorkflowActionRequest request) {
        ApplicationDetailResponse application = applicationService.rcApprove(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application approved by RC", application));
    }

    /**
     * Return application for correction (FR-007)
     */
    @PostMapping("/{id}/return")
    @RequirePermission(Permission.APPLICATION_RETURN)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> returnForCorrection(
            @PathVariable Long id,
            @Valid @RequestBody WorkflowActionRequest request) {
        ApplicationDetailResponse application = applicationService.returnForCorrection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application returned for correction", application));
    }

    /**
     * Minister forwards to NRCC Chair (FR-008, Workflow C)
     */
    @PostMapping("/{id}/forward-to-nrcc")
    @RequirePermission(Permission.APPLICATION_APPROVE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> forwardToNrccChair(
            @PathVariable Long id,
            @Valid @RequestBody WorkflowActionRequest request) {
        ApplicationDetailResponse application = applicationService.forwardToNrccChair(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application forwarded to NRCC Chair", application));
    }

    /**
     * NRCC Chair assigns verification task (FR-009)
     */
    @PostMapping("/{id}/assign-verification")
    @RequirePermission(Permission.APPLICATION_ASSIGN_VERIFICATION)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> assignVerification(
            @PathVariable Long id,
            @Valid @RequestBody AssignVerificationRequest request) {
        ApplicationDetailResponse application = applicationService.assignVerification(id, request);
        return ResponseEntity.ok(ApiResponse.success("Verification assigned successfully", application));
    }

    /**
     * NRCC Member submits verification report (FR-010)
     */
    @PostMapping("/{id}/submit-verification-report")
    @RequirePermission(Permission.APPLICATION_VERIFY)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> submitVerificationReport(
            @PathVariable Long id,
            @Valid @RequestBody SubmitVerificationReportRequest request) {
        ApplicationDetailResponse application = applicationService.submitVerificationReport(id, request);
        return ResponseEntity.ok(ApiResponse.success("Verification report submitted successfully", application));
    }

    /**
     * Submit NRCC recommendation (FR-011)
     */
    @PostMapping("/{id}/submit-recommendation")
    @RequirePermission(Permission.APPLICATION_RECOMMEND)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> submitRecommendation(
            @PathVariable Long id,
            @Valid @RequestBody WorkflowActionRequest request) {
        ApplicationDetailResponse application = applicationService.submitRecommendation(id, request);
        return ResponseEntity.ok(ApiResponse.success("Recommendation submitted successfully", application));
    }

    /**
     * Minister records final decision (FR-008)
     */
    @PostMapping("/{id}/decision")
    @RequirePermission(Permission.APPLICATION_DECIDE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> recordMinisterDecision(
            @PathVariable Long id,
            @Valid @RequestBody MinisterDecisionRequest request) {
        ApplicationDetailResponse application = applicationService.recordMinisterDecision(id, request);
        return ResponseEntity.ok(ApiResponse.success("Decision recorded successfully", application));
    }

    /**
     * Ministry Lawyer updates gazettement status (FR-013)
     */
    @PostMapping("/{id}/gazette")
    @RequirePermission(Permission.APPLICATION_GAZETTE)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> updateGazettement(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGazettementRequest request) {
        ApplicationDetailResponse application = applicationService.updateGazettement(id, request);
        return ResponseEntity.ok(ApiResponse.success("Gazettement updated successfully", application));
    }

    /**
     * Submit appeal (FR-014)
     */
    @PostMapping("/{id}/appeal")
    @RequirePermission(Permission.APPLICATION_APPEAL)
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> submitAppeal(
            @PathVariable Long id,
            @Valid @RequestBody SubmitAppealRequest request) {
        ApplicationDetailResponse application = applicationService.submitAppeal(id, request);
        return ResponseEntity.ok(ApiResponse.success("Appeal submitted successfully", application));
    }
}
