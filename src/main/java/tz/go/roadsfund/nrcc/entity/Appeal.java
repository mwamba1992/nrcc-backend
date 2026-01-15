package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @JoinColumn(name = "appellant_id", nullable = false)
    private User appellant;

    @Column(columnDefinition = "TEXT")
    private String grounds;

    @Column(columnDefinition = "TEXT")
    private String attachments;

    @Column(nullable = false, length = 50)
    private String status = "SUBMITTED";

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(columnDefinition = "TEXT")
    private String decision;

    @Column(name = "decision_date")
    private LocalDate decisionDate;
}
