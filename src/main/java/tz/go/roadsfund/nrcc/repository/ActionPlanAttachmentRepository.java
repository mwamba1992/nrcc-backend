package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.ActionPlanAttachment;

import java.util.List;

/**
 * Repository interface for ActionPlanAttachment entity
 */
@Repository
public interface ActionPlanAttachmentRepository extends JpaRepository<ActionPlanAttachment, Long> {

    List<ActionPlanAttachment> findByTargetId(Long targetId);

    List<ActionPlanAttachment> findByActivityId(Long activityId);

    List<ActionPlanAttachment> findByUploadedById(Long userId);
}
