package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Token Blacklist for revoked access tokens
 */
@Entity
@Table(name = "token_blacklist", indexes = {
        @Index(name = "idx_token", columnList = "token"),
        @Index(name = "idx_expires_at", columnList = "expires_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(length = 100)
    private String reason;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
