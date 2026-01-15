package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.ActionPlanActivity;
import tz.go.roadsfund.nrcc.entity.ActionPlanCostItem;

import java.util.List;

/**
 * Repository interface for ActionPlanCostItem entity
 */
@Repository
public interface ActionPlanCostItemRepository extends JpaRepository<ActionPlanCostItem, Long> {

    List<ActionPlanCostItem> findByActivity(ActionPlanActivity activity);

    List<ActionPlanCostItem> findByActivityId(Long activityId);
}
