package tz.go.roadsfund.nrcc.enums;

/**
 * System permissions for fine-grained access control
 */
public enum Permission {
    // User Management
    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE,
    USER_LIST,

    // Application Management
    APPLICATION_CREATE,
    APPLICATION_READ,
    APPLICATION_UPDATE,
    APPLICATION_DELETE,
    APPLICATION_SUBMIT,
    APPLICATION_LIST,

    // Workflow Actions
    APPLICATION_APPROVE,
    APPLICATION_RETURN,
    APPLICATION_ASSIGN_VERIFICATION,
    APPLICATION_VERIFY,
    APPLICATION_RECOMMEND,
    APPLICATION_DECIDE,
    APPLICATION_GAZETTE,
    APPLICATION_APPEAL,

    // Action Plan Management
    ACTION_PLAN_CREATE,
    ACTION_PLAN_READ,
    ACTION_PLAN_UPDATE,
    ACTION_PLAN_DELETE,
    ACTION_PLAN_APPROVE,
    ACTION_PLAN_TRACK,

    // Meeting Management
    MEETING_CREATE,
    MEETING_READ,
    MEETING_UPDATE,
    MEETING_DELETE,
    MEETING_LIST,
    MEETING_CONDUCT,

    // Region Management
    REGION_CREATE,
    REGION_READ,
    REGION_UPDATE,
    REGION_DELETE,
    REGION_LIST,

    // District Management
    DISTRICT_CREATE,
    DISTRICT_READ,
    DISTRICT_UPDATE,
    DISTRICT_DELETE,
    DISTRICT_LIST,

    // Organization Management
    ORGANIZATION_CREATE,
    ORGANIZATION_READ,
    ORGANIZATION_UPDATE,
    ORGANIZATION_DELETE,
    ORGANIZATION_LIST,

    // Road Management
    ROAD_CREATE,
    ROAD_READ,
    ROAD_UPDATE,
    ROAD_DELETE,
    ROAD_LIST,

    // Reporting
    REPORT_VIEW,
    REPORT_EXPORT,
    REPORT_STATISTICS,

    // System Administration
    SYSTEM_CONFIGURE,
    AUDIT_LOG_VIEW,
    NOTIFICATION_SEND,

    // File Management
    FILE_UPLOAD,
    FILE_DOWNLOAD,
    FILE_DELETE;

    public String getPermission() {
        return name();
    }
}
