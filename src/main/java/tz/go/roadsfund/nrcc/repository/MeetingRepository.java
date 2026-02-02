package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Meeting;
import tz.go.roadsfund.nrcc.enums.MeetingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Optional<Meeting> findByMeetingNumber(String meetingNumber);

    List<Meeting> findByStatus(MeetingStatus status);

    Page<Meeting> findByStatus(MeetingStatus status, Pageable pageable);

    List<Meeting> findByMeetingDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT m FROM Meeting m WHERE m.meetingDate >= :date ORDER BY m.meetingDate ASC")
    List<Meeting> findUpcomingMeetings(@Param("date") LocalDate date);

    @Query("SELECT m FROM Meeting m JOIN m.applications a WHERE a.id = :applicationId")
    List<Meeting> findByApplicationId(@Param("applicationId") Long applicationId);

    Long countByStatus(MeetingStatus status);
}
