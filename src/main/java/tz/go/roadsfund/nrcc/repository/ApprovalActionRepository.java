package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.ApprovalAction;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.enums.WorkflowAction;

import java.util.List;

/**
 * Repository interface for ApprovalAction entity
 */
@Repository
public interface ApprovalActionRepository extends JpaRepository<ApprovalAction, Long> {

    List<ApprovalAction> findByApplication(Application application);

    List<ApprovalAction> findByApplicationOrderByActionDateDesc(Application application);

    List<ApprovalAction> findByApplicationIdOrderByActionDateDesc(Long applicationId);

    @Query("SELECT aa FROM ApprovalAction aa WHERE aa.application.id = :applicationId ORDER BY aa.actionDate DESC")
    List<ApprovalAction> findApplicationHistory(@Param("applicationId") Long applicationId);

    List<ApprovalAction> findByActorId(Long actorId);

    List<ApprovalAction> findByAction(WorkflowAction action);
}
