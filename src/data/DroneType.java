/**
 * This class represents the type of a drone, including its invariable specifications and attributes.
 * It is responsible for managing drone type data, checking for updates and saving the data to a file.
 */
package data;

import org.json.JSONArray;
import org.json.JSONObject;
import processing.Initializable;
import util.Streamer;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneType implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DroneType.class.getName());

    //DRONETYPE DATA
    private int droneTypeID;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maximumSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maximumCarriage;
    /**
     * The number of entries in file, on the server and in memory.
     */
    private static int localCount;
    private static int serverCount;
    private static int memoryCount;
    /**
     * The filename where we store downloaded data
     */
    private final static String filename = "dronetypes.json";
    /**
     * Dronetypes API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/dronetypes/";

    //CONSTRUCTORS
    /**
     * Default constructor for creating a DroneType instance.
     */
    public DroneType() {LOGGER.log(Level.INFO, "DroneType Object Created from empty constructor.");}

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
        LOGGER.info("DroneType Object created");
        this.droneTypeID = droneTypeID;
        this.manufacturer = manufacturer;
        this.typename = typename;
        this.weight = weight;
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maximumCarriage = maximumCarriage;
    }

    //GETTER METHODS
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

    //STATIC GETTER AND SETTER METHODS
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

    public static int getMemoryCount() {
        return memoryCount;
    }
    public static void setMemoryCount(int memoryCount) {
        DroneType.memoryCount = memoryCount;
    }

    public static String getFilename() {
        return filename;
    }

    public static String getUrl() {
        return URL;
    }

    //OTHER METHODS
    /**
     * Converts drone type data from JSON to DroneType objects.
     *
     * @param jsonString The JSON string containing drone type data.
     * @param droneTypes The list where DroneType objects will be added.
     */
//    public static LinkedList<DroneType> initialize(String jsonString) {
//        LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
//        JSONObject wholeHtml = new JSONObject(jsonString);
//        JSONArray jsonArray = wholeHtml.getJSONArray("results");
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject o = jsonArray.getJSONObject(i);
//            droneTypes.add(new DroneType(
//                    o.getInt("id"),
//                    o.getString("manufacturer"),
//                    o.getString("typename"),
//                    o.getInt("weight"),
//                    o.getInt("max_speed"),
//                    o.getInt("battery_capacity"),
//                    o.getInt("control_range"),
//                    o.getInt("max_carriage")
//            ));
//        }
//        setMemoryCount(getMemoryCount() + jsonArray.length());
//        return droneTypes;
//    }



    //PRINT METHODS
    /**
     * Prints the drone type details to the log.
     */
    public void printDroneType() {
        LOGGER.info("DroneType id: " + this.droneTypeID);
        LOGGER.info("Manufacturer: " + this.manufacturer);
        LOGGER.info("TypeName: " + this.typename);
        LOGGER.info("Weight: " + this.weight);
        LOGGER.info("Maximum Speed: " + this.maximumSpeed);
        LOGGER.info("BatteryCapacity: " + this.batteryCapacity);
        LOGGER.info("Control Range (int): " + this.controlRange);
        LOGGER.info("Maximum Carriage: " + this.maximumCarriage);
    }

    public static LinkedList<DroneType> create() {
        return new DroneType().initialise();
    };

    @Override
    public LinkedList<DroneType> initialise() {
        LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();

        String jsonString = new Streamer().reader(filename);
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
        return droneTypes;
    }

    public boolean isNewDataAvailable() {
        this.createFile(filename);

        if(serverCount == 0) {
            //logger.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
            //TODO: Own Exception
            return false;
        }
        else if (localCount == serverCount) {
            //logger.log(Level.INFO, "local- and serverDroneCount identical.");
            return false;
        }
        else if(localCount < serverCount) {
            LOGGER.info("Yes new data available");
            this.saveAsFile(URL, serverCount, filename);
            return true;
        }
        else {
            LOGGER.log(Level.WARNING, "localCount is greater than serverCount. Please check database");
        }
        return false;
    }
}
