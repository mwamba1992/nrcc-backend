package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.entity.RefreshToken;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.UnauthorizedException;
import tz.go.roadsfund.nrcc.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for managing refresh tokens
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    /**
     * Create new refresh token
     */
    public RefreshToken createRefreshToken(User user, String deviceInfo, String ipAddress) {
        // Generate unique token
        String tokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .isRevoked(false)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for user: {}", user.getEmail());

        return refreshToken;
    }

    /**
     * Find refresh token by token value
     */
    @Transactional(readOnly = true)
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
    }

    /**
     * Verify refresh token
     */
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);

        if (refreshToken.getIsRevoked()) {
            throw new UnauthorizedException("Refresh token has been revoked");
        }

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token has expired");
        }

        return refreshToken;
    }

    /**
     * Revoke refresh token
     */
    public void revokeToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        refreshToken.setIsRevoked(true);
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token revoked");
    }

    /**
     * Revoke all refresh tokens for a user
     */
    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("All refresh tokens revoked for user: {}", userId);
    }

    /**
     * Delete all refresh tokens for a user
     */
    public void deleteAllUserTokens(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        log.info("All refresh tokens deleted for user: {}", userId);
    }

    /**
     * Clean up expired tokens (runs daily)
     */
    @Scheduled(cron = "0 0 3 * * ?") // 3 AM daily
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired refresh tokens cleanup completed");
    }
}
