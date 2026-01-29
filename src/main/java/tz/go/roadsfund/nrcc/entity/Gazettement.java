package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.GazettementStatus;

import java.time.LocalDate;

/**
 * Gazettement entity for tracking official publication
 */
@Entity
@Table(name = "gazettements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gazettement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decision_id")
    private MinisterDecision decision;

    @Column(name = "gazette_number", length = 100)
    private String gazetteNumber;

    @Column(name = "gazette_date")
    private LocalDate gazetteDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private GazettementStatus status = GazettementStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_id")
    private User processedBy;
}
