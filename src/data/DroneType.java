/**
 * This class represents the type of a drone, including its invariable specifications and attributes.
 * It is responsible for managing drone type data, checking for updates and saving the data to a file.
 */
package data;

import org.json.JSONObject;
import processing.JSONDeruloHelper;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneType extends AbstractDroneOperations {

    private static final Logger logger = Logger.getLogger(DroneType.class.getName());

    // DRONETYPE DATA
    private int droneTypeID;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maximumSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maximumCarriage;

    /**
     * The number of entries of drones in the last downloaded local json file
     */
    private static int localCount;

    /**
     * The number of entries of drones on the webserver
     */
    private static int serverCount;

    /**
     * The filename where we store downloaded data
     */
    public final static String filename = "dronetypes.json";

    /**
     * Dronetypes API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/dronetypes/";
    public static String getUrl() {
        return URL;
    }



    // Constructor

    /**
     * Default constructor for creating a DroneType instance.
     */
    public DroneType() {
    }

    /**
     * Parameterized constructor for creating a DroneType instance with specified attributes.
     *
     * @param droneTypeID        Unique identifier for the drone type.
     * @param manufacturer       Manufacturer of the drone.
     * @param typename           Name of the drone type.
     * @param weight             Weight of the drone.
     * @param maximumSpeed       Maximum speed of the drone.
     * @param batteryCapacity    Battery capacity of the drone.
     * @param controlRange       Control range of the drone.
     * @param maximumCarriage    Maximum carriage capacity of the drone.
     */
    public DroneType(int droneTypeID, String manufacturer, String typename, int weight, int maximumSpeed, int batteryCapacity, int controlRange, int maximumCarriage) {
        logger.info("DroneType Object created");
        this.droneTypeID = droneTypeID;
        this.manufacturer = manufacturer;
        this.typename = typename;
        this.weight = weight;
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maximumCarriage = maximumCarriage;
    }

    // GETTER-methods

    public int getDroneTypeID() {
        return this.droneTypeID;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public String getTypename() {
        return this.typename;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getMaximumSpeed() {
        return this.maximumSpeed;
    }

    public int getBatteryCapacity() {
        return this.batteryCapacity;
    }

    public int getControlRange() {
        return this.controlRange;
    }

    public int getMaximumCarriage() {
        return this.maximumCarriage;
    }

    // PRINT-methods to test without GETTER

    /**
     * Prints the drone type details to the log.
     */
    public void printDroneType() {
        logger.info("DroneType id: " + this.droneTypeID);
        logger.info("Manufacturer: " + this.manufacturer);
        logger.info("TypeName: " + this.typename);
        logger.info("Weight: " + this.weight);
        logger.info("Maximum Speed: " + this.maximumSpeed);
        logger.info("BatteryCapacity: " + this.batteryCapacity);
        logger.info("Control Range (int): " + this.controlRange);
        logger.info("Maximum Carriage: " + this.maximumCarriage);
    }

    @Override
    public void checkForNewData2() {
        checkFile(filename);
        localCount = getLocalCount2(filename, localCount);
        serverCount = getServerCount2(URL);

        if(serverCount == 0) {
            logger.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
            //TODO: Own Exception
        }
        if (localCount == serverCount) {
            logger.log(Level.INFO, "local- and serverDroneCount identical.");
        }
        else if(localCount < serverCount) {
            saveAsFile2(URL, serverCount, filename);
        }
        else {
            logger.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
        }
    }
}
