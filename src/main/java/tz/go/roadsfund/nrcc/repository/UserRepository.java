package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.District;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, String status);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByRoleAndStatus(UserRole role, String status);

    List<User> findByDistrict(District district);

    List<User> findByDistrict_Region_Id(Long regionId);

    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByEmailVerificationToken(String token);

    List<User> findByStatus(String status);

    // Search by name (case-insensitive)
    List<User> findByNameContainingIgnoreCase(String name);

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Search by name and status
    List<User> findByNameContainingIgnoreCaseAndStatus(String name, String status);

    // Search by email (partial match)
    List<User> findByEmailContainingIgnoreCase(String email);

    // Combined search
    @Query("SELECT u FROM User u WHERE " +
           "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(:organizationId IS NULL OR u.organization.id = :organizationId) AND " +
           "(:districtId IS NULL OR u.district.id = :districtId) AND " +
           "(:regionId IS NULL OR u.district.region.id = :regionId) AND " +
           "(:userType IS NULL OR u.userType = :userType)")
    Page<User> searchUsers(
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") UserRole role,
            @Param("status") String status,
            @Param("organizationId") Long organizationId,
            @Param("districtId") Long districtId,
            @Param("regionId") Long regionId,
            @Param("userType") String userType,
            Pageable pageable
    );

    // Bulk update status
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id IN :ids")
    int updateStatusByIdIn(@Param("ids") List<Long> ids, @Param("status") String status);

    // Count by organization
    long countByOrganization_Id(Long organizationId);

    // Count by role
    long countByRole(UserRole role);

    // Count by status
    long countByStatus(String status);
}
