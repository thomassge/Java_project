/**
 * This class represents the dynamics of a drone, including several metrics such as
 * speed, alignment, location and battery status. It is capable of checking for new data updates,
 * saving data to a file and maintaining a count of drone dynamics data from both local and server sources.
 */
package data;

import org.json.JSONArray;
import org.json.JSONObject;
import processing.JSONDeruloHelper;
import processing.Streamable;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDynamics extends Objects implements Expandable, Streamable {

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
    private String status;

    private static int localDroneDynamicsCount;
    private static int serverDroneDynamicsCount;

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
    public DroneDynamics(String dronePointer, String timestamp, int speed, float alignmentRoll, float alignmentPitch, float alignmentYaw, double longitude, double latitude, int batteryStatus, String lastSeen, String status) {
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

    public String getStatus() {
        return this.status;
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

    /**
     * Gets the count of drone dynamics entries from the server.
     *
     * @return The count of drone dynamics entries on the server.
     */
    @Override
    public int getServerCount() {
        String checkDroneDynamics = "https://dronesim.facets-labs.com/api/dronedynamics/?limit=1";
        String jsonDroneDynamics = JSONDeruloHelper.jsonCreator(checkDroneDynamics);
        JSONObject droneDynamicsJsonObject = new JSONObject(jsonDroneDynamics);
        return droneDynamicsJsonObject.getInt("count");
    }

    /**
     * Gets the count of drone dynamic entries from the local JSON file.
     *
     * @return The count of drone dynamics in the local file.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    @Override
    public int getLocalCount() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("dronedynamics.json"));
            StringBuilder jsonContent = new StringBuilder();
            int limit = 20;
            int readChars = 0;
            int currentChar = 0;

            while ((currentChar = reader.read()) != -1 && readChars < limit) {
                jsonContent.append((char) currentChar);
                readChars++;
            }

            localDroneDynamicsCount = Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
            return localDroneDynamicsCount;
        } catch (Exception e) {
            logger.log(Level.INFO, "No LocalCount found: Creating Drones JSON File.");
            return 0;
        }
    }

    /**
     * Checks for new drone dynamics data by comparing local and server data counts.
     *
     * @return true if new data is available, false otherwise.
     * @throws FileNotFoundException if the local file is not found.
     */
    @Override
    public boolean checkForNewData() throws FileNotFoundException {
        try {
            localDroneDynamicsCount = getLocalCount();
            serverDroneDynamicsCount = getServerCount();

            if (serverDroneDynamicsCount == localDroneDynamicsCount) {
                return false;
            } else {
                logger.log(Level.INFO,"damn, refetching");
                return true;
            }
        } catch (FileNotFoundException fnfE) {
            logger.severe("File not found exception while checking for DroneDynamics data.");
            return true;
        } catch (IOException e) {
            logger.severe("IO exception occurred while checking for DroneDynamics data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the latest drone dynamics data to a local file.
     */
    @Override
    public void saveAsFile() {
        try {
            if (!(checkForNewData())) {
                logger.info("No New DroneDynamics Data to fetch from");
                return;
            }
        } catch (FileNotFoundException e) {
            logger.severe("File not found exception while checking for new data.");
            throw new RuntimeException(e);
        }

        logger.info("DroneDynamics Count: " + serverDroneDynamicsCount);
        String forCreatingDroneObjects = JSONDeruloHelper.jsonCreator(JSONDeruloHelper.getDroneDynamicsUrl() + "?limit=" + serverDroneDynamicsCount);

        logger.info("Saving DroneDynamic Data from Webserver in file ...");

        try (PrintWriter out = new PrintWriter("dronedynamics.json")) {
            out.println(forCreatingDroneObjects);
        } catch (FileNotFoundException e) {
            logger.severe("Error while saving DroneDynamics data to file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DroneDynamics> initialise(String jsonString) {
        ArrayList<DroneDynamics> list = new ArrayList<DroneDynamics>();
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            list.add(new DroneDynamics(
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
                    o.getString("status")
            ));
        }
        this.memoryObjectCount = this.memoryObjectCount + jsonArray.length(); // update numberOfDrones if refresh() created new Drone objects
    return list;
    }

    @Override
    public boolean update() {

    }

    /**
     * Fetches drone type data and converts it into DroneType objects.
     *
     * @return A LinkedList of DroneType objects.
     */
    public ArrayList<DroneDynamics> getDroneDynamics() {
        this.saveAsFile();

        String myJson = reader("dronedynamics.json");

        return initialise(myJson);
    }

}
