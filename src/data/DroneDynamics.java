package data;

import data.enums.Status;
import processing.Initializable;
import processing.JsonFile;
import util.Streamer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
/**
 * This class represents the dynamics of a drone, including several metrics such as
 * speed, alignment, location and battery status. It is capable of checking for new data updates,
 * saving data to a file and maintaining a count of drone dynamics data from both local and server sources.
 */
public class DroneDynamics extends JsonFile implements Initializable<ArrayList<DroneDynamics>> {
    private static final Logger LOGGER = Logger.getLogger(DroneDynamics.class.getName());

    //DRONEDYNAMICS DATA
    private String dronePointer;
    private String timestamp;
    private int speed;
    private float alignmentRoll;
    private float alignmentPitch;
    private float alignmentYaw;
    private double longitude;
    private double latitude;
    private int batteryStatus;
    private String lastSeen;
    private Status status;

    /**
     * The number of entries in file, on the server and in memory.
     */
    private static int localCount;
    private static int serverCount;
    private static int memoryCount;

    /**
     * The filename where we store downloaded data
     */
    private final static String filename = "dronedynamics.json";

    /**
     * DroneDynamics API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/dronedynamics/";

    //CONSTRUCTORS
    /**
     * Default constructor for creating an instance of DroneDynamics.
     */
    public DroneDynamics() {LOGGER.log(Level.INFO, "DroneDynamics Object Created from empty constructor.");}

    /**
     * Parameterized constructor for creating an instance of DroneDynamics with specified attributes.
     *
     * @param dronePointer       Reference to the associated drone.
     * @param timestamp          Timestamp of the dynamics data.
     * @param speed              Speed of the drone.
     * @param alignmentRoll      Roll alignment of the drone.
     * @param alignmentPitch     Pitch alignment of the drone.
     * @param alignmentYaw       Yaw alignment of the drone.
     * @param longitude          Longitude of the drone's location.
     * @param latitude           Latitude of the drone's location.
     * @param batteryStatus      Current battery status of the drone.
     * @param lastSeen           Last seen timestamp of the drone.
     * @param status             Current status of the drone.
     */
    public DroneDynamics(String dronePointer, String timestamp, int speed, float alignmentRoll, float alignmentPitch, float alignmentYaw, double longitude, double latitude, int batteryStatus, String lastSeen, Status status) {
        this.dronePointer = dronePointer;
        this.timestamp = timestamp;
        this.speed = speed;
        this.alignmentRoll = alignmentRoll;
        this.alignmentPitch = alignmentPitch;
        this.alignmentYaw = alignmentYaw;
        this.longitude = longitude;
        this.latitude = latitude;
        this.batteryStatus = batteryStatus;
        this.lastSeen = lastSeen;
        this.status = status;
    }

    //GETTER METHODS
    public String getDronePointer() {
        return this.dronePointer;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public int getSpeed() {
        return this.speed;
    }

    public float getAlignmentRoll() {
        return this.alignmentRoll;
    }

    public float getAlignmentPitch() {
        return this.alignmentPitch;
    }

    public float getAlignmentYaw() {
        return this.alignmentYaw;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public int getBatteryStatus() {
        return this.batteryStatus;
    }

    public String getLastSeen() {
        return this.lastSeen;
    }

    public Status getStatus() {
        return this.status;
    }

    //STATIC GETTER AND SETTER METHODS
    public static int getLocalCount() {
        return localCount;
    }
    public static void setLocalCount(int localCount) {
        DroneDynamics.localCount = localCount;
    }

    public static int getServerCount() {
        return serverCount;
    }
    public static void setServerCount(int serverCount) {
        DroneDynamics.serverCount = serverCount;
    }

    public static int getMemoryCount() {
        return memoryCount;
    }
    private static void setMemoryCount(int memoryCount) {
        DroneDynamics.memoryCount = memoryCount;
    }

    public static String getFilename() {
        return filename;
    }

    public static String getUrl() {
        return URL;
    }

    private Status mapStatus(String status) {
        return switch (status) {
            case "ON" -> Status.ON;
            case "OF" -> Status.OF;
            case "IS" -> Status.IS;
            default -> throw new IllegalArgumentException("Invalid status value: " + status);
        };
    }

    //OTHER METHODS

    /**
     * Checks if new data is available by comparing local data count with server data count.
     * This method determines whether a new drone dynamics data needs to be fetched from the server.
     * @return true if new data is available, false otherwise.
     */
    public boolean isNewDataAvailable() {
        createFile(filename);
        if(serverCount == 0) {
            LOGGER.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
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
     * Provides a summary of the drone's battery status over a period.
     * This method calculates the duration for which the drone remains in a specific status
     * (e.g. ON, OFF) based on the drone dynamics entries.
     * @param data                  The list of DataStorage objects containing drone dynamics information.
     * @param droneCounter          The index of the drone in the data list.
     * @param droneDynamicsEntry    The initial index for the drone dynamics entry to start the calculation.
     * @return                      A string message summarizing the drone's battery status over time.
     */
    public String printBatteryInformation(ArrayList<DataStorage> data, int droneCounter, int droneDynamicsEntry) {
        if(data.get(droneCounter).getDroneDynamicsList().get(droneDynamicsEntry) == data.get(droneCounter).getDroneDynamicsList().getLast()) {
            return "Last Entry";
        }
        int infoInMinutes = 0;
        if(this.getStatus() == Status.OF){
            while(data.get(droneCounter).getDroneDynamicsList().get(droneDynamicsEntry).getStatus() == Status.OF) {
                droneDynamicsEntry++;
                infoInMinutes++;
            }
            return "Drone rests " + infoInMinutes + "more minutes until battery is recharged.";
        } else if (this.getStatus() == Status.ON){
            while(data.get(droneCounter).getDroneDynamicsList().get(droneDynamicsEntry).getStatus() == Status.ON) {
                droneDynamicsEntry++;
                infoInMinutes++;
            }
            return "Drone flies " + infoInMinutes + " more minutes until battery is empty.";
        }
        else {
            return "Drone has Issues. Please check the drone.";
        }
    }

    /**
     * Creates and initializes a list of DroneDynamics objects.
     * @return An ArrayList of initialized DroneDynamics objects.
     */
    public static ArrayList<DroneDynamics> create() {
        return new DroneDynamics().initialise();
    }

    /**
     * Initializes the list of DroneDynamics objects from a JSON file.
     * The method reads data from a JSON file, parses it and creates DroneDynamics objects based on the parsed data.
     * @return An ArrayList of DroneDynamics objects initialized with the data from the JSON file.
     */
    @Override
    public ArrayList<DroneDynamics> initialise() {
        ArrayList<DroneDynamics> droneDynamics = new ArrayList<>();
        String jsonString = new Streamer().reader(filename);
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            droneDynamics.add(new DroneDynamics(
                    o.getString("drone"),
                    o.getString("timestamp"),
                    o.getInt("speed"),
                    o.getFloat("align_roll"),
                    o.getFloat("align_pitch"),
                    o.getFloat("align_yaw"),
                    o.getDouble("longitude"),
                    o.getDouble("latitude"),
                    o.getInt("battery_status"),
                    o.getString("last_seen"),
                    this.mapStatus(o.getString("status"))
            ));
        }
        setMemoryCount(getMemoryCount() + jsonArray.length());
        return droneDynamics;
    }
}
