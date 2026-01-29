package tz.go.roadsfund.nrcc.entity;

import jakarta.persistence.*;
import lombok.*;
import tz.go.roadsfund.nrcc.enums.UserRole;

/**
 * User entity representing all system users
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @Column(name = "user_type", length = 50)
    private String userType;

    @Column(length = 20)
    private String status = "ACTIVE";

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    @Column(name = "last_login")
    private java.time.LocalDateTime lastLogin;

    @Column(name = "reset_token", length = 500)
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private java.time.LocalDateTime resetTokenExpiry;

    @Column(name = "email_verification_token", length = 500)
    private String emailVerificationToken;

    @Column(name = "email_verification_token_expiry")
    private java.time.LocalDateTime emailVerificationTokenExpiry;

    @Column(name = "token_version")
    private Integer tokenVersion = 0;

    /**
     * Increment token version to invalidate all existing tokens
     */
    public void incrementTokenVersion() {
        this.tokenVersion = (this.tokenVersion == null ? 0 : this.tokenVersion) + 1;
    }
}
