package processing;
import javax.swing.*;
import java.io.FileNotFoundException;

public class Backend {

    private boolean newDronesAvailable;
    private boolean newDroneTypesAvailable;
    private boolean newDroneDynamicsAvailable;

    private static JSONDeruloHelper helper = new JSONDeruloHelper();

    public Backend() {
        this.newDronesAvailable = false;
        this.newDroneTypesAvailable = false;
        this.newDroneDynamicsAvailable = false;
    }

    public void startDataUpdaterThread() {
        Thread updaterThread = new Thread(new DataUpdater());
        updaterThread.start();
    }

    private void processData() {
        // Implement your logic here to process the new data
        System.out.println("Processing new data...");

        // Notify the user about new data
        showNotification("New Data Found");

        // Restart the GUI
        restartGUI();
    }

    private void showNotification(String message) {
        JOptionPane.showMessageDialog(null, message, "Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartGUI() {
        // Implement your logic here to restart the GUI
        // For example: create a new instance of the GUI class and close the existing one
        System.out.println("Restarting the GUI...");
    }

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

    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.startDataUpdaterThread();
    }
}