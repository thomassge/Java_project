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

public class DroneDynamics {

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

    /**
     * The number of objects in memory
     */
    private static int memoryCount;

    /**
     * The filename where we store downloaded data
     */
    private final static String filename = "dronedynamics.json";
    public static String getFilename() {
        return filename;
    }

    /**
     * Dronedynamics API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/dronedynamics/";

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

    public static String getUrl() {
        return URL;
    }

    public static void setMemoryCount(int memoryCount) {
        DroneDynamics.memoryCount = memoryCount;
    }

    public static Status mapStatus(String status) {
        return switch (status) {
            case "ON" -> Status.ON;
            case "OF" -> Status.OF;
            case "IS" -> Status.IS;
            default -> throw new IllegalArgumentException("Invalid status value: " + status);
        };
    }

//    /**
//     * Adds drone dynamics data to the provided list of drones.
//     *
//     * @param drones The list of drones to which the dynamics data will be added.
//     */
//    public static void addDroneDynamicsData(LinkedList<Drone> drones) {
//        checkForNewData(filename, URL, localCount, serverCount);
//
//        String myJson = Streamable.reader(filename);
//
//        JSONObject myJsonObject = new JSONObject(myJson);
//        JSONArray jsonArray = myJsonObject.getJSONArray("results");
//
//        // code insists that number of drones >= number of drones that have dronedynamics data (probably fine since every droneD entry has a drone url)
//        for (int z = 0; z < Drone.getMemoryCount(); z++) {
//            if (drones.get(z).droneDynamicsArrayList == null) {
//                drones.get(z).setDroneDynamicsArrayList(new ArrayList<DroneDynamics>());
//            }
//            String toCheck = "http://dronesim.facets-labs.com/api/drones/" + drones.get(z).getId() + "/";
//
//            for (int j = 0; j < jsonArray.length(); j++) {
//                JSONObject o = jsonArray.getJSONObject(j);
//
//                if (o.getString("drone").equals(toCheck)) {
//                    drones.get(z).droneDynamicsArrayList.add(new DroneDynamics(
//                            o.getString("drone"),
//                            o.getString("timestamp"),
//                            o.getInt("speed"),
//                            o.getFloat("align_roll"),
//                            o.getFloat("align_pitch"),
//                            o.getFloat("align_yaw"),
//                            o.getDouble("longitude"),
//                            o.getDouble("latitude"),
//                            o.getInt("battery_status"),
//                            o.getString("last_seen"),
//                            DroneDynamics.mapStatus(o.getString("status"))
//                    ));
//                }
//            }
//        }
//        setMemoryCount(getMemoryCount() + jsonArray.length());
//        //numberOfDroneDynamics = numberOfDroneDynamics + jsonArray.length(); // Update numberOfDroneDynamics if refresh() creates new DroneDynamics data
//    }

    public static void droneDynamicsLinker(DataStorage dataStorage) {
        for (int z = 0; z < Drone.getMemoryCount(); z++) {
            if (dataStorage.getDrones().get(z).droneDynamicsArrayList == null) {
                dataStorage.getDrones().get(z).setDroneDynamicsArrayList(new ArrayList<DroneDynamics>());
            }
            String toCheck = "http://dronesim.facets-labs.com/api/drones/" + dataStorage.getDrones().get(z).getId() + "/";
            for (int j = 0; j < DroneDynamics.getMemoryCount(); j++) {
                if (dataStorage.getDroneDynamics().get(j).getDronePointer().equals(toCheck)) {
                    dataStorage.setData(dataStorage);
                    dataStorage.getDrones().get(z).droneDynamicsArrayList.add(dataStorage.getDroneDynamics().get(j));
                }
            }

        }
    }

    public static void initialize(String jsonString, ArrayList<DroneDynamics> droneDynamics) {
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
}
