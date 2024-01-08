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

    public static Logger getLogger() {
        return logger;
    }

    public static String formatLogMessage(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        return "[" + formattedDate + "] " + message;
    }

    public static void logException(Exception e) {
        logger.severe(formatLogMessage("Exception occurred: " + e.getMessage()));
        e.printStackTrace();
    }
}
