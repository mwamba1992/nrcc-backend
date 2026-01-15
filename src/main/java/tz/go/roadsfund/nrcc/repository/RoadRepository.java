package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Road;
import tz.go.roadsfund.nrcc.enums.RoadClass;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Road entity
 */
@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {

    List<Road> findByCurrentClass(RoadClass currentClass);

    List<Road> findByRegion(String region);

    List<Road> findByRegionAndDistrict(String region, String district);

    Optional<Road> findByRoadNumber(String roadNumber);

    List<Road> findByNameContainingIgnoreCase(String name);

    List<Road> findByStatus(String status);
}
