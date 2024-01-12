/**
 * This class represents the type of a drone, including its invariable specifications and attributes.
 * It is responsible for managing drone type data, checking for updates and saving the data to a file.
 */
package data;

import org.json.JSONArray;
import org.json.JSONObject;
import processing.JSONDeruloHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneType extends AbstractDrone implements Expandable {

    private static final String DRONETYPES_URL = "https://dronesim.facets-labs.com/api/dronetypes/";
    private static final String filename = "dronetypes.json";

    @Override
    protected String reader(String url) {
        return super.reader(filename);
    }

    @Override
    protected String jsonCreator(String url) {
        return super.jsonCreator(DRONETYPES_URL);
    }

    protected ArrayList<DroneType> initialise(String jsonString) {
        ArrayList<DroneType> list = new ArrayList<>();
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            list.add(new DroneType(
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
        memoryObjectCount = memoryObjectCount + jsonArray.length(); // update numberOfDroneTypes if refresh() created new Drone objects
    return list;
    }

    public ArrayList<DroneType> getDroneTypes() {
        return initialise(jsonCreator(DRONETYPES_URL));
    }

    public DroneType(ArrayList <DroneType> droneTypes) {
        droneTypes = new ArrayList<>();
        JSONObject wholeHtml = new JSONObject(reader(filename));
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
        memoryObjectCount = memoryObjectCount + jsonArray.length(); // update numberOfDrones if refresh() created new Drone objects
    }




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
    private static int localDroneTypeCount;

    /**
     * The number of entries of drones on the webserver
     */
    private static int serverDroneTypeCount;

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

    /**
     * Gets the count of drone types entries from the server.
     *
     * @return The count of drone types entries on the server.
     */
    @Override
    public int getServerCount() {
        String checkDroneTypes = "https://dronesim.facets-labs.com/api/dronetypes/?limit=1";
        String jsonDroneTypes = JSONDeruloHelper.jsonCreator(checkDroneTypes);
        JSONObject droneTypeJsonObject = new JSONObject(jsonDroneTypes);
        return droneTypeJsonObject.getInt("count");
    }

    /**
     * Reads and returns the local count of drone types from a JSON file.
     *
     * @return The count of drone types in the local JSON file.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    @Override
    public int getLocalCount() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("dronetypes.json"));
            StringBuilder jsonContent = new StringBuilder();
            int limit = 20;
            int readChars = 0;
            int currentChar = 0;

            while ((currentChar = reader.read()) != -1 && readChars < limit) {
                jsonContent.append((char) currentChar);
                readChars++;
            }

            localDroneTypeCount = Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
            return localDroneTypeCount;
        } catch (Exception e) {
            logger.log(Level.INFO, "No LocalCount found: Creating DroneTypes JSON File.");
            return 0;
        }
    }

    /**
     * Checks for new drone type data by comparing local and server data counts.
     *
     * @return true if new data is available, false otherwise.
     * @throws FileNotFoundException if the local file is not found.
     */
    @Override
    public boolean checkForNewData() throws FileNotFoundException {
        try {
            localDroneTypeCount = getLocalCount();
            serverDroneTypeCount = getServerCount();

            if (serverDroneTypeCount == localDroneTypeCount) {
                return false;
            } else {
                logger.log(Level.INFO,"damn, refetching");
                return true;
            }
        } catch (FileNotFoundException fnfE) {
            logger.severe("File not found exception while checking for DroneType data.");
            return true;
        } catch (IOException e) {
            logger.severe("IO exception occurred while checking for DroneType data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the latest drone type data to a local file.
     */
    @Override
    public void saveAsFile() {
        try {
            if (!(checkForNewData())) {
                logger.info("No New DroneType Data to fetch from");
                return;
            }
        } catch (FileNotFoundException e) {
            logger.severe("File not found exception while checking for new data.");
            throw new RuntimeException(e);
        }

        logger.info("DroneTypes Count: " + serverDroneTypeCount);
        String forCreatingDroneTypeObjects = JSONDeruloHelper.jsonCreator(JSONDeruloHelper.getDroneTypesUrl() + "?limit=" + serverDroneTypeCount);

        logger.info("Saving DroneType Data from Webserver in file ...");

        try (PrintWriter out = new PrintWriter("dronetypes.json")) {
            out.println(forCreatingDroneTypeObjects);
        } catch (FileNotFoundException e) {
            logger.severe("File not found exception while saving DroneType data.");
            throw new RuntimeException(e);
        }
    }
}
