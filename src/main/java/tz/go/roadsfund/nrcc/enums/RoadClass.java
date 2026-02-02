package tz.go.roadsfund.nrcc.enums;

/**
 * Road classification types based on Tanzania road network
 */
public enum RoadClass {
    TRUNK("Trunk Road"),
    REGIONAL("Regional Road"),
    DISTRICT("District Road"),
    FEEDER("Feeder Road"),
    URBAN("Urban Road"),
    COMMUNITY("Community Road");

    private final String displayName;

    RoadClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
