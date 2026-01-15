package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.ActionPlan;
import tz.go.roadsfund.nrcc.entity.ActionPlanTarget;

import java.util.List;

/**
 * Repository interface for ActionPlanTarget entity
 */
@Repository
public interface ActionPlanTargetRepository extends JpaRepository<ActionPlanTarget, Long> {

    List<ActionPlanTarget> findByActionPlan(ActionPlan actionPlan);

    List<ActionPlanTarget> findByActionPlanId(Long actionPlanId);

    List<ActionPlanTarget> findByActionPlanIdOrderByDisplayOrderAsc(Long actionPlanId);
}
