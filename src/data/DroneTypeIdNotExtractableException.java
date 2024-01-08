package data;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneTypeIdNotExtractableException extends Exception {

    // Ein Logger für diese Klasse erstellen
    private static final Logger LOGGER = Logger.getLogger(DroneTypeIdNotExtractableException.class.getName());

    public DroneTypeIdNotExtractableException() {
        super();

        // Loggen Sie eine SEVERE-Nachricht
        LOGGER.log(Level.SEVERE, "Couldnt extract DroneType ID from URL-String");
        LOGGER.log(Level.SEVERE, "No RegEx Match with pattern: '[0-9]+' found.");
    }
}
