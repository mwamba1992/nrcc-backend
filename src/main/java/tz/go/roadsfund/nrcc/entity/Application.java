package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.ApplicantType;
import tz.go.roadsfund.nrcc.enums.ApplicationStatus;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Application entity for road reclassification requests
 */
@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_number", unique = true, nullable = false, length = 50)
    private String applicationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicant_type", nullable = false, length = 50)
    private ApplicantType applicantType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @Enumerated(EnumType.STRING)
    @Column(name = "proposed_class", nullable = false, length = 20)
    private RoadClass proposedClass;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_owner_id")
    private User currentOwner;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private ApplicationFormData formData;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EligibilityCriteriaSelection> eligibilityCriteria = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApprovalAction> approvalActions = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VerificationAssignment> verificationAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @Column(name = "decision_date")
    private LocalDate decisionDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;
}
