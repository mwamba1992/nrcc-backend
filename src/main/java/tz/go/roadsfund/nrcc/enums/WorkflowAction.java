package tz.go.roadsfund.nrcc.enums;

/**
 * Workflow action types for audit trail
 */
public enum WorkflowAction {
    CREATE("Created"),
    UPDATE("Updated"),
    SUBMIT("Submitted"),
    APPROVE("Approved"),
    RETURN("Returned for Correction"),
    REJECT("Rejected"),
    FORWARD("Forwarded"),
    ASSIGN("Assigned"),
    VERIFY("Verified"),
    RECOMMEND("Recommended"),
    DECIDE("Decision Made"),
    GAZETTE("Gazetted"),
    APPEAL("Appealed"),
    CLOSE("Closed");

    private final String displayName;

    WorkflowAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
