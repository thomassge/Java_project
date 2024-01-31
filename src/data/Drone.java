package data;

import data.enums.CarriageType;
import data.exceptions.DroneTypeIdNotExtractableException;
import processing.Initializable;
import processing.JsonFile;

import util.Streamer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class holds logic and fields that are linked to the individual Drone information on the web server.
 * It is responsible for managing drone data.
 * @author Leon Oet
 */
public class Drone extends JsonFile implements Initializable<LinkedList<Drone>> {

    private static final Logger LOGGER = Logger.getLogger(Drone.class.getName());

    private static int localCount;
    private static int serverCount;
    private final static String filename = "drones.json";
    private static final String URL = "https://dronesim.facets-labs.com/api/drones/";

    private int id;
    private String created;
    private int carriageWeight;
    private String serialnumber;
    private String droneTypePointer;
    private CarriageType carriageType;

    /**
     * Default constructor for the Drone class.
     */
    public Drone() {
        LOGGER.log(Level.INFO, "Drone Object Created from empty constructor.");
    }

    /**
     * Constructor for the Drone class that initializes all attributes.
     *
     * @param carriageType     Enum representing the type of carriage.
     * @param serialnumber     Serial number of the drone.
     * @param created          Date and time when the entry was created.
     * @param carriageWeight   Additional weight the drone carries.
     * @param id               Index of the Drone on the webserver.
     * @param droneTypePointer Link to the DroneType information of this drone.
     */
    public Drone(CarriageType carriageType, String serialnumber, String created,
                 int carriageWeight, int id, String droneTypePointer) {
        LOGGER.log(Level.INFO, "Drone Object created.");
        this.carriageType = carriageType;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = droneTypePointer;
    }
    public static int getLocalCount() {
        return localCount;
    }
    public static void setLocalCount(int localCount) {
        Drone.localCount = localCount;
    }
    public static int getServerCount() {
        return serverCount;
    }
    public static void setServerCount(int serverCount) {
        Drone.serverCount = serverCount;
    }
    public static String getFilename() {
        return filename;
    }
    public static String getUrl() {
        return URL;
    }

    public int getId() {
        return this.id;
    }
    public String getCreated() {
        return this.created;
    }
    public int getCarriageWeight() {
        return this.carriageWeight;
    }
    public String getSerialnumber() {
        return this.serialnumber;
    }
    public String getDroneTypePointer() {
        return this.droneTypePointer;
    }
    public CarriageType getCarriageType(){
        return this.carriageType;
    }

    /**
     * This method is being overwritten from the Initializable interface.
     * It reads the data from the file and saves it in a LinkedList of its own datatype.
     * @return Drone web server data as a LinkedList of its own datatype Drone.
     */
    @Override
    public LinkedList<Drone> initialize() {
        LinkedList<Drone> drones = new LinkedList<>();
        String jsonString = new Streamer().reader(filename);
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            drones.add(new Drone(
                    this.mapCarriageType(o.getString("carriage_type")),
                    o.getString("serialnumber"),
                    o.getString("created"),
                    o.getInt("carriage_weight"),
                    o.getInt("id"),
                    o.getString("dronetype")
            ));
        }
        return drones;
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
            LOGGER.log(Level.WARNING, "ServerDroneCount is 0. Please check database");
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
     * Extracts the DroneTypeID from the DroneTypePointer via RegEx.
     * This helps to link the DroneType to the Drone.
     * @return Extracted DroneTypeID as integer
     */
    public int getExtractedDroneTypeID() {
        Pattern pattern = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.droneTypePointer);
        try {
            validateExtractedDroneTypeID(matcher.find());
        } catch (DroneTypeIdNotExtractableException e) {
            LOGGER.log(Level.WARNING, "Error extracting the DroneTypeID", e);
        }
        return Integer.parseInt(matcher.group(0));
    }

    private static void validateExtractedDroneTypeID(boolean isIdLegit) throws DroneTypeIdNotExtractableException {
        if (!isIdLegit) {
            throw new DroneTypeIdNotExtractableException();
        }
    }

    private CarriageType mapCarriageType(String carriageType) {
        return switch (carriageType) {
            case "ACT" -> CarriageType.ACT;
            case "SEN" -> CarriageType.SEN;
            case "NOT" -> CarriageType.NOT;
            default -> throw new IllegalArgumentException("Invalid CarriageType value: " + carriageType);
        };
    }
}
