package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.District;
import tz.go.roadsfund.nrcc.entity.Region;

import java.util.List;
import java.util.Optional;

/**
 * Repository for District entity
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByCode(String code);

    List<District> findByRegion(Region region);

    List<District> findByRegionId(Long regionId);

    boolean existsByCode(String code);

    List<District> findByStatus(String status);

    Page<District> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
