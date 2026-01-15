package tz.go.roadsfund.nrcc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tz.go.roadsfund.nrcc.config.RolePermissionConfig;
import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.enums.UserRole;
import tz.go.roadsfund.nrcc.util.SecurityUtil;

/**
 * Component for checking user permissions
 */
@Component
@RequiredArgsConstructor
public class PermissionChecker {

    /**
     * Check if current user has a specific permission
     */
    public boolean hasPermission(Permission permission) {
        String roleStr = SecurityUtil.getCurrentUserRole();
        if (roleStr == null) {
            return false;
        }

        try {
            UserRole role = UserRole.valueOf(roleStr);
            return RolePermissionConfig.hasPermission(role, permission);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if current user has any of the specified permissions
     */
    public boolean hasAnyPermission(Permission... permissions) {
        for (Permission permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if current user has all of the specified permissions
     */
    public boolean hasAllPermissions(Permission... permissions) {
        for (Permission permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if user with specific role has a permission
     */
    public boolean roleHasPermission(UserRole role, Permission permission) {
        return RolePermissionConfig.hasPermission(role, permission);
    }
}
