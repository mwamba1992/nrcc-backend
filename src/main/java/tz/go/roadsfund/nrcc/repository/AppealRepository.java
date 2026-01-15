package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Appeal;
import tz.go.roadsfund.nrcc.entity.Application;
import tz.go.roadsfund.nrcc.entity.User;

import java.util.List;

/**
 * Repository interface for Appeal entity
 */
@Repository
public interface AppealRepository extends JpaRepository<Appeal, Long> {

    List<Appeal> findByApplication(Application application);

    List<Appeal> findByApplicationId(Long applicationId);

    List<Appeal> findByAppellant(User appellant);

    List<Appeal> findByStatus(String status);
}
