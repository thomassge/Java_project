/**
 * This is our main class and entry poiint of the program. This class is responsible for initializing
 * the program, fetching data and managing the overall flow of the application.
 */
package dronesim;

import data.*;
import gui.DroneMenu;
import processing.JSONDeruloHelper;
import processing.ThreadClass;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Main {

    // Ein Logger für diese Klasse erstellen
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * The main method that serves as the entry point for the program.
     * It initializes data and manages the flow of execution.
     *
     * @param args Command line arguments (not used in this implementation).
     * @throws IOException if an I/O error occurs during data fetchhing.
     */
    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting the program...");

//        DataFactory factory = new DataFactory();
//        ArrayList<DataStorage> data = factory.getDataStorage();
//
//        data.getFirst().getDrone().printDrone();
//        data.getFirst().getDroneType().printDroneType();
//        data.getFirst().getDroneDynamicsList().getFirst().printDroneDynamics();

        DataFactory factory = new DataFactory();
        ArrayList<DataStorage> data = factory.getDataStorage();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DroneMenu droneM = new DroneMenu(data, factory);
            }
        });
        ThreadClass threadClass = new ThreadClass();
        threadClass.startDataUpdaterThread();

        data.getFirst().getDrone().getDroneTypeObject();
        int i = 0;
        LOGGER.info("Program execution completed.");
    }
}
