package data;

public class DroneTypeIdNotExtractableException extends Exception{
    public DroneTypeIdNotExtractableException() {
        super();
        System.out.println("Couldnt extract DroneType ID from URL-String");
        System.out.println("No RegEx Match with pattern: '[0-9]+' found.");
    }
}
