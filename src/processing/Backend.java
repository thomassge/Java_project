/**
 * This class manages the data update and processing mechanism for the application.
 * It runs a background thread to regularly check for new drone-related data and processes it as needed.
 */
package processing;
import javax.swing.*;
import java.io.FileNotFoundException;

public class Backend {

    private boolean newDronesAvailable;
    private boolean newDroneTypesAvailable;
    private boolean newDroneDynamicsAvailable;

    private static JSONDeruloHelper helper = new JSONDeruloHelper();

    /**
     * Constructs a new Backend instance with initial states set to indicate no new data is available.
     */
    public Backend() {
        this.newDronesAvailable = false;
        this.newDroneTypesAvailable = false;
        this.newDroneDynamicsAvailable = false;
    }

    /**
     * Starts a separate thread to periodically check for new data updates.
     */
    public void startDataUpdaterThread() {
        Thread updaterThread = new Thread(new DataUpdater());
        updaterThread.start();
    }

    /**
     * Processes new data when available. This includes showing notifications to the user
     * and potentially restarting the GUI to reflect new data.
     */
    private void processData() {
        // Implement your logic here to process the new data
        System.out.println("Processing new data...");

        // Notify the user about new data
        showNotification("New Data Found");

        // Restart the GUI
        restartGUI();
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
        System.out.println("Restarting the GUI...");
    }

    /**
     * Inner class DataUpdater implements the Runnable interface and defines the task for the data update thread.
     */
    private class DataUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    newDronesAvailable = helper.droneObject.checkForNewData();
                    newDroneTypesAvailable = helper.droneTypesObject.checkForNewData();
                    newDroneDynamicsAvailable = helper.droneDynamicsObject.checkForNewData();
                    if (newDronesAvailable || newDroneTypesAvailable || newDroneDynamicsAvailable) {
                        processData();
                        helper.droneObject.saveAsFile();
                        helper.droneTypesObject.saveAsFile();
                        helper.droneDynamicsObject.saveAsFile();
                    } // evtl. mit cases arbeiten

                    // Wait for 1 minute before the next check
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                // Thread was interrupted, exit the thread
                System.out.println("DataUpdater Thread was interrupted.");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Main method to start the backend processing.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.startDataUpdaterThread();
    }
}