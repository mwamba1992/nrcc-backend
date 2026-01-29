package tz.go.roadsfund.nrcc.enums;

/**
 * Types of applicants who can submit road reclassification requests
 */
public enum ApplicantType {
    INDIVIDUAL("Individual"),
    GROUP("Group"),
    MEMBER_OF_PARLIAMENT("Member of Parliament"),
    REGIONAL_ROADS_BOARD("Regional Roads Board");

    private final String displayName;

    ApplicantType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if this applicant type follows the Regional Roads Board workflow (via RAS/RC)
     */
    public boolean followsRrbWorkflow() {
        return this == REGIONAL_ROADS_BOARD;
    }

    /**
     * Check if this applicant type goes directly to Minister (Public/MP workflow)
     */
    public boolean goesDirectToMinister() {
        return this == INDIVIDUAL || this == GROUP || this == MEMBER_OF_PARLIAMENT;
    }
}
