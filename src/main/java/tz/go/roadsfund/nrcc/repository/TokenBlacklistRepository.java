package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.TokenBlacklist;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for TokenBlacklist entity
 */
@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    Optional<TokenBlacklist> findByToken(String token);

    boolean existsByToken(String token);

    @Modifying
    @Query("DELETE FROM TokenBlacklist tb WHERE tb.expiresAt < :now")
    void deleteExpiredTokens(LocalDateTime now);

    @Modifying
    @Query("DELETE FROM TokenBlacklist tb WHERE tb.userId = :userId")
    void deleteByUserId(Long userId);
}
