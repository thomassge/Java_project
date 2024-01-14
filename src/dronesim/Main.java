/**
 * This is our main class and entry poiint of the program. This class is responsible for initializing
 * the program, fetching data and managing the overall flow of the application.
 */
package dronesim;

import data.*;
import processing.JSONDeruloHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    // Ein Logger für diese Klasse erstellen
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static JSONDeruloHelper jsonDerulo = new JSONDeruloHelper();
    private static ArrayList<Drone> drones = new ArrayList<Drone>();
    private static ArrayList<DroneType> droneTypes = new ArrayList<DroneType>();
    private static ArrayList<DroneDynamics> droneDynamics = new ArrayList<DroneDynamics>();

    private static Drone drone = new Drone();
    private static DroneType droneType = new DroneType();
    private static DroneDynamics droneDynamic = new DroneDynamics();


    /**
     * The main method that serves as the entry point for the program.
     * It initializes data and manages the flow of execution.
     *
     * @param args Command line arguments (not used in this implementation).
     * @throws IOException if an I/O error occurs during data fetchhing.
     */
    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting the program...");

//        drones = jsonDerulo.getDrones();
//        droneTypes = jsonDerulo.getDroneTypes();
//        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);
//        jsonDerulo.addDroneDynamicsData(drones);
//
//        jsonDerulo.refresh(drones, droneTypes);

        drones = drone.getDrones();
        droneTypes = droneType.getDroneTypes();
        droneDynamics = droneDynamic.getDroneDynamics();


        LOGGER.info("Program execution completed.");
    }
}
