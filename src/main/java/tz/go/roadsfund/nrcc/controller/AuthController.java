package tz.go.roadsfund.nrcc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.request.*;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.AuthResponse;
import tz.go.roadsfund.nrcc.dto.response.UserDetailsResponse;
import tz.go.roadsfund.nrcc.service.AuthService;
import tz.go.roadsfund.nrcc.service.UserService;
import tz.go.roadsfund.nrcc.util.SecurityUtil;

/**
 * Authentication REST controller with enhanced token management
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authService.refreshAccessToken(request.getRefreshToken(), httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody(required = false) RefreshTokenRequest refreshTokenRequest) {

        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }

        String refreshToken = refreshTokenRequest != null ? refreshTokenRequest.getRefreshToken() : null;

        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAllDevices() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("User not authenticated"));
        }

        authService.logoutAllDevices(userId);
        return ResponseEntity.ok(ApiResponse.success("Logged out from all devices successfully", null));
    }

    // ==================== PASSWORD RESET ====================

    /**
     * Request password reset (forgot password)
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        userService.requestPasswordReset(request);
        return ResponseEntity.ok(ApiResponse.success(
                "If the email exists, a password reset link has been sent", null));
    }

    /**
     * Reset password with token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
    }

    // ==================== EMAIL VERIFICATION ====================

    /**
     * Verify email with token
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request) {
        userService.verifyEmail(request);
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully", null));
    }

    /**
     * Resend email verification (for authenticated users)
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerification() {
        userService.resendEmailVerification();
        return ResponseEntity.ok(ApiResponse.success("Verification email sent", null));
    }

    // ==================== CURRENT USER PROFILE ====================

    /**
     * Get current user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getCurrentUser() {
        UserDetailsResponse user = userService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", user));
    }

    /**
     * Update current user's profile
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateCurrentUser(
            @Valid @RequestBody UpdateProfileRequest request) {
        UserDetailsResponse user = userService.updateCurrentUserProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", user));
    }

    /**
     * Change current user's password
     */
    @PutMapping("/me/change-password")
    public ResponseEntity<ApiResponse<Void>> changeCurrentUserPassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changeCurrentUserPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }
}
