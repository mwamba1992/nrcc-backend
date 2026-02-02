package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tz.go.roadsfund.nrcc.enums.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Schema(description = "Request to update a meeting")
public class UpdateMeetingRequest {

    @Schema(description = "Meeting title", example = "NRCC Review Meeting Q1 2024 - Updated")
    private String title;

    @Schema(description = "Meeting description")
    private String description;

    @Schema(description = "Date of the meeting", example = "2024-03-20")
    private LocalDate meetingDate;

    @Schema(description = "Start time", example = "10:00")
    private LocalTime startTime;

    @Schema(description = "End time", example = "13:00")
    private LocalTime endTime;

    @Schema(description = "Meeting venue")
    private String venue;

    @Schema(description = "Meeting status", example = "IN_PROGRESS")
    private MeetingStatus status;

    @Schema(description = "ID of the meeting chairperson")
    private Long chairpersonId;

    @Schema(description = "ID of the meeting secretary")
    private Long secretaryId;

    @Schema(description = "Application IDs to be discussed")
    private Set<Long> applicationIds;

    @Schema(description = "Meeting agenda")
    private String agenda;

    @Schema(description = "Meeting minutes")
    private String minutes;

    @Schema(description = "Meeting resolution")
    private String resolution;
}
