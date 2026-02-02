package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.ActionPlan;
import tz.go.roadsfund.nrcc.enums.ActionPlanStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ActionPlan entity
 */
@Repository
public interface ActionPlanRepository extends JpaRepository<ActionPlan, Long> {

    Optional<ActionPlan> findByFinancialYear(String financialYear);

    List<ActionPlan> findByStatus(ActionPlanStatus status);

    List<ActionPlan> findByPreparedById(Long userId);

    List<ActionPlan> findByApprovedById(Long userId);

    List<ActionPlan> findByFinancialYearContaining(String year);

    Long countByStatus(ActionPlanStatus status);
}
