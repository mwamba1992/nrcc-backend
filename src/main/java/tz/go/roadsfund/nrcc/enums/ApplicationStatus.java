package tz.go.roadsfund.nrcc.enums;

/**
 * Application status enumeration based on SRS Section 6.2
 */
public enum ApplicationStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    RETURNED_FOR_CORRECTION("Returned for Correction"),
    UNDER_RAS_REVIEW("Under RAS Review"),
    UNDER_RC_REVIEW("Under RC Review"),
    UNDER_MINISTER_REVIEW("Under Minister Review"),
    WITH_NRCC_CHAIR("With NRCC Chair"),
    VERIFICATION_IN_PROGRESS("Verification in Progress"),
    NRCC_REVIEW_MEETING("NRCC Review Meeting"),
    RECOMMENDATION_SUBMITTED("Recommendation Submitted"),
    APPROVED("Approved"),
    DISAPPROVED_REFUSED("Disapproved - Refused"),
    DISAPPROVED_DESIGNATED("Disapproved - Designated"),
    PENDING_GAZETTEMENT("Pending Gazettement"),
    GAZETTED("Gazetted"),
    APPEAL_SUBMITTED("Appeal Submitted"),
    APPEAL_UNDER_REVIEW("Appeal Under Review"),
    APPEAL_REJECTED("Appeal Rejected"),
    APPEAL_CLOSED("Appeal Closed");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if application can be edited by applicant in this status
     */
    public boolean isEditableByApplicant() {
        return this == DRAFT || this == RETURNED_FOR_CORRECTION;
    }

    /**
     * Check if application can be appealed in this status
     */
    public boolean canBeAppealed() {
        return this == DISAPPROVED_REFUSED;
    }

    /**
     * Check if application is in a final state
     */
    public boolean isFinal() {
        return this == GAZETTED || this == DISAPPROVED_DESIGNATED || this == APPEAL_CLOSED || this == APPEAL_REJECTED;
    }

    /**
     * Check if application is under review
     */
    public boolean isUnderReview() {
        return this == UNDER_RAS_REVIEW || this == UNDER_RC_REVIEW ||
               this == UNDER_MINISTER_REVIEW || this == WITH_NRCC_CHAIR ||
               this == VERIFICATION_IN_PROGRESS || this == NRCC_REVIEW_MEETING ||
               this == APPEAL_UNDER_REVIEW;
    }
}
