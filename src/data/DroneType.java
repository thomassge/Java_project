/**
 * This class represents the type of a drone, including its invariable specifications and attributes.
 * It is responsible for managing drone type data, checking for updates and saving the data to a file.
 */
package data;

import org.json.JSONArray;
import org.json.JSONObject;
import processing.Streamable;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneType {

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

    public static int getLocalCount() {
        return localCount;
    }

    public static void setLocalCount(int localCount) {
        DroneType.localCount = localCount;
    }

    public static int getServerCount() {
        return serverCount;
    }

    public static void setServerCount(int serverCount) {
        DroneType.serverCount = serverCount;
    }

    /**
     * The number of objects in memory
     */
    private static int memoryCount;

    /**
     * The filename where we store downloaded data
     */
    private final static String filename = "dronetypes.json";
    public static String getFilename() {
        return filename;
    }

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

    public static int getMemoryCount() {
        return memoryCount;
    }

    public static void setMemoryCount(int memoryCount) {
        DroneType.memoryCount = memoryCount;
    }

    // PRINT-methods to test without GETTER

    /**
     * Converts drone type data from JSON to DroneType objects.
     *
     * @param jsonString The JSON string containing drone type data.
     * @param droneTypes The list where DroneType objects will be added.
     */
    public static void initialize(String jsonString, LinkedList<DroneType> droneTypes) {
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            droneTypes.add(new DroneType(
                    o.getInt("id"),
                    o.getString("manufacturer"),
                    o.getString("typename"),
                    o.getInt("weight"),
                    o.getInt("max_speed"),
                    o.getInt("battery_capacity"),
                    o.getInt("control_range"),
                    o.getInt("max_carriage")
            ));
        }
        setMemoryCount(getMemoryCount() + jsonArray.length());
    }

    /**
     * Links drone to drones in the provided lists.
     *
     * @param droneTypes The list of DroneType objects.
     * @param drones The list of Drone objects.
     */
    public static void droneTypeToDroneLinker(LinkedList<DroneType> droneTypes, LinkedList<Drone> drones) {
        for(Drone droneObjectThatNeedsDroneTypeInformation : drones) {
            if(droneObjectThatNeedsDroneTypeInformation.getDroneTypeObject() == null) {

                for (DroneType droneType : droneTypes) {
                    if (droneObjectThatNeedsDroneTypeInformation.getExtractedDroneTypeID() == (droneType.getDroneTypeID())) {

                        droneObjectThatNeedsDroneTypeInformation.setDroneTypeObject(droneType);
                        break; //break added
                    }
                }
            }
            else { continue; }
        }
    }

//    /**
//     * Fetches drone type data and converts it into DroneType objects.
//     *
//     * @return A LinkedList of DroneType objects.
//     */
//    public static LinkedList<DroneType> initializeDroneTypes() {
//        checkForNewData(filename, URL, localCount, serverCount);
//
//        String myJson = Streamable.reader(filename);
//
//        LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
//        DroneType.initialize(myJson, droneTypes);
//
//        return droneTypes;
//    }

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
}
