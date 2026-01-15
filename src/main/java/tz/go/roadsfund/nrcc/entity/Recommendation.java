package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Recommendation entity from NRCC to Minister
 */
@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "recommendation_text", columnDefinition = "TEXT")
    private String recommendationText;

    @Column(length = 500)
    private String attachment;

    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_id")
    private User submittedBy;

    @Column(name = "recommendation_type", length = 50)
    private String recommendationType;
}
