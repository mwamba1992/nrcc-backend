package tz.go.roadsfund.nrcc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.go.roadsfund.nrcc.dto.request.ChangePasswordRequest;
import tz.go.roadsfund.nrcc.dto.request.CreateUserRequest;
import tz.go.roadsfund.nrcc.dto.request.UpdateUserRequest;
import tz.go.roadsfund.nrcc.dto.response.ApiResponse;
import tz.go.roadsfund.nrcc.dto.response.UserDetailsResponse;
import tz.go.roadsfund.nrcc.dto.response.UserResponse;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.enums.UserRole;
import tz.go.roadsfund.nrcc.security.RequirePermission;
import tz.go.roadsfund.nrcc.service.UserService;

import java.util.List;

/**
 * REST Controller for user management
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create a new user (Admin only)
     */
    @PostMapping
    @RequirePermission(Permission.USER_CREATE)
    public ResponseEntity<ApiResponse<UserDetailsResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDetailsResponse user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", user));
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    @RequirePermission(anyOf = {Permission.USER_READ, Permission.USER_LIST})
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserById(@PathVariable Long id) {
        UserDetailsResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Get user by email
     */
    @GetMapping("/email/{email}")
    @RequirePermission(anyOf = {Permission.USER_READ, Permission.USER_LIST})
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserByEmail(@PathVariable String email) {
        UserDetailsResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Get all users
     */
    @GetMapping
    @RequirePermission(Permission.USER_LIST)
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get users with pagination
     */
    @GetMapping("/paginated")
    @RequirePermission(Permission.USER_LIST)
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<UserResponse> users = userService.getUsersPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get users by role
     */
    @GetMapping("/role/{role}")
    @RequirePermission(Permission.USER_LIST)
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable UserRole role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get users by region
     */
    @GetMapping("/region/{region}")
    @RequirePermission(Permission.USER_LIST)
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRegion(@PathVariable String region) {
        List<UserResponse> users = userService.getUsersByRegion(region);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get users by status
     */
    @GetMapping("/status/{status}")
    @RequirePermission(Permission.USER_LIST)
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByStatus(@PathVariable String status) {
        List<UserResponse> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    @RequirePermission(Permission.USER_UPDATE)
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserDetailsResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }

    /**
     * Change user password
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    @RequirePermission(Permission.USER_DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    /**
     * Activate user
     */
    @PutMapping("/{id}/activate")
    @RequirePermission(Permission.USER_UPDATE)
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
    }

    /**
     * Deactivate user
     */
    @PutMapping("/{id}/deactivate")
    @RequirePermission(Permission.USER_UPDATE)
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }
}
