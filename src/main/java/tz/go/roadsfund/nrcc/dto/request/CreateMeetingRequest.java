package tz.go.roadsfund.nrcc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Schema(description = "Request to create a new NRCC meeting")
public class CreateMeetingRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "Meeting title", example = "NRCC Review Meeting Q1 2024")
    private String title;

    @Schema(description = "Meeting description", example = "Quarterly meeting to review road reclassification applications")
    private String description;

    @NotNull(message = "Meeting date is required")
    @Schema(description = "Date of the meeting", example = "2024-03-15")
    private LocalDate meetingDate;

    @Schema(description = "Start time of the meeting", example = "09:00")
    private LocalTime startTime;

    @Schema(description = "End time of the meeting", example = "12:00")
    private LocalTime endTime;

    @Schema(description = "Meeting venue", example = "NRCC Conference Room, Dar es Salaam")
    private String venue;

    @Schema(description = "ID of the meeting chairperson")
    private Long chairpersonId;

    @Schema(description = "ID of the meeting secretary")
    private Long secretaryId;

    @Schema(description = "Application IDs to be discussed in this meeting")
    private Set<Long> applicationIds;

    @Schema(description = "Meeting agenda", example = "1. Opening remarks\n2. Review of applications\n3. Voting\n4. AOB")
    private String agenda;
}
