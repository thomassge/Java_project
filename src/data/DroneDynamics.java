/**
 * This class represents the dynamics of a drone, including several metrics such as
 * speed, alignment, location and battery status. It is capable of checking for new data updates,
 * saving data to a file and maintaining a count of drone dynamics data from both local and server sources.
 */
package data;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDynamics extends AbstractDroneOperations {

    private static final Logger logger = Logger.getLogger(DroneDynamics.class.getName());

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

    private static int localCount;
    private static int serverCount;

    /**
     * The number of objects in memory
     */
    private static int memoryCount;

    /**
     * The filename where we store downloaded data
     */
    public final static String filename = "dronedynamics.json";

    /**
     * Dronedynamics API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/dronedynamics/";
    public static String getUrl() {
        return URL;
    }

                                    //CONSTRUCTOR

    /**
     * Default constructor for creating an instance of DroneDynamics.
     */
    public DroneDynamics() {
    }

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

    public static int getMemoryCount() {
        return memoryCount;
    }

    public static void setMemoryCount(int memoryCount) {
        DroneDynamics.memoryCount = memoryCount;
    }

    public static Status mapStatus(String status) {
        switch (status) {
            case "ON":
                return Status.ON;
            case "OF":
                return Status.OF;
            case "IS":
                return Status.IS;
            default:
                throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

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

    @Override
    public void checkForNewData() {
        checkFile(filename);
        localCount = getLocalCount(filename, localCount);
        serverCount = getServerCount(URL);

        if(serverCount == 0) {
            logger.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
            //TODO: Own Exception
        }
        if (localCount == serverCount) {
            logger.log(Level.INFO, "local- and serverDroneCount identical.");
        }
        else if(localCount < serverCount) {
            saveAsFile(URL, serverCount, filename);
        }
        else {
            logger.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
        }
    }
}
