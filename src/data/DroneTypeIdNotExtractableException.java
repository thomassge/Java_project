/**
 * This exception is thrown when the DroneType ID cannot be extracted from a URL string.
 * It extends the standard Exception class and logs an error message using Java's logging framework.
 */
package data;


import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneTypeIdNotExtractableException extends Exception {

    // Ein Logger für diese Klasse erstellen
    private static final Logger LOGGER = Logger.getLogger(DroneTypeIdNotExtractableException.class.getName());

    /**
     * Constructs a new DroneTypeIdNotExtractableExceptpion.
     * Logs a severe level message indicating the failure to extract the DroneType ID.
     */

    public DroneTypeIdNotExtractableException() {
        super();

        // Loggen Sie eine SEVERE-Nachricht
        LOGGER.log(Level.SEVERE, "Couldnt extract DroneType ID from URL-String");
        LOGGER.log(Level.SEVERE, "No RegEx Match with pattern: '[0-9]+' found.");
    }
}
