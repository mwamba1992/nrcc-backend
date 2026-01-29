package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.AppealStatus;

import java.time.LocalDateTime;

/**
 * Appeal entity for refused applications
 */
@Entity
@Table(name = "appeals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appeal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decision_id")
    private MinisterDecision decision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appellant_id", nullable = false)
    private User appellant;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String grounds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private AppealStatus status = AppealStatus.SUBMITTED;

    @Column(name = "appeal_date")
    private LocalDateTime appealDate;

    @Column(name = "appeal_decision", columnDefinition = "TEXT")
    private String appealDecision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decided_by_id")
    private User decidedBy;

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;
}
