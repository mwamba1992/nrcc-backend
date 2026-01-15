package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.EligibilityCriteriaSelection;
import tz.go.roadsfund.nrcc.enums.EligibilityCriterion;

import java.util.List;

/**
 * Repository interface for EligibilityCriteriaSelection entity
 */
@Repository
public interface EligibilityCriteriaSelectionRepository extends JpaRepository<EligibilityCriteriaSelection, Long> {

    List<EligibilityCriteriaSelection> findByApplication(Application application);

    List<EligibilityCriteriaSelection> findByApplicationId(Long applicationId);

    List<EligibilityCriteriaSelection> findByCriterionCode(EligibilityCriterion criterionCode);

    List<EligibilityCriteriaSelection> findByApplicationAndIsSatisfied(Application application, Boolean isSatisfied);
}
