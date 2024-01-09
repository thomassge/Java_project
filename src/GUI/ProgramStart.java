/**
 * This class serves as the entry point of the application.
 * It initializes the application data and launches the main GUI components.
 */
package GUI;

import data.*;
import processing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.IOException;

public class ProgramStart {

    public static JSONDeruloHelper helper = new JSONDeruloHelper();
    public static LinkedList<Drone> drones = new LinkedList<>();
    public static LinkedList<DroneType> droneTypes = new LinkedList<>();

    /**
     * Fetches and processes drone data from an external source using the JSONDeruloHelper.
     * It links drone types to drone and adds dynamics data to each drone.
     *
     * @return A LinkedList of Drone objects with complete data.
     * @throws IOException if there is an error in fetching or processing the data.
     */
    public static LinkedList<Drone> getData() throws IOException {
        drones = helper.getDrones();
        droneTypes = helper.getDroneTypes();
        helper.droneTypeToDroneLinker(droneTypes, drones);
        helper.addDroneDynamicsData(drones);

        return drones;
    }

    /**
     * The main method to start the application. It initializes data and sets up the GUI.
     *
     * @param args Command line arguments (not used in this application).
     * @throws IOException if there is an error in fetching or processing data.
     */
    public static void main(String[] args) throws IOException {
        LogMain lm = new LogMain();
        drones = getData();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {DroneMenu.createDroneTableGUI(drones);}});

    }
}