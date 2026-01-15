package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.NRCCMeeting;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for NRCCMeeting entity
 */
@Repository
public interface NRCCMeetingRepository extends JpaRepository<NRCCMeeting, Long> {

    List<NRCCMeeting> findByApplication(Application application);

    List<NRCCMeeting> findByApplicationId(Long applicationId);

    List<NRCCMeeting> findByMeetingDate(LocalDate meetingDate);

    List<NRCCMeeting> findByMeetingDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<NRCCMeeting> findByMeetingNumber(String meetingNumber);
}
