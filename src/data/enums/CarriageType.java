package data.enums;

/**
 * This enum holds the different carriage types a drone can have.
 * @author Leon Oet
 */
public enum CarriageType {
    SEN("Sensor"),
    ACT("Actor"),
    NOT("Nothing");

    private final String type;

    CarriageType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}