package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.CreateMeetingRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateMeetingRequest;
import tz.go.roadsfund.nrcc.dto.response.MeetingDetailResponse;
import tz.go.roadsfund.nrcc.dto.response.MeetingResponse;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.Meeting;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.MeetingStatus;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.ApplicationRepository;
import tz.go.roadsfund.nrcc.repository.MeetingRepository;
import tz.go.roadsfund.nrcc.repository.UserRepository;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public MeetingDetailResponse createMeeting(CreateMeetingRequest request) {
        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .meetingDate(request.getMeetingDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .venue(request.getVenue())
                .agenda(request.getAgenda())
                .status(MeetingStatus.SCHEDULED)
                .meetingNumber(generateMeetingNumber())
                .build();

        if (request.getChairpersonId() != null) {
            User chairperson = userRepository.findById(request.getChairpersonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chairperson not found"));
            meeting.setChairperson(chairperson);
        }

        if (request.getSecretaryId() != null) {
            User secretary = userRepository.findById(request.getSecretaryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Secretary not found"));
            meeting.setSecretary(secretary);
        }

        if (request.getApplicationIds() != null && !request.getApplicationIds().isEmpty()) {
            List<Application> applications = applicationRepository.findAllById(request.getApplicationIds());
            meeting.setApplications(new HashSet<>(applications));
        }

        meeting = meetingRepository.save(meeting);
        log.info("Meeting created: {}", meeting.getMeetingNumber());

        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse updateMeeting(Long id, UpdateMeetingRequest request) {
        Meeting meeting = getMeetingById(id);

        if (request.getTitle() != null) {
            meeting.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            meeting.setDescription(request.getDescription());
        }
        if (request.getMeetingDate() != null) {
            meeting.setMeetingDate(request.getMeetingDate());
        }
        if (request.getStartTime() != null) {
            meeting.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            meeting.setEndTime(request.getEndTime());
        }
        if (request.getVenue() != null) {
            meeting.setVenue(request.getVenue());
        }
        if (request.getStatus() != null) {
            meeting.setStatus(request.getStatus());
        }
        if (request.getAgenda() != null) {
            meeting.setAgenda(request.getAgenda());
        }
        if (request.getMinutes() != null) {
            meeting.setMinutes(request.getMinutes());
        }
        if (request.getResolution() != null) {
            meeting.setResolution(request.getResolution());
        }
        if (request.getChairpersonId() != null) {
            User chairperson = userRepository.findById(request.getChairpersonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chairperson not found"));
            meeting.setChairperson(chairperson);
        }
        if (request.getSecretaryId() != null) {
            User secretary = userRepository.findById(request.getSecretaryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Secretary not found"));
            meeting.setSecretary(secretary);
        }
        if (request.getApplicationIds() != null) {
            List<Application> applications = applicationRepository.findAllById(request.getApplicationIds());
            meeting.setApplications(new HashSet<>(applications));
        }

        meeting = meetingRepository.save(meeting);
        log.info("Meeting updated: {}", meeting.getMeetingNumber());

        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse getMeeting(Long id) {
        Meeting meeting = getMeetingById(id);
        return mapToDetailResponse(meeting);
    }

    @Transactional(readOnly = true)
    public Page<MeetingResponse> getAllMeetings(Pageable pageable) {
        return meetingRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<MeetingResponse> getMeetingsByStatus(MeetingStatus status, Pageable pageable) {
        return meetingRepository.findByStatus(status, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getUpcomingMeetings() {
        return meetingRepository.findUpcomingMeetings(LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MeetingDetailResponse startMeeting(Long id) {
        Meeting meeting = getMeetingById(id);
        if (meeting.getStatus() != MeetingStatus.SCHEDULED) {
            throw new BadRequestException("Only scheduled meetings can be started");
        }
        meeting.setStatus(MeetingStatus.IN_PROGRESS);
        meeting = meetingRepository.save(meeting);
        log.info("Meeting started: {}", meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse completeMeeting(Long id, String minutes, String resolution) {
        Meeting meeting = getMeetingById(id);
        if (meeting.getStatus() != MeetingStatus.IN_PROGRESS) {
            throw new BadRequestException("Only in-progress meetings can be completed");
        }
        meeting.setStatus(MeetingStatus.COMPLETED);
        meeting.setMinutes(minutes);
        meeting.setResolution(resolution);
        meeting = meetingRepository.save(meeting);
        log.info("Meeting completed: {}", meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse cancelMeeting(Long id) {
        Meeting meeting = getMeetingById(id);
        if (meeting.getStatus() == MeetingStatus.COMPLETED) {
            throw new BadRequestException("Completed meetings cannot be cancelled");
        }
        meeting.setStatus(MeetingStatus.CANCELLED);
        meeting = meetingRepository.save(meeting);
        log.info("Meeting cancelled: {}", meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse addApplicationToMeeting(Long meetingId, Long applicationId) {
        Meeting meeting = getMeetingById(meetingId);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        meeting.getApplications().add(application);
        meeting = meetingRepository.save(meeting);
        log.info("Application {} added to meeting {}", applicationId, meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse removeApplicationFromMeeting(Long meetingId, Long applicationId) {
        Meeting meeting = getMeetingById(meetingId);
        meeting.getApplications().removeIf(app -> app.getId().equals(applicationId));
        meeting = meetingRepository.save(meeting);
        log.info("Application {} removed from meeting {}", applicationId, meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse addAttendee(Long meetingId, Long userId) {
        Meeting meeting = getMeetingById(meetingId);
        User attendee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        meeting.getAttendees().add(attendee);
        meeting = meetingRepository.save(meeting);
        log.info("Attendee {} added to meeting {}", userId, meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public MeetingDetailResponse removeAttendee(Long meetingId, Long userId) {
        Meeting meeting = getMeetingById(meetingId);
        meeting.getAttendees().removeIf(user -> user.getId().equals(userId));
        meeting = meetingRepository.save(meeting);
        log.info("Attendee {} removed from meeting {}", userId, meeting.getMeetingNumber());
        return mapToDetailResponse(meeting);
    }

    public void deleteMeeting(Long id) {
        Meeting meeting = getMeetingById(id);
        if (meeting.getStatus() == MeetingStatus.COMPLETED) {
            throw new BadRequestException("Completed meetings cannot be deleted");
        }
        meetingRepository.delete(meeting);
        log.info("Meeting deleted: {}", meeting.getMeetingNumber());
    }

    private Meeting getMeetingById(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with id: " + id));
    }

    private String generateMeetingNumber() {
        long count = meetingRepository.count() + 1;
        return String.format("NRCC-MTG-%d-%04d", Year.now().getValue(), count);
    }

    private MeetingResponse mapToResponse(Meeting meeting) {
        return MeetingResponse.builder()
                .id(meeting.getId())
                .meetingNumber(meeting.getMeetingNumber())
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .meetingDate(meeting.getMeetingDate())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .venue(meeting.getVenue())
                .status(meeting.getStatus())
                .chairpersonName(meeting.getChairperson() != null ? meeting.getChairperson().getName() : null)
                .secretaryName(meeting.getSecretary() != null ? meeting.getSecretary().getName() : null)
                .applicationCount(meeting.getApplications() != null ? meeting.getApplications().size() : 0)
                .attendeeCount(meeting.getAttendees() != null ? meeting.getAttendees().size() : 0)
                .createdAt(meeting.getCreatedAt())
                .build();
    }

    private MeetingDetailResponse mapToDetailResponse(Meeting meeting) {
        List<MeetingDetailResponse.ApplicationSummary> applicationSummaries = meeting.getApplications().stream()
                .map(app -> MeetingDetailResponse.ApplicationSummary.builder()
                        .id(app.getId())
                        .applicationNumber(app.getApplicationNumber())
                        .roadName(app.getFormData() != null ? app.getFormData().getRoadName() : null)
                        .applicantName(app.getApplicant() != null ? app.getApplicant().getName() : null)
                        .status(app.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        List<MeetingDetailResponse.AttendeeSummary> attendeeSummaries = meeting.getAttendees().stream()
                .map(user -> MeetingDetailResponse.AttendeeSummary.builder()
                        .id(user.getId())
                        .fullName(user.getName())
                        .role(user.getRole().name())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());

        return MeetingDetailResponse.builder()
                .id(meeting.getId())
                .meetingNumber(meeting.getMeetingNumber())
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .meetingDate(meeting.getMeetingDate())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .venue(meeting.getVenue())
                .status(meeting.getStatus())
                .chairpersonId(meeting.getChairperson() != null ? meeting.getChairperson().getId() : null)
                .chairpersonName(meeting.getChairperson() != null ? meeting.getChairperson().getName() : null)
                .secretaryId(meeting.getSecretary() != null ? meeting.getSecretary().getId() : null)
                .secretaryName(meeting.getSecretary() != null ? meeting.getSecretary().getName() : null)
                .agenda(meeting.getAgenda())
                .minutes(meeting.getMinutes())
                .resolution(meeting.getResolution())
                .applications(applicationSummaries)
                .attendees(attendeeSummaries)
                .createdAt(meeting.getCreatedAt())
                .updatedAt(meeting.getUpdatedAt())
                .build();
    }
}
