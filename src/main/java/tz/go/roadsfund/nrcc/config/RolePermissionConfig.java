package tz.go.roadsfund.nrcc.config;

import tz.go.roadsfund.nrcc.enums.Permission;
import tz.go.roadsfund.nrcc.enums.UserRole;

import java.util.*;

/**
 * Configuration for Role-Permission mapping
 */
public class RolePermissionConfig {

    private static final Map<UserRole, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        // PUBLIC_APPLICANT Permissions
        ROLE_PERMISSIONS.put(UserRole.PUBLIC_APPLICANT, EnumSet.of(
                Permission.APPLICATION_CREATE,
                Permission.APPLICATION_READ,
                Permission.APPLICATION_UPDATE,
                Permission.APPLICATION_SUBMIT,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_APPEAL,
                Permission.FILE_UPLOAD,
                Permission.FILE_DOWNLOAD,
                Permission.ROAD_LIST,
                Permission.ROAD_READ,
                Permission.REGION_READ,
                Permission.REGION_LIST,
                Permission.DISTRICT_READ,
                Permission.DISTRICT_LIST,
                Permission.ORGANIZATION_READ,
                Permission.ORGANIZATION_LIST
        ));

        // MEMBER_OF_PARLIAMENT Permissions
        ROLE_PERMISSIONS.put(UserRole.MEMBER_OF_PARLIAMENT, Set.of(
                Permission.APPLICATION_CREATE,
                Permission.APPLICATION_READ,
                Permission.APPLICATION_UPDATE,
                Permission.APPLICATION_SUBMIT,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_APPEAL,
                Permission.FILE_UPLOAD,
                Permission.FILE_DOWNLOAD,
                Permission.REPORT_VIEW
        ));

        // REGIONAL_ROADS_BOARD_INITIATOR Permissions
        ROLE_PERMISSIONS.put(UserRole.REGIONAL_ROADS_BOARD_INITIATOR, Set.of(
                Permission.APPLICATION_CREATE,
                Permission.APPLICATION_READ,
                Permission.APPLICATION_UPDATE,
                Permission.APPLICATION_SUBMIT,
                Permission.APPLICATION_LIST,
                Permission.FILE_UPLOAD,
                Permission.FILE_DOWNLOAD,
                Permission.REPORT_VIEW
        ));

        // REGIONAL_ADMINISTRATIVE_SECRETARY Permissions
        ROLE_PERMISSIONS.put(UserRole.REGIONAL_ADMINISTRATIVE_SECRETARY, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_APPROVE,
                Permission.APPLICATION_RETURN,
                Permission.FILE_DOWNLOAD,
                Permission.REPORT_VIEW
        ));

        // REGIONAL_COMMISSIONER Permissions
        ROLE_PERMISSIONS.put(UserRole.REGIONAL_COMMISSIONER, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_APPROVE,
                Permission.APPLICATION_RETURN,
                Permission.FILE_DOWNLOAD,
                Permission.REPORT_VIEW
        ));

        // MINISTER_OF_WORKS Permissions
        ROLE_PERMISSIONS.put(UserRole.MINISTER_OF_WORKS, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_DECIDE,
                Permission.APPLICATION_APPROVE,
                Permission.FILE_DOWNLOAD,
                Permission.REPORT_VIEW,
                Permission.REPORT_EXPORT,
                Permission.REPORT_STATISTICS
        ));

        // NRCC_CHAIRPERSON Permissions
        ROLE_PERMISSIONS.put(UserRole.NRCC_CHAIRPERSON, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_ASSIGN_VERIFICATION,
                Permission.APPLICATION_RECOMMEND,
                Permission.FILE_DOWNLOAD,
                Permission.FILE_UPLOAD,
                Permission.REPORT_VIEW,
                Permission.REPORT_EXPORT,
                Permission.ACTION_PLAN_CREATE,
                Permission.ACTION_PLAN_READ,
                Permission.ACTION_PLAN_UPDATE,
                Permission.ACTION_PLAN_APPROVE
        ));

        // NRCC_MEMBER Permissions
        ROLE_PERMISSIONS.put(UserRole.NRCC_MEMBER, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_VERIFY,
                Permission.FILE_DOWNLOAD,
                Permission.FILE_UPLOAD,
                Permission.ACTION_PLAN_READ,
                Permission.ACTION_PLAN_TRACK
        ));

        // NRCC_SECRETARIAT Permissions
        ROLE_PERMISSIONS.put(UserRole.NRCC_SECRETARIAT, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_UPDATE,
                Permission.APPLICATION_LIST,
                Permission.FILE_DOWNLOAD,
                Permission.FILE_UPLOAD,
                Permission.NOTIFICATION_SEND,
                Permission.REPORT_VIEW,
                Permission.ACTION_PLAN_CREATE,
                Permission.ACTION_PLAN_READ,
                Permission.ACTION_PLAN_UPDATE,
                Permission.ACTION_PLAN_TRACK
        ));

        // MINISTRY_LAWYER Permissions
        ROLE_PERMISSIONS.put(UserRole.MINISTRY_LAWYER, Set.of(
                Permission.APPLICATION_READ,
                Permission.APPLICATION_LIST,
                Permission.APPLICATION_GAZETTE,
                Permission.FILE_DOWNLOAD,
                Permission.FILE_UPLOAD,
                Permission.REPORT_VIEW
        ));

        // SYSTEM_ADMINISTRATOR Permissions (all permissions)
        ROLE_PERMISSIONS.put(UserRole.SYSTEM_ADMINISTRATOR, EnumSet.allOf(Permission.class));
    }

    public static Set<Permission> getPermissionsForRole(UserRole role) {
        return ROLE_PERMISSIONS.getOrDefault(role, Collections.emptySet());
    }

    public static boolean hasPermission(UserRole role, Permission permission) {
        Set<Permission> permissions = getPermissionsForRole(role);
        return permissions.contains(permission);
    }

    public static List<String> getPermissionStrings(UserRole role) {
        return getPermissionsForRole(role).stream()
                .map(Permission::name)
                .sorted()
                .toList();
    }
}
