package tz.go.roadsfund.nrcc.enums;

/**
 * Eligibility criteria for road reclassification
 * Based on SRS Section 3.2.1 and 3.2.2
 */
public enum EligibilityCriterion {
    // Regional Road Criteria (R1-R7)
    R1("Directly joining any two or more existing district headquarters"),
    R2("Linking a new district headquarters with an existing regional or district headquarters following creation of a new district"),
    R3("Linking a new regional headquarters with a new or existing district headquarters following the creation of a new region"),
    R4("A secondary national road that connects a trunk road and a district or regional headquarters"),
    R5("A secondary national road that connects a regional headquarters and a district headquarters"),
    R6("Not forming a loop road connecting two points on the same regional road"),
    R7("Not running parallel to an existing regional road connecting the same regional headquarters and the same district headquarters"),

    // Trunk Road Criteria (T1-T5)
    T1("Linking a new regional headquarters with an existing or another new regional headquarters following creation of a new region"),
    T2("A primary national road linking two or more regional headquarters"),
    T3("An international route that links regional headquarters and another major or important city, town, or major ports outside the United Republic"),
    T4("Not running parallel to an existing trunk road connecting the same cities, towns or major port"),
    T5("Not forming a loop road connecting two points on the same trunk road");

    private final String description;

    EligibilityCriterion(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRegionalCriterion() {
        return this.name().startsWith("R");
    }

    public boolean isTrunkCriterion() {
        return this.name().startsWith("T");
    }
}
