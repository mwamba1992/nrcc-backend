package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.EligibilityCriterion;

/**
 * Eligibility Criteria Selection entity
 */
@Entity
@Table(name = "eligibility_criteria_selections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityCriteriaSelection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(name = "criterion", nullable = false, length = 10)
    private EligibilityCriterion criterion;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "evidence_description", columnDefinition = "TEXT")
    private String evidenceDescription;

    @Column(name = "is_satisfied")
    @Builder.Default
    private Boolean isSatisfied = true;
}
