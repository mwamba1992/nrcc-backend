package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.CreateActionPlanRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateActionPlanRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateActivityProgressRequest;
import tz.go.roadsfund.nrcc.dto.response.ActionPlanDetailResponse;
import tz.go.roadsfund.nrcc.dto.response.ActionPlanResponse;
import tz.go.roadsfund.nrcc.entity.*;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;
import tz.go.roadsfund.nrcc.enums.ActivityStatus;
import tz.go.roadsfund.nrcc.enums.Quarter;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.*;
import tz.go.roadsfund.nrcc.util.SecurityUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for Action Plan management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ActionPlanService {

    private final ActionPlanRepository actionPlanRepository;
    private final ActionPlanTargetRepository targetRepository;
    private final ActionPlanActivityRepository activityRepository;
    private final ActionPlanCostItemRepository costItemRepository;
    private final UserRepository userRepository;

    /**
     * Create a new action plan
     */
    public ActionPlanDetailResponse createActionPlan(CreateActionPlanRequest request) {
        User currentUser = getCurrentUser();

        ActionPlan actionPlan = ActionPlan.builder()
                .financialYear(request.getFinancialYear())
                .title(request.getTitle())
                .description(request.getDescription())
                .totalBudget(request.getTotalBudget())
                .status(ActionPlanStatus.DRAFT)
                .preparedBy(currentUser)
                .preparedDate(LocalDate.now())
                .version("1.0")
                .build();

        actionPlan = actionPlanRepository.save(actionPlan);

        // Create targets and activities
        if (request.getTargets() != null) {
            int targetOrder = 1;
            for (CreateActionPlanRequest.TargetRequest targetReq : request.getTargets()) {
                ActionPlanTarget target = createTarget(actionPlan, targetReq, targetOrder++);
                actionPlan.getTargets().add(target);
            }
        }

        log.info("Action plan created: {} for {}", actionPlan.getTitle(), actionPlan.getFinancialYear());
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Update action plan (only DRAFT status)
     */
    public ActionPlanDetailResponse updateActionPlan(Long id, UpdateActionPlanRequest request) {
        ActionPlan actionPlan = getActionPlanById(id);

        if (actionPlan.getStatus() != ActionPlanStatus.DRAFT) {
            throw new BadRequestException("Only draft action plans can be updated");
        }

        if (request.getTitle() != null) {
            actionPlan.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            actionPlan.setDescription(request.getDescription());
        }
        if (request.getTotalBudget() != null) {
            actionPlan.setTotalBudget(request.getTotalBudget());
        }

        actionPlan = actionPlanRepository.save(actionPlan);
        log.info("Action plan updated: {}", actionPlan.getId());
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Submit action plan for approval
     */
    public ActionPlanDetailResponse submitActionPlan(Long id) {
        ActionPlan actionPlan = getActionPlanById(id);

        if (actionPlan.getStatus() != ActionPlanStatus.DRAFT) {
            throw new BadRequestException("Only draft action plans can be submitted");
        }

        if (actionPlan.getTargets().isEmpty()) {
            throw new BadRequestException("Action plan must have at least one target");
        }

        actionPlan.setStatus(ActionPlanStatus.SUBMITTED);
        actionPlan = actionPlanRepository.save(actionPlan);

        log.info("Action plan submitted: {}", actionPlan.getId());
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Approve action plan
     */
    public ActionPlanDetailResponse approveActionPlan(Long id, String resolution) {
        ActionPlan actionPlan = getActionPlanById(id);

        if (actionPlan.getStatus() != ActionPlanStatus.SUBMITTED) {
            throw new BadRequestException("Only submitted action plans can be approved");
        }

        User currentUser = getCurrentUser();
        actionPlan.setStatus(ActionPlanStatus.APPROVED);
        actionPlan.setApprovedBy(currentUser);
        actionPlan.setApprovedDate(LocalDate.now());
        actionPlan.setApprovalResolution(resolution);

        actionPlan = actionPlanRepository.save(actionPlan);
        log.info("Action plan approved: {}", actionPlan.getId());
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Start action plan execution
     */
    public ActionPlanDetailResponse startExecution(Long id) {
        ActionPlan actionPlan = getActionPlanById(id);

        if (actionPlan.getStatus() != ActionPlanStatus.APPROVED) {
            throw new BadRequestException("Only approved action plans can be started");
        }

        actionPlan.setStatus(ActionPlanStatus.IN_PROGRESS);
        actionPlan = actionPlanRepository.save(actionPlan);

        log.info("Action plan execution started: {}", actionPlan.getId());
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Update activity progress
     */
    public ActionPlanDetailResponse updateActivityProgress(Long activityId, UpdateActivityProgressRequest request) {
        ActionPlanActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", activityId));

        ActionPlan actionPlan = activity.getTarget().getActionPlan();
        if (actionPlan.getStatus() != ActionPlanStatus.IN_PROGRESS) {
            throw new BadRequestException("Can only update progress for in-progress action plans");
        }

        if (request.getStatus() != null) {
            activity.setStatus(request.getStatus());
        }
        if (request.getProgressPercent() != null) {
            activity.setProgressPercent(request.getProgressPercent());

            // Auto-update status based on progress
            if (request.getProgressPercent() == 100) {
                activity.setStatus(ActivityStatus.COMPLETED);
            } else if (request.getProgressPercent() > 0 && activity.getStatus() == ActivityStatus.NOT_STARTED) {
                activity.setStatus(ActivityStatus.IN_PROGRESS);
            }
        }
        if (request.getActualCost() != null) {
            activity.setActualCost(request.getActualCost());
        }
        if (request.getComments() != null) {
            activity.setComments(request.getComments());
        }

        activityRepository.save(activity);

        // Check if all activities are complete
        checkAndUpdatePlanCompletion(actionPlan);

        log.info("Activity progress updated: {}", activityId);
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Get action plan by ID
     */
    @Transactional(readOnly = true)
    public ActionPlanDetailResponse getActionPlan(Long id) {
        ActionPlan actionPlan = getActionPlanById(id);
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Get all action plans (paginated)
     */
    @Transactional(readOnly = true)
    public Page<ActionPlanResponse> getAllActionPlans(Pageable pageable) {
        return actionPlanRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    /**
     * Get action plans by status
     */
    @Transactional(readOnly = true)
    public List<ActionPlanResponse> getActionPlansByStatus(ActionPlanStatus status) {
        return actionPlanRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get action plan by financial year
     */
    @Transactional(readOnly = true)
    public ActionPlanDetailResponse getActionPlanByFinancialYear(String financialYear) {
        ActionPlan actionPlan = actionPlanRepository.findByFinancialYear(financialYear)
                .orElseThrow(() -> new ResourceNotFoundException("ActionPlan", "financialYear", financialYear));
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Delete action plan (only DRAFT)
     */
    public void deleteActionPlan(Long id) {
        ActionPlan actionPlan = getActionPlanById(id);

        if (actionPlan.getStatus() != ActionPlanStatus.DRAFT) {
            throw new BadRequestException("Only draft action plans can be deleted");
        }

        actionPlanRepository.delete(actionPlan);
        log.info("Action plan deleted: {}", id);
    }

    /**
     * Add target to action plan
     */
    public ActionPlanDetailResponse addTarget(Long actionPlanId, CreateActionPlanRequest.TargetRequest request) {
        ActionPlan actionPlan = getActionPlanById(actionPlanId);

        if (actionPlan.getStatus() != ActionPlanStatus.DRAFT) {
            throw new BadRequestException("Can only add targets to draft action plans");
        }

        int nextOrder = actionPlan.getTargets().size() + 1;
        ActionPlanTarget target = createTarget(actionPlan, request, nextOrder);
        actionPlan.getTargets().add(target);

        actionPlan = actionPlanRepository.save(actionPlan);
        return mapToDetailResponse(actionPlan);
    }

    /**
     * Add activity to target
     */
    public ActionPlanDetailResponse addActivity(Long targetId, CreateActionPlanRequest.ActivityRequest request) {
        ActionPlanTarget target = targetRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Target", "id", targetId));

        ActionPlan actionPlan = target.getActionPlan();
        if (actionPlan.getStatus() != ActionPlanStatus.DRAFT) {
            throw new BadRequestException("Can only add activities to draft action plans");
        }

        int nextOrder = target.getActivities().size() + 1;
        ActionPlanActivity activity = createActivity(target, request, nextOrder);
        target.getActivities().add(activity);

        targetRepository.save(target);
        return mapToDetailResponse(actionPlan);
    }

    // ==================== HELPER METHODS ====================

    private ActionPlan getActionPlanById(Long id) {
        return actionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActionPlan", "id", id));
    }

    private User getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Current user not found"));
    }

    private ActionPlanTarget createTarget(ActionPlan actionPlan, CreateActionPlanRequest.TargetRequest request, int order) {
        ActionPlanTarget target = ActionPlanTarget.builder()
                .actionPlan(actionPlan)
                .title(request.getTitle())
                .dueDate(request.getDueDate())
                .indicator(request.getIndicator())
                .baseline(request.getBaseline())
                .targetValue(request.getTargetValue())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : order)
                .subtotal(request.getSubtotal())
                .build();

        target = targetRepository.save(target);

        if (request.getActivities() != null) {
            int activityOrder = 1;
            for (CreateActionPlanRequest.ActivityRequest activityReq : request.getActivities()) {
                ActionPlanActivity activity = createActivity(target, activityReq, activityOrder++);
                target.getActivities().add(activity);
            }
        }

        return target;
    }

    private ActionPlanActivity createActivity(ActionPlanTarget target, CreateActionPlanRequest.ActivityRequest request, int order) {
        Set<Quarter> quarters = new HashSet<>();
        if (request.getQuarterSchedule() != null) {
            for (String q : request.getQuarterSchedule()) {
                quarters.add(Quarter.valueOf(q));
            }
        }

        ActionPlanActivity activity = ActionPlanActivity.builder()
                .target(target)
                .description(request.getDescription())
                .quarterSchedule(quarters)
                .expectedOutput(request.getExpectedOutput())
                .responsibleUnit(request.getResponsibleUnit())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : order)
                .status(ActivityStatus.NOT_STARTED)
                .progressPercent(0)
                .build();

        activity = activityRepository.save(activity);

        if (request.getCostItems() != null) {
            for (CreateActionPlanRequest.CostItemRequest costReq : request.getCostItems()) {
                ActionPlanCostItem costItem = ActionPlanCostItem.builder()
                        .activity(activity)
                        .costItem(costReq.getDescription())
                        .computedCost(costReq.getAmount())
                        .build();
                costItemRepository.save(costItem);
                activity.getCostItems().add(costItem);
            }
        }

        return activity;
    }

    private void checkAndUpdatePlanCompletion(ActionPlan actionPlan) {
        boolean allComplete = actionPlan.getTargets().stream()
                .flatMap(t -> t.getActivities().stream())
                .allMatch(a -> a.getStatus() == ActivityStatus.COMPLETED);

        if (allComplete && !actionPlan.getTargets().isEmpty()) {
            actionPlan.setStatus(ActionPlanStatus.COMPLETED);
            actionPlanRepository.save(actionPlan);
            log.info("Action plan completed: {}", actionPlan.getId());
        }
    }

    private ActionPlanResponse mapToResponse(ActionPlan actionPlan) {
        int activityCount = 0;
        int completedCount = 0;
        int totalProgress = 0;

        for (ActionPlanTarget target : actionPlan.getTargets()) {
            for (ActionPlanActivity activity : target.getActivities()) {
                activityCount++;
                if (activity.getStatus() == ActivityStatus.COMPLETED) {
                    completedCount++;
                }
                totalProgress += activity.getProgressPercent() != null ? activity.getProgressPercent() : 0;
            }
        }

        Integer overallProgress = activityCount > 0 ? totalProgress / activityCount : 0;

        return ActionPlanResponse.builder()
                .id(actionPlan.getId())
                .financialYear(actionPlan.getFinancialYear())
                .title(actionPlan.getTitle())
                .version(actionPlan.getVersion())
                .status(actionPlan.getStatus())
                .totalBudget(actionPlan.getTotalBudget())
                .preparedById(actionPlan.getPreparedBy() != null ? actionPlan.getPreparedBy().getId() : null)
                .preparedByName(actionPlan.getPreparedBy() != null ? actionPlan.getPreparedBy().getName() : null)
                .preparedDate(actionPlan.getPreparedDate())
                .approvedById(actionPlan.getApprovedBy() != null ? actionPlan.getApprovedBy().getId() : null)
                .approvedByName(actionPlan.getApprovedBy() != null ? actionPlan.getApprovedBy().getName() : null)
                .approvedDate(actionPlan.getApprovedDate())
                .targetCount(actionPlan.getTargets().size())
                .activityCount(activityCount)
                .completedActivityCount(completedCount)
                .overallProgress(overallProgress)
                .createdAt(actionPlan.getCreatedAt())
                .updatedAt(actionPlan.getUpdatedAt())
                .build();
    }

    private ActionPlanDetailResponse mapToDetailResponse(ActionPlan actionPlan) {
        BigDecimal totalActualCost = BigDecimal.ZERO;
        int totalProgress = 0;
        int activityCount = 0;

        List<ActionPlanDetailResponse.TargetResponse> targetResponses = actionPlan.getTargets().stream()
                .map(target -> {
                    List<ActionPlanDetailResponse.ActivityResponse> activityResponses = target.getActivities().stream()
                            .map(activity -> {
                                BigDecimal budgetedCost = activity.getCostItems().stream()
                                        .map(ci -> ci.getComputedCost() != null ? ci.getComputedCost() : BigDecimal.ZERO)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                List<ActionPlanDetailResponse.CostItemResponse> costItemResponses = activity.getCostItems().stream()
                                        .map(ci -> ActionPlanDetailResponse.CostItemResponse.builder()
                                                .id(ci.getId())
                                                .description(ci.getCostItem())
                                                .amount(ci.getComputedCost())
                                                .fundingSource(null)
                                                .build())
                                        .collect(Collectors.toList());

                                Set<String> quarterStrings = activity.getQuarterSchedule().stream()
                                        .map(Enum::name)
                                        .collect(Collectors.toSet());

                                return ActionPlanDetailResponse.ActivityResponse.builder()
                                        .id(activity.getId())
                                        .description(activity.getDescription())
                                        .quarterSchedule(quarterStrings)
                                        .expectedOutput(activity.getExpectedOutput())
                                        .responsibleUnit(activity.getResponsibleUnit())
                                        .status(activity.getStatus())
                                        .progressPercent(activity.getProgressPercent())
                                        .startDate(activity.getStartDate())
                                        .endDate(activity.getEndDate())
                                        .comments(activity.getComments())
                                        .actualCost(activity.getActualCost())
                                        .displayOrder(activity.getDisplayOrder())
                                        .costItems(costItemResponses)
                                        .budgetedCost(budgetedCost)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    int targetProgress = target.getActivities().isEmpty() ? 0 :
                            target.getActivities().stream()
                                    .mapToInt(a -> a.getProgressPercent() != null ? a.getProgressPercent() : 0)
                                    .sum() / target.getActivities().size();

                    return ActionPlanDetailResponse.TargetResponse.builder()
                            .id(target.getId())
                            .title(target.getTitle())
                            .dueDate(target.getDueDate())
                            .indicator(target.getIndicator())
                            .baseline(target.getBaseline())
                            .targetValue(target.getTargetValue())
                            .displayOrder(target.getDisplayOrder())
                            .subtotal(target.getSubtotal())
                            .activities(activityResponses)
                            .targetProgress(targetProgress)
                            .build();
                })
                .collect(Collectors.toList());

        // Calculate totals
        for (ActionPlanTarget target : actionPlan.getTargets()) {
            for (ActionPlanActivity activity : target.getActivities()) {
                activityCount++;
                totalProgress += activity.getProgressPercent() != null ? activity.getProgressPercent() : 0;
                if (activity.getActualCost() != null) {
                    totalActualCost = totalActualCost.add(activity.getActualCost());
                }
            }
        }

        Integer overallProgress = activityCount > 0 ? totalProgress / activityCount : 0;

        return ActionPlanDetailResponse.builder()
                .id(actionPlan.getId())
                .financialYear(actionPlan.getFinancialYear())
                .title(actionPlan.getTitle())
                .version(actionPlan.getVersion())
                .description(actionPlan.getDescription())
                .status(actionPlan.getStatus())
                .totalBudget(actionPlan.getTotalBudget())
                .preparedById(actionPlan.getPreparedBy() != null ? actionPlan.getPreparedBy().getId() : null)
                .preparedByName(actionPlan.getPreparedBy() != null ? actionPlan.getPreparedBy().getName() : null)
                .preparedDate(actionPlan.getPreparedDate())
                .approvedById(actionPlan.getApprovedBy() != null ? actionPlan.getApprovedBy().getId() : null)
                .approvedByName(actionPlan.getApprovedBy() != null ? actionPlan.getApprovedBy().getName() : null)
                .approvedDate(actionPlan.getApprovedDate())
                .approvalResolution(actionPlan.getApprovalResolution())
                .targets(targetResponses)
                .overallProgress(overallProgress)
                .totalActualCost(totalActualCost)
                .createdAt(actionPlan.getCreatedAt())
                .updatedAt(actionPlan.getUpdatedAt())
                .build();
    }
}
