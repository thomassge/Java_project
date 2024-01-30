/**
 * This class manages the data update and processing mechanism for the application.
 * It runs a background thread to regularly check for new drone-related data and processes it as needed.
 */
package processing;

import gui.DroneMenu;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadClass {
    private static final Logger LOGGER = Logger.getLogger(ThreadClass.class.getName());
    DataFactory factory = new DataFactory();

    /**
     * Starts a separate thread to periodically check for new data updates.
     */
    public void startDataUpdaterThread() {
        Thread updaterThread = new Thread(new DataUpdater());
        updaterThread.start();
    }

    /**
     * Inner class DataUpdater implements the Runnable interface and defines the task for the data update thread.
     */
    private class DataUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    if (factory.checkForRefresh()) {
                        DroneMenu.restarter();
                    }
                    Thread.sleep(60000);
                }
            } catch (InterruptedException e) {
                LOGGER.warning("DataUpdater Thread was interrupted.");
            }
        }
    }
}