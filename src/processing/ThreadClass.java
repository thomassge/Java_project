package processing;

import gui.DroneMenu;

import java.util.logging.Logger;
/**
 * This class holds logic to create a new Thread that checks for new data on the web server.
 * @author Leon Oet
 */
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
        /**
         * The overwritten run method creates a Thread, that runs in an (infinite) loop,
         * until the Thread is interrupted.
         * It periodically checks if the web server holds additional data that needs to be fetched.
         * This check happens roughly every minute.
         */
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