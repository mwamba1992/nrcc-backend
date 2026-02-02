package tz.go.roadsfund.nrcc.dto.response;

import lombok.Builder;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class MeetingDetailResponse {
    private Long id;
    private String meetingNumber;
    private String title;
    private String description;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private MeetingStatus status;
    private Long chairpersonId;
    private String chairpersonName;
    private Long secretaryId;
    private String secretaryName;
    private String agenda;
    private String minutes;
    private String resolution;
    private List<ApplicationSummary> applications;
    private List<AttendeeSummary> attendees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class ApplicationSummary {
        private Long id;
        private String applicationNumber;
        private String roadName;
        private String applicantName;
        private String status;
    }

    @Data
    @Builder
    public static class AttendeeSummary {
        private Long id;
        private String fullName;
        private String role;
        private String email;
    }
}
