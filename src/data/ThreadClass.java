/**
 * This class manages the data update and processing mechanism for the application.
 * It runs a background thread to regularly check for new drone-related data and processes it as needed.
 */
package data;
import gui.DroneMenu;

import javax.swing.*;
import java.util.logging.Logger;

public class ThreadClass {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());
//    private boolean newDronesAvailable;
//    private boolean newDroneTypesAvailable;
//    private boolean newDroneDynamicsAvailable;

    /**
     * Constructs a new Backend instance with initial states set to indicate no new data is available.
     */
    public ThreadClass() {

    }

    /**
     * Starts a separate thread to periodically check for new data updates.
     */
    public void startDataUpdaterThread() {
        Thread updaterThread = new Thread(new DataUpdater());
        updaterThread.start();
    }

    /**
     * Displays a notification message in a dialog box.
     *
     * @param message The message to be displayed in the notification.
     */
    private void showNotification(String message) {
        JOptionPane.showMessageDialog(null, message, "Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Restarts the GUI components. This method should contain the logic to refresh or
     * recreate the GUI when new data is processed.
     */
    private void restartGUI() {
        // Implement your logic here to restart the GUI
        // For example: create a new instance of the GUI class and close the existing one
        LOGGER.info("Restarting the GUI...");
    }

    /**
     * Inner class DataUpdater implements the Runnable interface and defines the task for the data update thread.
     */
    private class DataUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Refresher.checkForRefresh();

                    if (Refresher.isRefreshNeeded) {
                        //showNotification("Restart GUI");
                        //restartGUI();
                        //one of these two methods makes the thread stop

                    }

                    // Wait for 1 minute before the next check
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                // Thread was interrupted, exit the thread
                LOGGER.warning("DataUpdater Thread was interrupted.");
            }
        }
    }

    /**
     * Main method to start the backend processing.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        ThreadClass threadClass = new ThreadClass();
        threadClass.startDataUpdaterThread();
    }
}