package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.config.RolePermissionConfig;
import tz.go.roadsfund.nrcc.dto.request.ChangePasswordRequest;
import tz.go.roadsfund.nrcc.dto.request.CreateUserRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateUserRequest;
import tz.go.roadsfund.nrcc.dto.response.UserDetailsResponse;
import tz.go.roadsfund.nrcc.dto.response.UserResponse;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.enums.UserRole;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.exception.ResourceNotFoundException;
import tz.go.roadsfund.nrcc.repository.UserRepository;

import java.util.List;
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
    private final PasswordEncoder passwordEncoder;

    public UserDetailsResponse createUser(CreateUserRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address already in use");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .organization(request.getOrganization())
                .region(request.getRegion())
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
    public List<UserResponse> getUsersByRegion(String region) {
        return userRepository.findByRegion(region).stream()
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
        if (request.getOrganization() != null) {
            user.setOrganization(request.getOrganization());
        }
        if (request.getRegion() != null) {
            user.setRegion(request.getRegion());
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

    // Mapping methods
    private UserDetailsResponse mapToDetailsResponse(User user) {
        return UserDetailsResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .organization(user.getOrganization())
                .region(user.getRegion())
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
