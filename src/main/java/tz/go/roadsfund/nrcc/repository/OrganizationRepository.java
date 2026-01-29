package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.Organization;

import java.util.Optional;

/**
 * Repository for Organization entity
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByCode(String code);

    Optional<Organization> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}
