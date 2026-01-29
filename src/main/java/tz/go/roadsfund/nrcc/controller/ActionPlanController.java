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
import tz.go.roadsfund.nrcc.dto.request.CreateActionPlanRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateActionPlanRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateActivityProgressRequest;
import tz.go.roadsfund.nrcc.dto.response.ActionPlanDetailResponse;
import tz.go.roadsfund.nrcc.dto.response.ActionPlanResponse;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.ActionPlanService;

import java.util.List;

/**
 * REST Controller for Action Plan management
 */
@RestController
@RequestMapping("/action-plans")
@RequiredArgsConstructor
public class ActionPlanController {

    private final ActionPlanService actionPlanService;

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new action plan
     */
    @PostMapping
    @RequirePermission(Permission.ACTION_PLAN_CREATE)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> createActionPlan(
            @Valid @RequestBody CreateActionPlanRequest request) {
        ActionPlanDetailResponse actionPlan = actionPlanService.createActionPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Action plan created successfully", actionPlan));
    }

    /**
     * Update an existing action plan
     */
    @PutMapping("/{id}")
    @RequirePermission(Permission.ACTION_PLAN_UPDATE)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> updateActionPlan(
            @PathVariable Long id,
            @Valid @RequestBody UpdateActionPlanRequest request) {
        ActionPlanDetailResponse actionPlan = actionPlanService.updateActionPlan(id, request);
        return ResponseEntity.ok(ApiResponse.success("Action plan updated successfully", actionPlan));
    }

    /**
     * Get action plan by ID
     */
    @GetMapping("/{id}")
    @RequirePermission(Permission.ACTION_PLAN_READ)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> getActionPlan(@PathVariable Long id) {
        ActionPlanDetailResponse actionPlan = actionPlanService.getActionPlan(id);
        return ResponseEntity.ok(ApiResponse.success("Action plan retrieved successfully", actionPlan));
    }

    /**
     * Get all action plans (paginated)
     */
    @GetMapping
    @RequirePermission(Permission.ACTION_PLAN_READ)
    public ResponseEntity<ApiResponse<Page<ActionPlanResponse>>> getAllActionPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ActionPlanResponse> actionPlans = actionPlanService.getAllActionPlans(pageable);
        return ResponseEntity.ok(ApiResponse.success("Action plans retrieved successfully", actionPlans));
    }

    /**
     * Get action plans by status
     */
    @GetMapping("/status/{status}")
    @RequirePermission(Permission.ACTION_PLAN_READ)
    public ResponseEntity<ApiResponse<List<ActionPlanResponse>>> getActionPlansByStatus(
            @PathVariable ActionPlanStatus status) {
        List<ActionPlanResponse> actionPlans = actionPlanService.getActionPlansByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Action plans retrieved successfully", actionPlans));
    }

    /**
     * Get action plan by financial year
     */
    @GetMapping("/year/{financialYear}")
    @RequirePermission(Permission.ACTION_PLAN_READ)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> getActionPlanByYear(
            @PathVariable String financialYear) {
        ActionPlanDetailResponse actionPlan = actionPlanService.getActionPlanByFinancialYear(financialYear);
        return ResponseEntity.ok(ApiResponse.success("Action plan retrieved successfully", actionPlan));
    }

    /**
     * Delete action plan (only DRAFT)
     */
    @DeleteMapping("/{id}")
    @RequirePermission(Permission.ACTION_PLAN_DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteActionPlan(@PathVariable Long id) {
        actionPlanService.deleteActionPlan(id);
        return ResponseEntity.ok(ApiResponse.success("Action plan deleted successfully", null));
    }

    // ==================== WORKFLOW OPERATIONS ====================

    /**
     * Submit action plan for approval
     */
    @PostMapping("/{id}/submit")
    @RequirePermission(Permission.ACTION_PLAN_CREATE)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> submitActionPlan(@PathVariable Long id) {
        ActionPlanDetailResponse actionPlan = actionPlanService.submitActionPlan(id);
        return ResponseEntity.ok(ApiResponse.success("Action plan submitted for approval", actionPlan));
    }

    /**
     * Approve action plan
     */
    @PostMapping("/{id}/approve")
    @RequirePermission(Permission.ACTION_PLAN_APPROVE)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> approveActionPlan(
            @PathVariable Long id,
            @RequestParam(required = false) String resolution) {
        ActionPlanDetailResponse actionPlan = actionPlanService.approveActionPlan(id, resolution);
        return ResponseEntity.ok(ApiResponse.success("Action plan approved successfully", actionPlan));
    }

    /**
     * Start action plan execution
     */
    @PostMapping("/{id}/start")
    @RequirePermission(Permission.ACTION_PLAN_TRACK)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> startExecution(@PathVariable Long id) {
        ActionPlanDetailResponse actionPlan = actionPlanService.startExecution(id);
        return ResponseEntity.ok(ApiResponse.success("Action plan execution started", actionPlan));
    }

    // ==================== TARGET & ACTIVITY OPERATIONS ====================

    /**
     * Add target to action plan
     */
    @PostMapping("/{id}/targets")
    @RequirePermission(Permission.ACTION_PLAN_UPDATE)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> addTarget(
            @PathVariable Long id,
            @Valid @RequestBody CreateActionPlanRequest.TargetRequest request) {
        ActionPlanDetailResponse actionPlan = actionPlanService.addTarget(id, request);
        return ResponseEntity.ok(ApiResponse.success("Target added successfully", actionPlan));
    }

    /**
     * Add activity to target
     */
    @PostMapping("/targets/{targetId}/activities")
    @RequirePermission(Permission.ACTION_PLAN_UPDATE)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> addActivity(
            @PathVariable Long targetId,
            @Valid @RequestBody CreateActionPlanRequest.ActivityRequest request) {
        ActionPlanDetailResponse actionPlan = actionPlanService.addActivity(targetId, request);
        return ResponseEntity.ok(ApiResponse.success("Activity added successfully", actionPlan));
    }

    /**
     * Update activity progress
     */
    @PatchMapping("/activities/{activityId}/progress")
    @RequirePermission(Permission.ACTION_PLAN_TRACK)
    public ResponseEntity<ApiResponse<ActionPlanDetailResponse>> updateActivityProgress(
            @PathVariable Long activityId,
            @Valid @RequestBody UpdateActivityProgressRequest request) {
        ActionPlanDetailResponse actionPlan = actionPlanService.updateActivityProgress(activityId, request);
        return ResponseEntity.ok(ApiResponse.success("Activity progress updated", actionPlan));
    }
}
