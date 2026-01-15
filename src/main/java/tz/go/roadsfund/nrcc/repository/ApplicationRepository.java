package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.ApplicantType;
import tz.go.roadsfund.nrcc.enums.ApplicationStatus;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Application entity
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByApplicationNumber(String applicationNumber);

    List<Application> findByApplicant(User applicant);

    List<Application> findByStatus(ApplicationStatus status);

    List<Application> findByApplicantType(ApplicantType applicantType);

    List<Application> findByProposedClass(RoadClass proposedClass);

    List<Application> findByCurrentOwner(User currentOwner);

    Page<Application> findByStatus(ApplicationStatus status, Pageable pageable);

    Page<Application> findByApplicant(User applicant, Pageable pageable);

    @Query("SELECT a FROM Application a WHERE a.submissionDate BETWEEN :startDate AND :endDate")
    List<Application> findBySubmissionDateBetween(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.status = :status")
    Long countByStatus(@Param("status") ApplicationStatus status);

    @Query("SELECT a FROM Application a WHERE a.applicant.email = :email")
    List<Application> findByApplicantEmail(@Param("email") String email);

    @Query("SELECT a FROM Application a WHERE a.status IN :statuses")
    List<Application> findByStatusIn(@Param("statuses") List<ApplicationStatus> statuses);
}
