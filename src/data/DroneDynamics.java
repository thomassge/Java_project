/**
 * This class represents the dynamics of a drone, including several metrics such as
 * speed, alignment, location and battery status. It is capable of checking for new data updates,
 * saving data to a file and maintaining a count of drone dynamics data from both local and server sources.
 */
package data;

import data.enums.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import processing.Streamable;

import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDynamics implements Saveable{
    private static final Logger logger = Logger.getLogger(DroneDynamics.class.getName());

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
    public DroneDynamics() {logger.log(Level.INFO, "DroneDynamics Object Created from empty constructor.");}

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
    public static void setMemoryCount(int memoryCount) {
        DroneDynamics.memoryCount = memoryCount;
    }

    public static String getFilename() {
        return filename;
    }

    public static String getUrl() {
        return URL;
    }

    //ENUM METHOD
    public static Status mapStatus(String status) {
        return switch (status) {
            case "ON" -> Status.ON;
            case "OF" -> Status.OF;
            case "IS" -> Status.IS;
            default -> throw new IllegalArgumentException("Invalid status value: " + status);
        };
    }

    //OTHER METHODS
    public static ArrayList<DroneDynamics> initialize(String jsonString) {
        ArrayList<DroneDynamics> droneDynamics = new ArrayList<DroneDynamics>();
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
                            DroneDynamics.mapStatus(o.getString("status"))
            ));
        }
        setMemoryCount(getMemoryCount() + jsonArray.length());
        return droneDynamics;
    }

    public static boolean isNewDataAvailable() {
        Saveable.createFile(filename);

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
            logger.info("Yes new data available");
            Saveable.saveAsFile(URL, serverCount, filename);
            return true;
        }
        else {
            logger.log(Level.WARNING, "localCount is greater than serverCount. Please check database");
        }
        return false;
    }

    public String printBatteryInformation(ArrayList<DataStorage> data, int drone, int counter) {
        int i =0;
        if(this.getStatus() == Status.OF){
            while(data.get(drone).getDroneDynamicsList().get(counter).getStatus() == Status.OF) {
                counter++;
                i++;
            }
            return "Drone rests " + i + "more minutes until battery is recharged.";
        } else if (this.getStatus() == Status.ON){
            while(data.get(drone).getDroneDynamicsList().get(counter).getStatus() == Status.ON) {
                counter++;
                i++;
            }
            return "Drone flies " + i + "more minutes until battery is empty.";
        }
        else {
            return "Drone has Issues. Please check the drone.";
        }
    }

    //PRINT METHODS
    /**
     * Prints detailed drone dynamics information to the log.
     */
    public void printDroneDynamics() {
        logger.info("DronePointer: " + this.getDronePointer());
        logger.info("Timestamp: " + this.getTimestamp());
        logger.info("Speed: " + this.getSpeed());
        logger.info("Alignment Roll: " + this.getAlignmentRoll());
        logger.info("Alignment Pitch: " + this.getAlignmentPitch());
        logger.info("Alignment Yaw: " + this.getAlignmentYaw());
        logger.info("Longitude: " + this.getLongitude());
        logger.info("Latitude: " + this.getLatitude());
        logger.info("Battery Status: " + this.getBatteryStatus());
        logger.info("Last Seen: " + this.getLastSeen());
    }
}
