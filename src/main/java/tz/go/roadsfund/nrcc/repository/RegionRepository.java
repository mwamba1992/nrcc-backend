package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Region;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Region entity
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByCode(String code);

    Optional<Region> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Region> findByStatus(String status);

    // Search by name (case-insensitive)
    List<Region> findByNameContainingIgnoreCase(String name);

    Page<Region> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Count by status
    long countByStatus(String status);
}
