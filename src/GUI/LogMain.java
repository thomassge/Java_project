/**
 * This class provides logging functionalities for the application.
 * It sets up a logger with a console handler and incudes utility methods for formatting
 * and logging messages and exceptions.
 */
package GUI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogMain {

    private static Logger logger;

    static {
        logger = Logger.getLogger("LogUtil");
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);
    }

    /**
     * Gets the singleton instance of Logger configured for the application.
     *
     * @return The Logger instance.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Formats a log message with the current timestamp.
     *
     * @param message The message to be formatted.
     * @return The formatted log message.
     */
    public static String formatLogMessage(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        return "[" + formattedDate + "] " + message;
    }

    /**
     * Logs an exception using the severe log level and prints the stack trace.
     *
     * @param e The exception to be logged.
     */
    public static void logException(Exception e) {
        logger.severe(formatLogMessage("Exception occurred: " + e.getMessage()));
        e.printStackTrace();
    }
}
