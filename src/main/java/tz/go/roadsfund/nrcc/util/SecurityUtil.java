package tz.go.roadsfund.nrcc.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tz.go.roadsfund.nrcc.security.UserPrincipal;

/**
 * Utility class for security-related operations
 */
public class SecurityUtil {

    private SecurityUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get the currently authenticated user
     */
    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Get the current user's ID
     */
    public static Long getCurrentUserId() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Get the current user's email
     */
    public static String getCurrentUserEmail() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    /**
     * Get the current user's role
     */
    public static String getCurrentUserRole() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }
}
