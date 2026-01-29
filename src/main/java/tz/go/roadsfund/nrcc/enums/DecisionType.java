package tz.go.roadsfund.nrcc.enums;

/**
 * Minister decision types
 */
public enum DecisionType {
    APPROVE("Approve"),
    DISAPPROVE("Disapprove");

    private final String displayName;

    DecisionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
