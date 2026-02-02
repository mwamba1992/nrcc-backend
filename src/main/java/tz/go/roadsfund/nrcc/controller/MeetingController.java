package tz.go.roadsfund.nrcc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.request.CreateMeetingRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateMeetingRequest;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.MeetingDetailResponse;
import tz.go.roadsfund.nrcc.dto.response.MeetingResponse;
import tz.go.roadsfund.nrcc.enums.MeetingStatus;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.MeetingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
@Tag(name = "Meetings", description = "NRCC Meeting management endpoints")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    @RequirePermission(Permission.MEETING_CREATE)
    @Operation(summary = "Create a new meeting", description = "Schedule a new NRCC meeting")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> createMeeting(
            @Valid @RequestBody CreateMeetingRequest request) {
        MeetingDetailResponse meeting = meetingService.createMeeting(request);
        return ResponseEntity.ok(ApiResponse.success("Meeting created successfully", meeting));
    }

    @GetMapping
    @RequirePermission(Permission.MEETING_LIST)
    @Operation(summary = "Get all meetings", description = "Retrieve paginated list of all meetings")
    public ResponseEntity<ApiResponse<Page<MeetingResponse>>> getAllMeetings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "meetingDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MeetingResponse> meetings = meetingService.getAllMeetings(pageable);
        return ResponseEntity.ok(ApiResponse.success("Meetings retrieved", meetings));
    }

    @GetMapping("/status/{status}")
    @RequirePermission(Permission.MEETING_LIST)
    @Operation(summary = "Get meetings by status", description = "Retrieve meetings filtered by status")
    public ResponseEntity<ApiResponse<Page<MeetingResponse>>> getMeetingsByStatus(
            @PathVariable MeetingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("meetingDate").descending());
        Page<MeetingResponse> meetings = meetingService.getMeetingsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Meetings retrieved", meetings));
    }

    @GetMapping("/upcoming")
    @RequirePermission(Permission.MEETING_LIST)
    @Operation(summary = "Get upcoming meetings", description = "Retrieve list of upcoming scheduled meetings")
    public ResponseEntity<ApiResponse<List<MeetingResponse>>> getUpcomingMeetings() {
        List<MeetingResponse> meetings = meetingService.getUpcomingMeetings();
        return ResponseEntity.ok(ApiResponse.success("Upcoming meetings retrieved", meetings));
    }

    @GetMapping("/{id}")
    @RequirePermission(Permission.MEETING_READ)
    @Operation(summary = "Get meeting by ID", description = "Retrieve detailed information about a specific meeting")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> getMeeting(@PathVariable Long id) {
        MeetingDetailResponse meeting = meetingService.getMeeting(id);
        return ResponseEntity.ok(ApiResponse.success("Meeting retrieved", meeting));
    }

    @PutMapping("/{id}")
    @RequirePermission(Permission.MEETING_UPDATE)
    @Operation(summary = "Update meeting", description = "Update meeting details")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> updateMeeting(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMeetingRequest request) {
        MeetingDetailResponse meeting = meetingService.updateMeeting(id, request);
        return ResponseEntity.ok(ApiResponse.success("Meeting updated successfully", meeting));
    }

    @PostMapping("/{id}/start")
    @RequirePermission(Permission.MEETING_CONDUCT)
    @Operation(summary = "Start meeting", description = "Change meeting status to IN_PROGRESS")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> startMeeting(@PathVariable Long id) {
        MeetingDetailResponse meeting = meetingService.startMeeting(id);
        return ResponseEntity.ok(ApiResponse.success("Meeting started", meeting));
    }

    @PostMapping("/{id}/complete")
    @RequirePermission(Permission.MEETING_CONDUCT)
    @Operation(summary = "Complete meeting", description = "Complete meeting with minutes and resolution")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> completeMeeting(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String minutes = payload.get("minutes");
        String resolution = payload.get("resolution");
        MeetingDetailResponse meeting = meetingService.completeMeeting(id, minutes, resolution);
        return ResponseEntity.ok(ApiResponse.success("Meeting completed", meeting));
    }

    @PostMapping("/{id}/cancel")
    @RequirePermission(Permission.MEETING_UPDATE)
    @Operation(summary = "Cancel meeting", description = "Cancel a scheduled meeting")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> cancelMeeting(@PathVariable Long id) {
        MeetingDetailResponse meeting = meetingService.cancelMeeting(id);
        return ResponseEntity.ok(ApiResponse.success("Meeting cancelled", meeting));
    }

    @PostMapping("/{meetingId}/applications/{applicationId}")
    @RequirePermission(Permission.MEETING_UPDATE)
    @Operation(summary = "Add application to meeting", description = "Add an application to be discussed in the meeting")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> addApplicationToMeeting(
            @PathVariable Long meetingId,
            @PathVariable Long applicationId) {
        MeetingDetailResponse meeting = meetingService.addApplicationToMeeting(meetingId, applicationId);
        return ResponseEntity.ok(ApiResponse.success("Application added to meeting", meeting));
    }

    @DeleteMapping("/{meetingId}/applications/{applicationId}")
    @RequirePermission(Permission.MEETING_UPDATE)
    @Operation(summary = "Remove application from meeting", description = "Remove an application from the meeting agenda")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> removeApplicationFromMeeting(
            @PathVariable Long meetingId,
            @PathVariable Long applicationId) {
        MeetingDetailResponse meeting = meetingService.removeApplicationFromMeeting(meetingId, applicationId);
        return ResponseEntity.ok(ApiResponse.success("Application removed from meeting", meeting));
    }

    @PostMapping("/{meetingId}/attendees/{userId}")
    @RequirePermission(Permission.MEETING_UPDATE)
    @Operation(summary = "Add attendee", description = "Add an attendee to the meeting")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> addAttendee(
            @PathVariable Long meetingId,
            @PathVariable Long userId) {
        MeetingDetailResponse meeting = meetingService.addAttendee(meetingId, userId);
        return ResponseEntity.ok(ApiResponse.success("Attendee added", meeting));
    }

    @DeleteMapping("/{meetingId}/attendees/{userId}")
    @RequirePermission(Permission.MEETING_UPDATE)
    @Operation(summary = "Remove attendee", description = "Remove an attendee from the meeting")
    public ResponseEntity<ApiResponse<MeetingDetailResponse>> removeAttendee(
            @PathVariable Long meetingId,
            @PathVariable Long userId) {
        MeetingDetailResponse meeting = meetingService.removeAttendee(meetingId, userId);
        return ResponseEntity.ok(ApiResponse.success("Attendee removed", meeting));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(Permission.MEETING_DELETE)
    @Operation(summary = "Delete meeting", description = "Delete a meeting (only if not completed)")
    public ResponseEntity<ApiResponse<Void>> deleteMeeting(@PathVariable Long id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok(ApiResponse.success("Meeting deleted", null));
    }
}
