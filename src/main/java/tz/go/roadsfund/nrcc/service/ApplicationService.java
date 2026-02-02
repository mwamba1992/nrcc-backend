package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.*;
import tz.go.roadsfund.nrcc.dto.response.ApplicationDetailResponse;
import tz.go.roadsfund.nrcc.dto.response.ApplicationResponse;
import tz.go.roadsfund.nrcc.entity.*;
import tz.go.roadsfund.nrcc.enums.*;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.*;
import tz.go.roadsfund.nrcc.util.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for application management and workflow operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationFormDataRepository formDataRepository;
    private final EligibilityCriteriaSelectionRepository eligibilityRepository;
    private final ApprovalActionRepository approvalActionRepository;
    private final VerificationAssignmentRepository verificationAssignmentRepository;
    private final VerificationReportRepository verificationReportRepository;
    private final RecommendationRepository recommendationRepository;
    private final MinisterDecisionRepository ministerDecisionRepository;
    private final GazettementRepository gazettementRepository;
    private final AppealRepository appealRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // ==================== APPLICATION CRUD ====================

    /**
     * Create a new application (FR-002, FR-003)
     */
    public ApplicationDetailResponse createApplication(CreateApplicationRequest request) {
        User currentUser = getCurrentUser();

        // Validate eligibility criteria (BR-01, BR-02)
        validateEligibilityCriteria(request.getProposedClass(), request.getEligibilityCriteria());

        // Generate application number
        String applicationNumber = generateApplicationNumber();

        // Create application
        Application application = Application.builder()
                .applicationNumber(applicationNumber)
                .applicantType(request.getApplicantType())
                .applicant(currentUser)
                .proposedClass(request.getProposedClass())
                .status(ApplicationStatus.DRAFT)
                .currentOwner(currentUser)
                .build();

        application = applicationRepository.save(application);

        // Create form data
        ApplicationFormData formData = ApplicationFormData.builder()
                .application(application)
                .roadName(request.getRoadName())
                .roadLength(request.getRoadLength())
                .currentClass(request.getCurrentClass().name())
                .proposedClass(request.getProposedClass().name())
                .startingPoint(request.getStartingPoint())
                .terminalPoint(request.getTerminalPoint())
                .reclassificationReasons(request.getReclassificationReasons())
                .surfaceTypeCarriageway(request.getSurfaceTypeCarriageway())
                .surfaceTypeShoulders(request.getSurfaceTypeShoulders())
                .carriagewayWidth(request.getCarriagewayWidth())
                .formationWidth(request.getFormationWidth())
                .actualRoadReserveWidth(request.getActualRoadReserveWidth())
                .trafficLevel(request.getTrafficLevel())
                .trafficComposition(request.getTrafficComposition())
                .townsVillagesLinked(request.getTownsVillagesLinked())
                .principalNodes(request.getPrincipalNodes())
                .busRoutes(request.getBusRoutes())
                .publicServices(request.getPublicServices())
                .alternativeRoutes(request.getAlternativeRoutes())
                .build();

        formDataRepository.save(formData);
        application.setFormData(formData);

        // Save eligibility criteria selections
        saveEligibilityCriteria(application, request.getEligibilityCriteria());

        // Record action
        recordAction(application, WorkflowAction.CREATE, null, ApplicationStatus.DRAFT, "Application created");

        log.info("Application created: {}", applicationNumber);
        return mapToDetailResponse(application);
    }

    /**
     * Update application (only in DRAFT or RETURNED_FOR_CORRECTION status)
     */
    public ApplicationDetailResponse updateApplication(Long id, UpdateApplicationRequest request) {
        Application application = getApplicationById(id);

        // Check if application can be edited
        if (!application.getStatus().isEditableByApplicant()) {
            throw new BadRequestException("Application cannot be edited in current status: " + application.getStatus());
        }

        // Check ownership
        validateOwnership(application);

        // Update form data
        ApplicationFormData formData = application.getFormData();
        if (formData != null) {
            updateFormDataFromRequest(formData, request);
            formDataRepository.save(formData);
        }

        // Update eligibility criteria if provided
        if (request.getEligibilityCriteria() != null) {
            RoadClass proposedClass = request.getProposedClass() != null ?
                    request.getProposedClass() : application.getProposedClass();
            validateEligibilityCriteria(proposedClass, request.getEligibilityCriteria());

            // Clear existing and save new
            eligibilityRepository.deleteByApplication(application);
            saveEligibilityCriteria(application, request.getEligibilityCriteria());
        }

        if (request.getProposedClass() != null) {
            application.setProposedClass(request.getProposedClass());
        }

        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.UPDATE, application.getStatus(), application.getStatus(), "Application updated");

        log.info("Application updated: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Get application by ID
     */
    @Transactional(readOnly = true)
    public ApplicationDetailResponse getApplication(Long id) {
        Application application = getApplicationById(id);
        return mapToDetailResponse(application);
    }

    /**
     * Get application by number
     */
    @Transactional(readOnly = true)
    public ApplicationDetailResponse getApplicationByNumber(String applicationNumber) {
        Application application = applicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "applicationNumber", applicationNumber));
        return mapToDetailResponse(application);
    }

    /**
     * Get all applications (paginated)
     */
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getAllApplications(Pageable pageable) {
        return applicationRepository.findAll(pageable).map(this::mapToResponse);
    }

    /**
     * Get applications by status
     */
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getApplicationsByStatus(ApplicationStatus status, Pageable pageable) {
        return applicationRepository.findByStatus(status, pageable).map(this::mapToResponse);
    }

    /**
     * Get current user's applications
     */
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getMyApplications(Pageable pageable) {
        User currentUser = getCurrentUser();
        return applicationRepository.findByApplicant(currentUser, pageable).map(this::mapToResponse);
    }

    /**
     * Get applications assigned to current user
     */
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyAssignedApplications() {
        User currentUser = getCurrentUser();
        return applicationRepository.findByCurrentOwner(currentUser).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Delete application (only DRAFT status)
     */
    public void deleteApplication(Long id) {
        Application application = getApplicationById(id);

        if (application.getStatus() != ApplicationStatus.DRAFT) {
            throw new BadRequestException("Only draft applications can be deleted");
        }

        validateOwnership(application);

        applicationRepository.delete(application);
        log.info("Application deleted: {}", application.getApplicationNumber());
    }

    // ==================== WORKFLOW OPERATIONS ====================

    /**
     * Submit application (FR-004, FR-005, FR-006)
     */
    public ApplicationDetailResponse submitApplication(Long id) {
        Application application = getApplicationById(id);

        if (application.getStatus() != ApplicationStatus.DRAFT &&
            application.getStatus() != ApplicationStatus.RETURNED_FOR_CORRECTION) {
            throw new BadRequestException("Application cannot be submitted in current status");
        }

        validateOwnership(application);

        // Validate all required fields (BR-05)
        validateForSubmission(application);

        ApplicationStatus previousStatus = application.getStatus();
        ApplicationStatus newStatus;
        User newOwner;

        // Route based on applicant type (Workflow A vs B)
        if (application.getApplicantType() == ApplicantType.REGIONAL_ROADS_BOARD) {
            // Workflow B: Goes to RAS
            newStatus = ApplicationStatus.UNDER_RAS_REVIEW;
            newOwner = findUserByRole(UserRole.REGIONAL_ADMINISTRATIVE_SECRETARY);
        } else {
            // Workflow A: Public/MP goes directly to Minister
            newStatus = ApplicationStatus.UNDER_MINISTER_REVIEW;
            newOwner = findUserByRole(UserRole.MINISTER_OF_WORKS);
        }

        application.setStatus(newStatus);
        application.setCurrentOwner(newOwner);
        application.setSubmissionDate(LocalDate.now());
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.SUBMIT, previousStatus, newStatus, "Application submitted");

        // Send notification
        notificationService.sendApplicationNotification(application, "Application submitted successfully");

        log.info("Application submitted: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * RAS approves and forwards to RC (Workflow B - Step 2)
     */
    public ApplicationDetailResponse rasApprove(Long id, WorkflowActionRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.UNDER_RAS_REVIEW);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.UNDER_RC_REVIEW);
        application.setCurrentOwner(findUserByRole(UserRole.REGIONAL_COMMISSIONER));
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.APPROVE, previousStatus, ApplicationStatus.UNDER_RC_REVIEW, request.getComments());

        log.info("RAS approved application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * RC approves and forwards to Minister (Workflow B - Step 3)
     */
    public ApplicationDetailResponse rcApprove(Long id, WorkflowActionRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.UNDER_RC_REVIEW);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.UNDER_MINISTER_REVIEW);
        application.setCurrentOwner(findUserByRole(UserRole.MINISTER_OF_WORKS));
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.APPROVE, previousStatus, ApplicationStatus.UNDER_MINISTER_REVIEW, request.getComments());

        log.info("RC approved application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Return application for correction (FR-007)
     */
    public ApplicationDetailResponse returnForCorrection(Long id, WorkflowActionRequest request) {
        Application application = getApplicationById(id);

        // Can return from various review stages
        List<ApplicationStatus> returnableStatuses = List.of(
                ApplicationStatus.UNDER_RAS_REVIEW,
                ApplicationStatus.UNDER_RC_REVIEW,
                ApplicationStatus.UNDER_MINISTER_REVIEW,
                ApplicationStatus.WITH_NRCC_CHAIR
        );

        if (!returnableStatuses.contains(application.getStatus())) {
            throw new BadRequestException("Application cannot be returned in current status");
        }

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.RETURNED_FOR_CORRECTION);
        application.setCurrentOwner(application.getApplicant());
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.RETURN, previousStatus, ApplicationStatus.RETURNED_FOR_CORRECTION, request.getComments());

        notificationService.sendApplicationNotification(application, "Application returned for correction: " + request.getComments());

        log.info("Application returned for correction: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Minister forwards to NRCC Chair (Workflow C - Step 1)
     */
    public ApplicationDetailResponse forwardToNrccChair(Long id, WorkflowActionRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.UNDER_MINISTER_REVIEW);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.WITH_NRCC_CHAIR);
        application.setCurrentOwner(findUserByRole(UserRole.NRCC_CHAIRPERSON));
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.FORWARD, previousStatus, ApplicationStatus.WITH_NRCC_CHAIR, request.getComments());

        log.info("Application forwarded to NRCC Chair: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * NRCC Chair assigns verification task (FR-009, Workflow C - Step 2)
     */
    public ApplicationDetailResponse assignVerification(Long id, AssignVerificationRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.WITH_NRCC_CHAIR);

        User member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getMemberId()));

        if (member.getRole() != UserRole.NRCC_MEMBER) {
            throw new BadRequestException("User is not an NRCC Member");
        }

        VerificationAssignment assignment = VerificationAssignment.builder()
                .application(application)
                .member(member)
                .assignedBy(getCurrentUser())
                .dueDate(request.getDueDate())
                .visitDate(request.getVisitDate())
                .status(VerificationStatus.ASSIGNED)
                .instructions(request.getInstructions())
                .build();

        verificationAssignmentRepository.save(assignment);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.VERIFICATION_IN_PROGRESS);
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.ASSIGN, previousStatus, ApplicationStatus.VERIFICATION_IN_PROGRESS,
                "Verification assigned to " + member.getName());

        log.info("Verification assigned for application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * NRCC Member submits verification report (FR-010, Workflow C - Step 3)
     */
    public ApplicationDetailResponse submitVerificationReport(Long applicationId, SubmitVerificationReportRequest request) {
        Application application = getApplicationById(applicationId);

        VerificationAssignment assignment = verificationAssignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("VerificationAssignment", "id", request.getAssignmentId()));

        if (!assignment.getApplication().getId().equals(applicationId)) {
            throw new BadRequestException("Assignment does not belong to this application");
        }

        User currentUser = getCurrentUser();
        if (!assignment.getMember().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Only the assigned member can submit the report");
        }

        VerificationReport report = VerificationReport.builder()
                .application(application)
                .assignment(assignment)
                .member(currentUser)
                .findings(request.getFindings())
                .visitDate(request.getVisitDate())
                .submittedAt(LocalDateTime.now())
                .version(1)
                .build();

        verificationReportRepository.save(report);

        assignment.setStatus(VerificationStatus.COMPLETED);
        verificationAssignmentRepository.save(assignment);

        // Check if all verifications are complete
        boolean allComplete = application.getVerificationAssignments().stream()
                .allMatch(a -> a.getStatus() == VerificationStatus.COMPLETED);

        if (allComplete) {
            application.setStatus(ApplicationStatus.NRCC_REVIEW_MEETING);
        }
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.VERIFY, ApplicationStatus.VERIFICATION_IN_PROGRESS,
                application.getStatus(), "Verification report submitted");

        log.info("Verification report submitted for application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Submit NRCC recommendation (FR-011, Workflow C - Step 5)
     */
    public ApplicationDetailResponse submitRecommendation(Long id, WorkflowActionRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.NRCC_REVIEW_MEETING);

        Recommendation recommendation = Recommendation.builder()
                .application(application)
                .recommendationText(request.getComments())
                .submittedBy(getCurrentUser())
                .submittedDate(LocalDateTime.now())
                .build();

        recommendationRepository.save(recommendation);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.RECOMMENDATION_SUBMITTED);
        application.setCurrentOwner(findUserByRole(UserRole.MINISTER_OF_WORKS));
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.RECOMMEND, previousStatus, ApplicationStatus.RECOMMENDATION_SUBMITTED,
                request.getComments());

        log.info("Recommendation submitted for application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Minister records final decision (FR-008, Workflow C - Step 6)
     */
    public ApplicationDetailResponse recordMinisterDecision(Long id, MinisterDecisionRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.RECOMMENDATION_SUBMITTED);

        Recommendation recommendation = recommendationRepository.findByApplication(application)
                .orElseThrow(() -> new BadRequestException("No recommendation found for this application"));

        // Validate disapproval type if disapproved
        if (request.getDecision() == DecisionType.DISAPPROVE && request.getDisapprovalType() == null) {
            throw new BadRequestException("Disapproval type is required when disapproving");
        }

        MinisterDecision decision = MinisterDecision.builder()
                .application(application)
                .recommendation(recommendation)
                .decision(request.getDecision())
                .disapprovalType(request.getDisapprovalType())
                .reason(request.getReason())
                .decidedBy(getCurrentUser())
                .decisionDate(LocalDateTime.now())
                .build();

        ministerDecisionRepository.save(decision);

        ApplicationStatus previousStatus = application.getStatus();
        ApplicationStatus newStatus;

        if (request.getDecision() == DecisionType.APPROVE) {
            newStatus = ApplicationStatus.PENDING_GAZETTEMENT;
            application.setCurrentOwner(findUserByRole(UserRole.MINISTRY_LAWYER));

            // Create gazettement record
            Gazettement gazettement = Gazettement.builder()
                    .application(application)
                    .decision(decision)
                    .status(GazettementStatus.PENDING)
                    .build();
            gazettementRepository.save(gazettement);
        } else {
            if (request.getDisapprovalType() == DisapprovalType.REFUSED) {
                newStatus = ApplicationStatus.DISAPPROVED_REFUSED;
            } else {
                newStatus = ApplicationStatus.DISAPPROVED_DESIGNATED;
            }
            application.setCurrentOwner(null);
        }

        application.setStatus(newStatus);
        application.setDecisionDate(LocalDate.now());
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.DECIDE, previousStatus, newStatus, request.getReason());

        // Notify applicant
        String message = request.getDecision() == DecisionType.APPROVE ?
                "Your application has been approved" :
                "Your application has been disapproved: " + request.getReason();
        notificationService.sendApplicationNotification(application, message);

        log.info("Minister decision recorded for application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Ministry Lawyer updates gazettement status (FR-013, Workflow D)
     */
    public ApplicationDetailResponse updateGazettement(Long id, UpdateGazettementRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.PENDING_GAZETTEMENT);

        Gazettement gazettement = gazettementRepository.findByApplication(application)
                .orElseThrow(() -> new BadRequestException("No gazettement record found"));

        gazettement.setGazetteNumber(request.getGazetteNumber());
        gazettement.setGazetteDate(request.getGazetteDate());
        gazettement.setStatus(GazettementStatus.GAZETTED);
        gazettement.setProcessedBy(getCurrentUser());
        gazettementRepository.save(gazettement);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.GAZETTED);
        application.setCurrentOwner(null);
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.GAZETTE, previousStatus, ApplicationStatus.GAZETTED,
                "Gazetted with number: " + request.getGazetteNumber());

        notificationService.sendApplicationNotification(application,
                "Your road reclassification has been gazetted. Gazette Number: " + request.getGazetteNumber());

        log.info("Application gazetted: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Submit appeal (FR-014, Workflow D)
     */
    public ApplicationDetailResponse submitAppeal(Long id, SubmitAppealRequest request) {
        Application application = getApplicationById(id);

        if (!application.getStatus().canBeAppealed()) {
            throw new BadRequestException("Only refused applications can be appealed");
        }

        validateOwnership(application);

        MinisterDecision decision = ministerDecisionRepository.findByApplication(application)
                .orElseThrow(() -> new BadRequestException("No decision found to appeal"));

        Appeal appeal = Appeal.builder()
                .application(application)
                .decision(decision)
                .appellant(getCurrentUser())
                .grounds(request.getGrounds())
                .status(AppealStatus.SUBMITTED)
                .appealDate(LocalDateTime.now())
                .build();

        appealRepository.save(appeal);

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.APPEAL_SUBMITTED);
        application.setCurrentOwner(findUserByRole(UserRole.MINISTER_OF_WORKS));
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.APPEAL, previousStatus, ApplicationStatus.APPEAL_SUBMITTED,
                "Appeal submitted: " + request.getGrounds());

        log.info("Appeal submitted for application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    /**
     * Record appeal decision (FR-014)
     */
    public ApplicationDetailResponse recordAppealDecision(Long id, AppealDecisionRequest request) {
        Application application = getApplicationById(id);
        validateStatus(application, ApplicationStatus.APPEAL_SUBMITTED);

        List<Appeal> appeals = appealRepository.findByApplication(application);
        if (appeals.isEmpty()) {
            throw new BadRequestException("No appeal found for this application");
        }

        Appeal appeal = appeals.get(appeals.size() - 1); // Get the most recent appeal
        appeal.setStatus(AppealStatus.CLOSED);
        appeal.setDecisionDate(LocalDateTime.now());
        appeal.setDecidedBy(getCurrentUser());
        appeal.setDecisionReason(request.getReason());
        appealRepository.save(appeal);

        ApplicationStatus previousStatus = application.getStatus();
        ApplicationStatus newStatus;

        if (request.getDecision() == DecisionType.APPROVE) {
            // Appeal granted - restart the process
            newStatus = ApplicationStatus.WITH_NRCC_CHAIR;
            application.setCurrentOwner(findUserByRole(UserRole.NRCC_CHAIRPERSON));

            notificationService.sendApplicationNotification(application,
                    "Your appeal has been granted. Your application will be reviewed again by the NRCC.");
        } else {
            // Appeal rejected - final decision
            newStatus = ApplicationStatus.APPEAL_REJECTED;
            application.setCurrentOwner(null);

            notificationService.sendApplicationNotification(application,
                    "Your appeal has been rejected. Decision: " + request.getReason());
        }

        application.setStatus(newStatus);
        application = applicationRepository.save(application);

        recordAction(application, WorkflowAction.APPEAL_DECIDE, previousStatus, newStatus, request.getReason());

        log.info("Appeal decision recorded for application: {}", application.getApplicationNumber());
        return mapToDetailResponse(application);
    }

    // ==================== HELPER METHODS ====================

    private Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
    }

    private User getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Current user not found"));
    }

    private User findUserByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .filter(u -> "ACTIVE".equals(u.getStatus()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No active user found with role: " + role));
    }

    private void validateOwnership(Application application) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (!application.getApplicant().getId().equals(currentUserId)) {
            throw new BadRequestException("You are not the owner of this application");
        }
    }

    private void validateStatus(Application application, ApplicationStatus expectedStatus) {
        if (application.getStatus() != expectedStatus) {
            throw new BadRequestException("Application is not in expected status: " + expectedStatus);
        }
    }

    private void validateEligibilityCriteria(RoadClass proposedClass, List<CreateApplicationRequest.EligibilityCriterionRequest> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new BadRequestException("At least one eligibility criterion must be selected (BR-04)");
        }

        boolean hasValidCriterion = criteria.stream().anyMatch(c -> {
            EligibilityCriterion criterion = EligibilityCriterion.valueOf(c.getCriterionCode());
            if (proposedClass == RoadClass.REGIONAL) {
                return criterion.isRegionalCriterion();
            } else if (proposedClass == RoadClass.TRUNK) {
                return criterion.isTrunkCriterion();
            }
            return false;
        });

        if (!hasValidCriterion) {
            String message = proposedClass == RoadClass.REGIONAL ?
                    "At least one Regional criterion (R1-R7) must be selected (BR-01)" :
                    "At least one Trunk criterion (T1-T5) must be selected (BR-02)";
            throw new BadRequestException(message);
        }
    }

    private void validateForSubmission(Application application) {
        if (application.getFormData() == null) {
            throw new BadRequestException("Form data is required for submission (BR-05)");
        }

        List<EligibilityCriteriaSelection> criteria = eligibilityRepository.findByApplication(application);
        if (criteria.isEmpty()) {
            throw new BadRequestException("At least one eligibility criterion must be selected (BR-04)");
        }
    }

    private void saveEligibilityCriteria(Application application, List<CreateApplicationRequest.EligibilityCriterionRequest> criteria) {
        for (CreateApplicationRequest.EligibilityCriterionRequest c : criteria) {
            EligibilityCriterion criterion = EligibilityCriterion.valueOf(c.getCriterionCode());

            EligibilityCriteriaSelection selection = EligibilityCriteriaSelection.builder()
                    .application(application)
                    .criterion(criterion)
                    .details(c.getDetails())
                    .evidenceDescription(c.getEvidenceDescription())
                    .build();

            eligibilityRepository.save(selection);
        }
    }

    private void updateFormDataFromRequest(ApplicationFormData formData, UpdateApplicationRequest request) {
        if (request.getRoadName() != null) formData.setRoadName(request.getRoadName());
        if (request.getRoadLength() != null) formData.setRoadLength(request.getRoadLength());
        if (request.getCurrentClass() != null) formData.setCurrentClass(request.getCurrentClass().name());
        if (request.getProposedClass() != null) formData.setProposedClass(request.getProposedClass().name());
        if (request.getStartingPoint() != null) formData.setStartingPoint(request.getStartingPoint());
        if (request.getTerminalPoint() != null) formData.setTerminalPoint(request.getTerminalPoint());
        if (request.getReclassificationReasons() != null) formData.setReclassificationReasons(request.getReclassificationReasons());
        if (request.getSurfaceTypeCarriageway() != null) formData.setSurfaceTypeCarriageway(request.getSurfaceTypeCarriageway());
        if (request.getSurfaceTypeShoulders() != null) formData.setSurfaceTypeShoulders(request.getSurfaceTypeShoulders());
        if (request.getCarriagewayWidth() != null) formData.setCarriagewayWidth(request.getCarriagewayWidth());
        if (request.getFormationWidth() != null) formData.setFormationWidth(request.getFormationWidth());
        if (request.getActualRoadReserveWidth() != null) formData.setActualRoadReserveWidth(request.getActualRoadReserveWidth());
        if (request.getTrafficLevel() != null) formData.setTrafficLevel(request.getTrafficLevel());
        if (request.getTrafficComposition() != null) formData.setTrafficComposition(request.getTrafficComposition());
        if (request.getTownsVillagesLinked() != null) formData.setTownsVillagesLinked(request.getTownsVillagesLinked());
        if (request.getPrincipalNodes() != null) formData.setPrincipalNodes(request.getPrincipalNodes());
        if (request.getBusRoutes() != null) formData.setBusRoutes(request.getBusRoutes());
        if (request.getPublicServices() != null) formData.setPublicServices(request.getPublicServices());
        if (request.getAlternativeRoutes() != null) formData.setAlternativeRoutes(request.getAlternativeRoutes());
    }

    private String generateApplicationNumber() {
        int year = Year.now().getValue();
        long count = applicationRepository.count() + 1;
        return String.format("NRCC/%d/%04d", year, count);
    }

    private void recordAction(Application application, WorkflowAction action, ApplicationStatus fromStatus,
                              ApplicationStatus toStatus, String comments) {
        User currentUser = getCurrentUser();

        ApprovalAction approvalAction = ApprovalAction.builder()
                .application(application)
                .action(action)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .actor(currentUser)
                .actorRole(currentUser.getRole().name())
                .comments(comments)
                .actionDate(LocalDateTime.now())
                .build();

        approvalActionRepository.save(approvalAction);
    }

    // ==================== MAPPING METHODS ====================

    private ApplicationResponse mapToResponse(Application application) {
        ApplicationFormData formData = application.getFormData();

        return ApplicationResponse.builder()
                .id(application.getId())
                .applicationNumber(application.getApplicationNumber())
                .applicantType(application.getApplicantType().name())
                .applicantName(application.getApplicant() != null ? application.getApplicant().getName() : null)
                .applicantEmail(application.getApplicant() != null ? application.getApplicant().getEmail() : null)
                .roadName(formData != null ? formData.getRoadName() : null)
                .currentClass(formData != null ? formData.getCurrentClass() : null)
                .proposedClass(application.getProposedClass().name())
                .status(application.getStatus().name())
                .statusDisplayName(application.getStatus().getDisplayName())
                .currentOwnerName(application.getCurrentOwner() != null ? application.getCurrentOwner().getName() : null)
                .submissionDate(application.getSubmissionDate())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    private ApplicationDetailResponse mapToDetailResponse(Application application) {
        ApplicationFormData formData = application.getFormData();

        ApplicationDetailResponse.FormDataResponse formDataResponse = null;
        if (formData != null) {
            formDataResponse = ApplicationDetailResponse.FormDataResponse.builder()
                    .roadName(formData.getRoadName())
                    .roadLength(formData.getRoadLength())
                    .currentClass(formData.getCurrentClass())
                    .proposedClass(formData.getProposedClass())
                    .startingPoint(formData.getStartingPoint())
                    .terminalPoint(formData.getTerminalPoint())
                    .reclassificationReasons(formData.getReclassificationReasons())
                    .surfaceTypeCarriageway(formData.getSurfaceTypeCarriageway())
                    .surfaceTypeShoulders(formData.getSurfaceTypeShoulders())
                    .carriagewayWidth(formData.getCarriagewayWidth())
                    .formationWidth(formData.getFormationWidth())
                    .actualRoadReserveWidth(formData.getActualRoadReserveWidth())
                    .trafficLevel(formData.getTrafficLevel())
                    .trafficComposition(formData.getTrafficComposition())
                    .townsVillagesLinked(formData.getTownsVillagesLinked())
                    .principalNodes(formData.getPrincipalNodes())
                    .busRoutes(formData.getBusRoutes())
                    .publicServices(formData.getPublicServices())
                    .alternativeRoutes(formData.getAlternativeRoutes())
                    .build();
        }

        // Map eligibility criteria
        List<ApplicationDetailResponse.EligibilityCriterionResponse> criteriaResponses =
                eligibilityRepository.findByApplication(application).stream()
                        .map(c -> ApplicationDetailResponse.EligibilityCriterionResponse.builder()
                                .code(c.getCriterion().name())
                                .description(c.getCriterion().getDescription())
                                .details(c.getDetails())
                                .evidenceDescription(c.getEvidenceDescription())
                                .build())
                        .collect(Collectors.toList());

        // Map approval history
        List<ApplicationDetailResponse.ApprovalActionResponse> historyResponses =
                approvalActionRepository.findByApplicationOrderByActionDateDesc(application).stream()
                        .map(a -> ApplicationDetailResponse.ApprovalActionResponse.builder()
                                .id(a.getId())
                                .action(a.getAction().name())
                                .fromStatus(a.getFromStatus() != null ? a.getFromStatus().name() : null)
                                .toStatus(a.getToStatus().name())
                                .actorName(a.getActor().getName())
                                .actorRole(a.getActorRole())
                                .comments(a.getComments())
                                .actionDate(a.getActionDate())
                                .build())
                        .collect(Collectors.toList());

        return ApplicationDetailResponse.builder()
                .id(application.getId())
                .applicationNumber(application.getApplicationNumber())
                .applicantType(application.getApplicantType().name())
                .applicantTypeDisplayName(application.getApplicantType().getDisplayName())
                .applicantId(application.getApplicant() != null ? application.getApplicant().getId() : null)
                .applicantName(application.getApplicant() != null ? application.getApplicant().getName() : null)
                .applicantEmail(application.getApplicant() != null ? application.getApplicant().getEmail() : null)
                .applicantPhone(application.getApplicant() != null ? application.getApplicant().getPhoneNumber() : null)
                .status(application.getStatus().name())
                .statusDisplayName(application.getStatus().getDisplayName())
                .currentOwnerId(application.getCurrentOwner() != null ? application.getCurrentOwner().getId() : null)
                .currentOwnerName(application.getCurrentOwner() != null ? application.getCurrentOwner().getName() : null)
                .currentOwnerRole(application.getCurrentOwner() != null ? application.getCurrentOwner().getRole().name() : null)
                .submissionDate(application.getSubmissionDate())
                .decisionDate(application.getDecisionDate())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .formData(formDataResponse)
                .eligibilityCriteria(criteriaResponses)
                .approvalHistory(historyResponses)
                .remarks(application.getRemarks())
                .build();
    }
}
