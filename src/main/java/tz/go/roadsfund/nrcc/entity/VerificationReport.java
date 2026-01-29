package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Verification Report entity
 */
@Entity
@Table(name = "verification_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private VerificationAssignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String findings;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column
    @Builder.Default
    private Integer version = 1;

    @Column(name = "is_final")
    @Builder.Default
    private Boolean isFinal = false;
}
