package data;

import org.json.JSONObject;
import processing.JSONDeruloHelper;

import java.io.*;
import java.util.logging.Logger;

public class DroneDynamics implements Printable, Expandable {

    private static final Logger LOGGER = Logger.getLogger(DroneDynamics.class.getName());

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

    public DroneDynamics() {
    }

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

    public void printDroneDynamics() {
        LOGGER.info("DronePointer: " + this.getDronePointer());
        LOGGER.info("Timestamp: " + this.getTimestamp());
        LOGGER.info("Speed: " + this.getSpeed());
        LOGGER.info("Alignment Roll: " + this.getAlignmentRoll());
        LOGGER.info("Alignment Pitch: " + this.getAlignmentPitch());
        LOGGER.info("Alignment Yaw: " + this.getAlignmentYaw());
        LOGGER.info("Longitude: " + this.getLongitude());
        LOGGER.info("Latitude: " + this.getLatitude());
        LOGGER.info("Battery Status: " + this.getBatteryStatus());
        LOGGER.info("Last Seen: " + this.getLastSeen());
    }

    @Override
    public void print() {
        printDroneDynamics();
    }

    @Override
    public boolean checkForNewData() throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader("dronedynamics.json"))) {
            localDroneDynamicsCount = getLocalCount();
            serverDroneDynamicsCount = getServerCount();

            if (serverDroneDynamicsCount != localDroneDynamicsCount) {
                LOGGER.warning("Data mismatch between local and server for DroneDynamics. Refetching data.");
                return true;
            }
            return false;
        } catch (FileNotFoundException fnfE) {
            LOGGER.severe("File not found exception while checking for DroneDynamics data.");
            return true;
        } catch (IOException e) {
            LOGGER.severe("IO exception occurred while checking for DroneDynamics data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getLocalCount() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("dronedynamics.json"));
        StringBuilder jsonContent = new StringBuilder();
        int limit = 20;
        int readChars = 0;
        int currentChar = 0;

        while ((currentChar = reader.read()) != -1 && readChars < limit) {
            jsonContent.append((char) currentChar);
            readChars++;
        }

        int fileDroneDynamicsCount = Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
        return fileDroneDynamicsCount;
    }

    @Override
    public void saveAsFile() {
        try {
            if (!(checkForNewData())) {
                LOGGER.info("No New DroneDynamics Data to fetch from");
                return;
            }
        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found exception while checking for new data.");
            throw new RuntimeException(e);
        }

        LOGGER.info("DroneDynamics Count: " + serverDroneDynamicsCount);
        String forCreatingDroneObjects = JSONDeruloHelper.jsonCreator(JSONDeruloHelper.getDroneDynamicsUrl() + "?limit=" + serverDroneDynamicsCount);

        LOGGER.info("Saving DroneDynamic Data from Webserver in file ...");

        try (PrintWriter out = new PrintWriter("dronedynamics.json")) {
            out.println(forCreatingDroneObjects);
        } catch (FileNotFoundException e) {
            LOGGER.severe("Error while saving DroneDynamics data to file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getServerCount() {
        String checkDroneDynamics = "https://dronesim.facets-labs.com/api/dronedynamics/?limit=1";
        String jsonDroneDynamics = JSONDeruloHelper.jsonCreator(checkDroneDynamics);
        JSONObject droneDynamicsJsonObject = new JSONObject(jsonDroneDynamics);
        return droneDynamicsJsonObject.getInt("count");
    }
}
