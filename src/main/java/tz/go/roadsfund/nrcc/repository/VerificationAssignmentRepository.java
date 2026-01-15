package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.entity.VerificationAssignment;

import java.util.List;

/**
 * Repository interface for VerificationAssignment entity
 */
@Repository
public interface VerificationAssignmentRepository extends JpaRepository<VerificationAssignment, Long> {

    List<VerificationAssignment> findByApplication(Application application);

    List<VerificationAssignment> findByApplicationId(Long applicationId);

    List<VerificationAssignment> findByMember(User member);

    List<VerificationAssignment> findByMemberId(Long memberId);

    List<VerificationAssignment> findByStatus(String status);

    List<VerificationAssignment> findByMemberAndStatus(User member, String status);
}
