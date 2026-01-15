package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.MinisterDecision;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MinisterDecision entity
 */
@Repository
public interface MinisterDecisionRepository extends JpaRepository<MinisterDecision, Long> {

    Optional<MinisterDecision> findByApplication(Application application);

    Optional<MinisterDecision> findByApplicationId(Long applicationId);

    List<MinisterDecision> findByDecision(String decision);

    List<MinisterDecision> findByDisapprovalType(String disapprovalType);
}
