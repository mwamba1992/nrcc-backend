package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.ApplicationStatus;
import tz.go.roadsfund.nrcc.enums.WorkflowAction;

import java.time.LocalDateTime;

/**
 * Approval Action entity for tracking workflow actions (Audit Trail)
 */
@Entity
@Table(name = "approval_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalAction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private WorkflowAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 50)
    private ApplicationStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", length = 50)
    private ApplicationStatus toStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    @Column(name = "actor_role", length = 50)
    private String actorRole;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;
}
