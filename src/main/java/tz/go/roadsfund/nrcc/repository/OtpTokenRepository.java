package tz.go.roadsfund.nrcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.roadsfund.nrcc.entity.OtpToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findByPhoneNumberAndVerifiedFalseAndExpiresAtAfter(String phoneNumber, LocalDateTime now);

    Optional<OtpToken> findTopByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);

    @Modifying
    @Query("DELETE FROM OtpToken o WHERE o.expiresAt < :now")
    void deleteExpiredTokens(LocalDateTime now);

    @Modifying
    @Query("DELETE FROM OtpToken o WHERE o.phoneNumber = :phoneNumber")
    void deleteByPhoneNumber(String phoneNumber);

    long countByPhoneNumberAndCreatedAtAfter(String phoneNumber, LocalDateTime since);
}
