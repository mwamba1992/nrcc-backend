package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.ApplicationFormData;

import java.util.Optional;

/**
 * Repository interface for ApplicationFormData entity
 */
@Repository
public interface ApplicationFormDataRepository extends JpaRepository<ApplicationFormData, Long> {

    Optional<ApplicationFormData> findByApplication(Application application);

    Optional<ApplicationFormData> findByApplicationId(Long applicationId);
}
