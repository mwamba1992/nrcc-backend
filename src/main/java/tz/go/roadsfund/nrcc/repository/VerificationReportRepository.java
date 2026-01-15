package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.VerificationAssignment;
import tz.go.roadsfund.nrcc.entity.VerificationReport;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for VerificationReport entity
 */
@Repository
public interface VerificationReportRepository extends JpaRepository<VerificationReport, Long> {

    Optional<VerificationReport> findByAssignment(VerificationAssignment assignment);

    Optional<VerificationReport> findByAssignmentId(Long assignmentId);

    @Query("SELECT vr FROM VerificationReport vr WHERE vr.assignment.application.id = :applicationId")
    List<VerificationReport> findByApplicationId(@Param("applicationId") Long applicationId);

    List<VerificationReport> findByIsFinal(Boolean isFinal);
}
