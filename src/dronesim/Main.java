/**
 * This is our main class and entry poiint of the program. This class is responsible for initializing
 * the program, fetching data and managing the overall flow of the application.
 */
package dronesim;

import data.*;
import processing.JSONDeruloHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    // Ein Logger für diese Klasse erstellen
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static JSONDeruloHelper jsonDerulo = new JSONDeruloHelper();
    private static LinkedList<Drone> drones = new LinkedList<Drone>();
    private static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();

    /**
     * The main method that serves as the entry point for the program.
     * It initializes data and manages the flow of execution.
     *
     * @param args Command line arguments (not used in this implementation).
     * @throws IOException if an I/O error occurs during data fetchhing.
     */
    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting the program...");

        try {
            drones = jsonDerulo.getData();
            droneTypes = jsonDerulo.getDroneTypes();

            for (Drone object : drones) {
                LOGGER.info("Drone type name: " + object.getDroneTypeObject().getTypename());
                LOGGER.info("Drone control range: " + object.getDroneTypeObject().getControlRange() + "\n");
            }

            jsonDerulo.refresh(drones, droneTypes);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching data from JSONDeruloHelper", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", e);
        }

//        drones.getFirst().saveAsFile();
//        droneTypes.getFirst().saveAsFile();
//        drones.getFirst().getDroneDynamicsArrayList().getFirst().saveAsFile();
//        can be used for refresh but refetches ALL data instead of using offset method

        LOGGER.info("Program execution completed.");
    }
}
