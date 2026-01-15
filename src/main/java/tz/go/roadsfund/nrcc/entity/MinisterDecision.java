package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Column(nullable = false, length = 50)
    private String decision;

    @Column(name = "disapproval_type", length = 50)
    private String disapprovalType;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "decision_date", nullable = false)
    private LocalDate decisionDate;

    @Column(name = "decision_reference", length = 100)
    private String decisionReference;

    @Column(columnDefinition = "TEXT")
    private String comments;
}
