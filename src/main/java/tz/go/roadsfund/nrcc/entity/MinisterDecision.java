package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.DecisionType;
import tz.go.roadsfund.nrcc.enums.DisapprovalType;

import java.time.LocalDateTime;

/**
 * Minister Decision entity
 */
@Entity
@Table(name = "minister_decisions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinisterDecision extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DecisionType decision;

    @Enumerated(EnumType.STRING)
    @Column(name = "disapproval_type", length = 50)
    private DisapprovalType disapprovalType;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decided_by_id")
    private User decidedBy;

    @Column(name = "decision_date", nullable = false)
    private LocalDateTime decisionDate;
}
