package tz.go.roadsfund.nrcc.dto.response;

import lombok.Builder;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class MeetingResponse {
    private Long id;
    private String meetingNumber;
    private String title;
    private String description;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private MeetingStatus status;
    private String chairpersonName;
    private String secretaryName;
    private Integer applicationCount;
    private Integer attendeeCount;
    private LocalDateTime createdAt;
}
