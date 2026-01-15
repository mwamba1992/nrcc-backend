package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * NRCC Meeting entity
 */
@Entity
@Table(name = "nrcc_meetings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NRCCMeeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(columnDefinition = "TEXT")
    private String attendees;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "minutes_attachment", length = 500)
    private String minutesAttachment;

    @Column(name = "meeting_number", length = 50)
    private String meetingNumber;
}
