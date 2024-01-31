package data.enums;

/**
 * This enum holds the different status a drone can have.
 */
public enum Status {
    ON("Drone is ON"),
    OF("Drone is OFF"),
    IS("Drone has ISSUES");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
