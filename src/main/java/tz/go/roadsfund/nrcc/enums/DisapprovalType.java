package tz.go.roadsfund.nrcc.enums;

/**
 * Types of disapproval decisions
 * - REFUSED: Applicant can submit an appeal
 * - DESIGNATED: Final decision, no appeal allowed
 */
public enum DisapprovalType {
    REFUSED("Refused"),
    DESIGNATED("Designated");

    private final String displayName;

    DisapprovalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
