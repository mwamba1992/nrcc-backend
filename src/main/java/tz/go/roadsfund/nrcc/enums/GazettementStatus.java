package tz.go.roadsfund.nrcc.enums;

/**
 * Gazettement tracking status
 */
public enum GazettementStatus {
    PENDING("Pending"),
    GAZETTED("Gazetted");

    private final String displayName;

    GazettementStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
