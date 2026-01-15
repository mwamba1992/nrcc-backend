package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.Gazettement;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Gazettement entity
 */
@Repository
public interface GazettementRepository extends JpaRepository<Gazettement, Long> {

    Optional<Gazettement> findByApplication(Application application);

    Optional<Gazettement> findByApplicationId(Long applicationId);

    List<Gazettement> findByStatus(String status);

    Optional<Gazettement> findByGazetteNumber(String gazetteNumber);
}
