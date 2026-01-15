package tz.go.roadsfund.nrcc.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.dto.request.LoginRequest;
import tz.go.roadsfund.nrcc.dto.request.RegisterRequest;
import tz.go.roadsfund.nrcc.dto.response.AuthResponse;
import tz.go.roadsfund.nrcc.dto.response.UserResponse;
import tz.go.roadsfund.nrcc.entity.RefreshToken;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.UserRole;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.repository.UserRepository;
import tz.go.roadsfund.nrcc.security.JwtTokenProvider;

import java.time.LocalDateTime;

/**
 * Authentication service with enhanced token management
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address already in use");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole() != null ? request.getRole() : UserRole.PUBLIC_APPLICANT)
                .organization(request.getOrganization())
                .region(request.getRegion())
                .status("ACTIVE")
                .emailVerified(false)
                .phoneVerified(false)
                .build();

        userRepository.save(user);

        // Authenticate and generate tokens
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        UserResponse userResponse = mapToUserResponse(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Generate tokens with version
        Integer tokenVersion = user.getTokenVersion() != null ? user.getTokenVersion() : 0;
        String accessToken = tokenProvider.generateToken(authentication, tokenVersion);

        // Create refresh token in database
        String deviceInfo = getDeviceInfo(httpRequest);
        String ipAddress = getClientIpAddress(httpRequest);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo, ipAddress);

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserResponse userResponse = mapToUserResponse(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userResponse)
                .build();
    }

    public AuthResponse refreshAccessToken(String refreshTokenValue, HttpServletRequest httpRequest) {
        // Verify refresh token
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenValue);
        User user = refreshToken.getUser();

        // Create new authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                null
        );

        // Generate new access token with current version
        Integer tokenVersion = user.getTokenVersion() != null ? user.getTokenVersion() : 0;
        String accessToken = tokenProvider.generateToken(authentication, tokenVersion);

        // Optionally rotate refresh token for security
        refreshTokenService.revokeToken(refreshTokenValue);
        String deviceInfo = getDeviceInfo(httpRequest);
        String ipAddress = getClientIpAddress(httpRequest);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user, deviceInfo, ipAddress);

        UserResponse userResponse = mapToUserResponse(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .user(userResponse)
                .build();
    }

    public void logout(String accessToken, String refreshTokenValue) {
        // Blacklist the access token
        if (accessToken != null) {
            try {
                Long userId = tokenProvider.getUserIdFromToken(accessToken);
                LocalDateTime expiresAt = tokenProvider.getExpirationFromToken(accessToken)
                        .toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();

                tokenBlacklistService.blacklistToken(accessToken, userId, expiresAt, "User logout");
            } catch (Exception e) {
                // Token might be invalid, but still try to revoke refresh token
            }
        }

        // Revoke the refresh token
        if (refreshTokenValue != null) {
            try {
                refreshTokenService.revokeToken(refreshTokenValue);
            } catch (Exception e) {
                // Refresh token might not exist or already revoked
            }
        }
    }

    public void logoutAllDevices(Long userId) {
        // Increment token version to invalidate all access tokens
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        user.incrementTokenVersion();
        userRepository.save(user);

        // Revoke all refresh tokens
        refreshTokenService.revokeAllUserTokens(userId);
    }

    private String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown Device";
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .organization(user.getOrganization())
                .region(user.getRegion())
                .status(user.getStatus())
                .build();
    }
}
