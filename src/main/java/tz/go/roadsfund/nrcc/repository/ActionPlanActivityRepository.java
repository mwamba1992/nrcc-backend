package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.ActionPlanActivity;
import tz.go.roadsfund.nrcc.entity.ActionPlanTarget;
import tz.go.roadsfund.nrcc.enums.ActivityStatus;

import java.util.List;

/**
 * Repository interface for ActionPlanActivity entity
 */
@Repository
public interface ActionPlanActivityRepository extends JpaRepository<ActionPlanActivity, Long> {

    List<ActionPlanActivity> findByTarget(ActionPlanTarget target);

    List<ActionPlanActivity> findByTargetId(Long targetId);

    List<ActionPlanActivity> findByStatus(ActivityStatus status);

    List<ActionPlanActivity> findByTargetIdOrderByDisplayOrderAsc(Long targetId);
}
