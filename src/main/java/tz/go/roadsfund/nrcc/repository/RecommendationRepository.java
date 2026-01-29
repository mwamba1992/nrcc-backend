package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.Recommendation;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Recommendation entity
 */
@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Optional<Recommendation> findByApplication(Application application);

    Optional<Recommendation> findByApplicationId(Long applicationId);

    List<Recommendation> findBySubmittedById(Long userId);
}
