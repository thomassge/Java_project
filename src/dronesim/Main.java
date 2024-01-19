/**
 * This is our main class and entry poiint of the program. This class is responsible for initializing
 * the program, fetching data and managing the overall flow of the application.
 */
package dronesim;

import data.*;
import processing.JSONDeruloHelper;

import java.io.IOException;
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

        DataStorage dataStorage = new DataStorage();
        //DataFactory dataFactory = new DataFactory(dataStorage);
        dataStorage.getDrones().getFirst().printDrone();




//        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);
        //jsonDerulo.addDroneDynamicsData(drones);

//        jsonDerulo.refresh(drones, droneTypes);

        LOGGER.info("Program execution completed.");
    }
}
