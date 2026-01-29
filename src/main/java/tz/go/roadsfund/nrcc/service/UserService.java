package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.config.RolePermissionConfig;
import tz.go.roadsfund.nrcc.dto.request.*;
import tz.go.roadsfund.nrcc.dto.response.BulkActionResponse;
import tz.go.roadsfund.nrcc.dto.response.OrganizationResponse;
import tz.go.roadsfund.nrcc.dto.response.UserDetailsResponse;
import tz.go.roadsfund.nrcc.dto.response.UserResponse;
import tz.go.roadsfund.nrcc.entity.District;
import tz.go.roadsfund.nrcc.entity.Organization;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.UserRole;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.DistrictRepository;
import tz.go.roadsfund.nrcc.repository.OrganizationRepository;
import tz.go.roadsfund.nrcc.repository.UserRepository;
import tz.go.roadsfund.nrcc.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for user management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final DistrictRepository districtRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.reset-token-expiry-hours:24}")
    private int resetTokenExpiryHours;

    @Value("${app.verification-token-expiry-hours:48}")
    private int verificationTokenExpiryHours;

    public UserDetailsResponse createUser(CreateUserRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address already in use");
        }

        // Fetch organization if provided
        Organization organization = null;
        if (request.getOrganizationId() != null) {
            organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new BadRequestException("Organization not found"));
        }

        // Fetch district if provided
        District district = null;
        if (request.getDistrictId() != null) {
            district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new BadRequestException("District not found"));
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .organization(organization)
                .district(district)
                .userType(request.getUserType())
                .status("ACTIVE")
                .emailVerified(false)
                .phoneVerified(false)
                .build();

        user = userRepository.save(user);
        log.info("User created successfully: {}", user.getEmail());

        return mapToDetailsResponse(user);
    }

    @Transactional(readOnly = true)
    public UserDetailsResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDetailsResponse(user);
    }

    @Transactional(readOnly = true)
    public UserDetailsResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToDetailsResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserResponse);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRegion(Long regionId) {
        return userRepository.findByDistrict_Region_Id(regionId).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByDistrict(Long districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new BadRequestException("District not found"));
        return userRepository.findByDistrict(district).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByStatus(String status) {
        return userRepository.findByStatus(status).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserDetailsResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check if email is being changed and if it's already in use
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email address already in use");
            }
            user.setEmail(request.getEmail());
        }

        // Update fields if provided
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new BadRequestException("Organization not found"));
            user.setOrganization(organization);
        }
        if (request.getDistrictId() != null) {
            District district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new BadRequestException("District not found"));
            user.setDistrict(district);
        }
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        user = userRepository.save(user);
        log.info("User updated successfully: {}", user.getEmail());

        return mapToDetailsResponse(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Verify new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", user.getEmail());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
        log.info("User deleted successfully: {}", user.getEmail());
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setStatus("ACTIVE");
        userRepository.save(user);
        log.info("User activated: {}", user.getEmail());
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setStatus("INACTIVE");
        userRepository.save(user);
        log.info("User deactivated: {}", user.getEmail());
    }

    // ==================== PASSWORD RESET ====================

    public void requestPasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(resetTokenExpiryHours));
        userRepository.save(user);

        // Send reset email
        String resetLink = baseUrl + "/reset-password?token=" + resetToken;
        String subject = "NRCC - Password Reset Request";
        String body = String.format(
                "Dear %s,\n\n" +
                "You have requested to reset your password for the NRCC system.\n\n" +
                "Please click the following link to reset your password:\n%s\n\n" +
                "This link will expire in %d hours.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "NRCC System\n" +
                "Roads Fund Board",
                user.getName(), resetLink, resetTokenExpiryHours
        );

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
            log.info("Password reset email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", user.getEmail(), e);
            // Don't throw - we still saved the token, user can retry
        }
    }

    public void resetPassword(ResetPasswordRequest request) {
        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        // Find user by reset token
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        // Check if token is expired
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired. Please request a new password reset.");
        }

        // Update password and clear reset token
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        log.info("Password reset successful for user: {}", user.getEmail());
    }

    // ==================== EMAIL VERIFICATION ====================

    public void sendEmailVerification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getEmailVerified()) {
            throw new BadRequestException("Email is already verified");
        }

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        user.setEmailVerificationTokenExpiry(LocalDateTime.now().plusHours(verificationTokenExpiryHours));
        userRepository.save(user);

        // Send verification email
        String verificationLink = baseUrl + "/verify-email?token=" + verificationToken;
        String subject = "NRCC - Email Verification";
        String body = String.format(
                "Dear %s,\n\n" +
                "Thank you for registering with the NRCC system.\n\n" +
                "Please click the following link to verify your email address:\n%s\n\n" +
                "This link will expire in %d hours.\n\n" +
                "Best regards,\n" +
                "NRCC System\n" +
                "Roads Fund Board",
                user.getName(), verificationLink, verificationTokenExpiryHours
        );

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", user.getEmail(), e);
            throw new BadRequestException("Failed to send verification email. Please try again.");
        }
    }

    public void resendEmailVerification() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        sendEmailVerification(currentUserId);
    }

    public void verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmailVerificationToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired verification token"));

        // Check if token is expired
        if (user.getEmailVerificationTokenExpiry() == null ||
            user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token has expired. Please request a new verification email.");
        }

        // Mark email as verified and clear token
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);
        userRepository.save(user);

        log.info("Email verified for user: {}", user.getEmail());
    }

    // ==================== SEARCH & FILTER ====================

    @Transactional(readOnly = true)
    public List<UserResponse> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsersByName(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::mapToUserResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(UserSearchRequest searchRequest, Pageable pageable) {
        return userRepository.searchUsers(
                searchRequest.getName(),
                searchRequest.getEmail(),
                searchRequest.getRole(),
                searchRequest.getStatus(),
                searchRequest.getOrganizationId(),
                searchRequest.getDistrictId(),
                searchRequest.getRegionId(),
                searchRequest.getUserType(),
                pageable
        ).map(this::mapToUserResponse);
    }

    // ==================== BULK OPERATIONS ====================

    public BulkActionResponse performBulkAction(BulkUserActionRequest request) {
        List<Long> successIds = new ArrayList<>();
        List<BulkActionResponse.FailedItem> failedItems = new ArrayList<>();

        for (Long userId : request.getUserIds()) {
            try {
                switch (request.getAction()) {
                    case ACTIVATE:
                        activateUser(userId);
                        successIds.add(userId);
                        break;
                    case DEACTIVATE:
                        deactivateUser(userId);
                        successIds.add(userId);
                        break;
                    case DELETE:
                        deleteUser(userId);
                        successIds.add(userId);
                        break;
                }
            } catch (ResourceNotFoundException e) {
                failedItems.add(BulkActionResponse.FailedItem.builder()
                        .userId(userId)
                        .reason("User not found")
                        .build());
            } catch (Exception e) {
                failedItems.add(BulkActionResponse.FailedItem.builder()
                        .userId(userId)
                        .reason(e.getMessage())
                        .build());
            }
        }

        log.info("Bulk {} action completed: {} success, {} failed",
                request.getAction(), successIds.size(), failedItems.size());

        return BulkActionResponse.builder()
                .totalRequested(request.getUserIds().size())
                .successCount(successIds.size())
                .failedCount(failedItems.size())
                .successIds(successIds)
                .failedItems(failedItems)
                .build();
    }

    // ==================== CURRENT USER PROFILE ====================

    @Transactional(readOnly = true)
    public UserDetailsResponse getCurrentUserProfile() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return getUserById(currentUserId);
    }

    public UserDetailsResponse updateCurrentUserProfile(UpdateProfileRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUserId));

        // Update allowed fields only (users cannot change their own role, email, etc.)
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        user = userRepository.save(user);
        log.info("User profile updated: {}", user.getEmail());

        return mapToDetailsResponse(user);
    }

    public void changeCurrentUserPassword(ChangePasswordRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        changePassword(currentUserId, request);
    }

    // ==================== STATISTICS ====================

    @Transactional(readOnly = true)
    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    @Transactional(readOnly = true)
    public long countUsersByStatus(String status) {
        return userRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countUsersByOrganization(Long organizationId) {
        return userRepository.countByOrganization_Id(organizationId);
    }

    // Mapping methods
    private UserDetailsResponse mapToDetailsResponse(User user) {
        OrganizationResponse organizationResponse = null;
        if (user.getOrganization() != null) {
            Organization org = user.getOrganization();
            String districtName = org.getDistrict() != null ? org.getDistrict().getName() : null;
            String regionName = org.getDistrict() != null && org.getDistrict().getRegion() != null ?
                    org.getDistrict().getRegion().getName() : null;

            organizationResponse = OrganizationResponse.builder()
                    .id(org.getId())
                    .code(org.getCode())
                    .name(org.getName())
                    .organizationType(org.getOrganizationType())
                    .description(org.getDescription())
                    .contactPerson(org.getContactPerson())
                    .email(org.getEmail())
                    .phoneNumber(org.getPhoneNumber())
                    .address(org.getAddress())
                    .region(regionName)
                    .district(districtName)
                    .status(org.getStatus())
                    .build();
        }

        String districtName = user.getDistrict() != null ? user.getDistrict().getName() : null;
        String regionName = user.getDistrict() != null && user.getDistrict().getRegion() != null ?
                user.getDistrict().getRegion().getName() : null;

        return UserDetailsResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .organization(organizationResponse)
                .district(districtName)
                .region(regionName)
                .userType(user.getUserType())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .permissions(RolePermissionConfig.getPermissionStrings(user.getRole()))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        OrganizationResponse organizationResponse = null;
        if (user.getOrganization() != null) {
            Organization org = user.getOrganization();
            String districtName = org.getDistrict() != null ? org.getDistrict().getName() : null;
            String regionName = org.getDistrict() != null && org.getDistrict().getRegion() != null ?
                    org.getDistrict().getRegion().getName() : null;

            organizationResponse = OrganizationResponse.builder()
                    .id(org.getId())
                    .code(org.getCode())
                    .name(org.getName())
                    .organizationType(org.getOrganizationType())
                    .description(org.getDescription())
                    .contactPerson(org.getContactPerson())
                    .email(org.getEmail())
                    .phoneNumber(org.getPhoneNumber())
                    .address(org.getAddress())
                    .region(regionName)
                    .district(districtName)
                    .status(org.getStatus())
                    .build();
        }

        String districtName = user.getDistrict() != null ? user.getDistrict().getName() : null;
        String regionName = user.getDistrict() != null && user.getDistrict().getRegion() != null ?
                user.getDistrict().getRegion().getName() : null;

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .organization(organizationResponse)
                .district(districtName)
                .region(regionName)
                .userType(user.getUserType())
                .status(user.getStatus())
                .build();
    }
}
