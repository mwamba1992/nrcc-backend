package tz.go.roadsfund.nrcc.enums;

/**
 * Verification assignment status
 */
public enum VerificationStatus {
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String displayName;

    VerificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
