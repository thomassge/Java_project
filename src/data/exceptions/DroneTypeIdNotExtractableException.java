package data.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This exception is thrown when the DroneType ID cannot be extracted from a URL string.
 * @author Leon Oet
 */
public class DroneTypeIdNotExtractableException extends Exception {

    private static final Logger LOGGER = Logger.getLogger(DroneTypeIdNotExtractableException.class.getName());

    /**
     * Logs a severe level message indicating the failure to extract the DroneType ID
     * and therefore fails to link it to the drones.
     */
    public DroneTypeIdNotExtractableException() {
        super();
        LOGGER.log(Level.WARNING, "Couldn't extract DroneType ID from URL-String");
        LOGGER.log(Level.WARNING, "No RegEx Match with pattern: '[0-9]+' found.");
    }
}