package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.entity.TokenBlacklist;
import tz.go.roadsfund.nrcc.repository.TokenBlacklistRepository;

import java.time.LocalDateTime;

/**
 * Service for managing blacklisted tokens
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    /**
     * Add token to blacklist
     */
    public void blacklistToken(String token, Long userId, LocalDateTime expiresAt, String reason) {
        TokenBlacklist blacklistedToken = TokenBlacklist.builder()
                .token(token)
                .userId(userId)
                .blacklistedAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .reason(reason)
                .build();

        tokenBlacklistRepository.save(blacklistedToken);
        log.info("Token blacklisted for user: {} - Reason: {}", userId, reason);
    }

    /**
     * Check if token is blacklisted
     */
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    /**
     * Blacklist all tokens for a user (e.g., password change, account suspended)
     * Note: This only works for tokens that have been used and cached
     */
    public void blacklistAllUserTokens(Long userId) {
        tokenBlacklistRepository.deleteByUserId(userId);
        log.info("All tokens blacklisted for user: {}", userId);
    }

    /**
     * Clean up expired tokens (runs daily)
     */
    @Scheduled(cron = "0 0 2 * * ?") // 2 AM daily
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired blacklisted tokens");
        tokenBlacklistRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired tokens cleanup completed");
    }
}
