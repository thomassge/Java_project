package data;

import processing.Initializable;
import processing.JsonFile;
import util.Streamer;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;
/**
 * This class holds logic and fields that are linked to the DroneType information on the web server.
 * It is responsible for managing DroneType data.
 * @author Leon Oet
 */
public class DroneType extends JsonFile implements Initializable<LinkedList<DroneType>> {
    private static final Logger LOGGER = Logger.getLogger(DroneType.class.getName());

    private int droneTypeID;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maximumSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maximumCarriage;

    /**
     * The number of entries in file and on the server.
     */
    private static int localCount;
    private static int serverCount;

    /**
     * The file path/name where we store downloaded data
     */
    private final static String filename = "dronetypes.json";

    /**
     * Dronetypes API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/dronetypes/";

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
    public DroneType(int droneTypeID, String manufacturer, String typename, int weight, int maximumSpeed,
                     int batteryCapacity, int controlRange, int maximumCarriage) {
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

    public static String getFilename() {
        return filename;
    }

    public static String getUrl() {
        return URL;
    }

    /**
     * This method checks whether new data is available.
     * It starts off by creating a file or checking if a file with this name already exists.
     * It then compares the local and server counts to determine if a refresh is needed or not
     * In case it is needed, it overwrites the old file with the new data.
     * @return true if new data is available and false otherwise
     */
    public boolean isNewDataAvailable() {
        createFile(filename);
        if(serverCount == 0) {
            LOGGER.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
            //TODO: Own Exception
            return false;
        }
        else if (localCount == serverCount) {
            LOGGER.log(Level.INFO, "local- and serverDroneCount identical.");
            return false;
        }
        else if(localCount < serverCount) {
            LOGGER.log(Level.INFO,"Yes new data available");
            saveAsFile(URL, serverCount, filename);
            return true;
        }
        else {
            LOGGER.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
        }
        return false;
    }

    /**
     * This method is being overwritten from the Initializable interface.
     * It reads the data from the file and saves it in a LinkedList of its own datatype.
     * @return DroneType web server data as a LinkedList of its own datatype DroneTypes.
     */
    @Override
    public LinkedList<DroneType> initialize() {
        LinkedList<DroneType> droneTypes = new LinkedList<>();
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
        return droneTypes;
    }
}
