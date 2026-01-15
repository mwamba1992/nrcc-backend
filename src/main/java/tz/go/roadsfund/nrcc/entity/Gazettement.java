package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Column(name = "gazette_number", length = 100)
    private String gazetteNumber;

    @Column(name = "gazette_date")
    private LocalDate gazetteDate;

    @Column(nullable = false, length = 50)
    private String status = "PENDING";

    @Column(name = "publication_reference", length = 255)
    private String publicationReference;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
