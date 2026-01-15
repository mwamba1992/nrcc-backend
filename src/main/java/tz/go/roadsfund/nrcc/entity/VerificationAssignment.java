package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Verification Assignment entity
 */
@Entity
@Table(name = "verification_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationAssignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(nullable = false, length = 50)
    private String status = "PENDING";

    @OneToOne(mappedBy = "assignment", cascade = CascadeType.ALL)
    private VerificationReport report;
}
