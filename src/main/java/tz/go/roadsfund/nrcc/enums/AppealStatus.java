package tz.go.roadsfund.nrcc.enums;

/**
 * Appeal status
 */
public enum AppealStatus {
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    CLOSED("Closed");

    private final String displayName;

    AppealStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
