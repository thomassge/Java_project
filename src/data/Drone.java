package data;
/**
 * This package contains classes related to drone data management.
 */

import data.enums.CarriageType;
import data.exceptions.DroneTypeIdNotExtractableException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class holds all individual Drone data that can be retrieved from the webserver.
 */
public class Drone implements Saveable, Refreshable {
    private static final Logger logger = Logger.getLogger(Drone.class.getName());

    //INDIVIDUAL DRONE DATA
    private int id;
    private String created;
    private int carriageWeight;
    private String serialnumber;
    private String droneTypePointer;
    private int extractedDroneTypeID;
    private CarriageType carriageType;
    /**
     * The number of entries in file, on the server and in memory.
     */
    private static int localCount;
    private static int serverCount;
    private static int memoryCount;
    /**
     * The filename where we store downloaded data
     */
    private final static String filename = "drones.json";
    /**
     * Drones API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/drones/";

    //CONSTRUCTORS
    /**
     * Default constructor for the Drone class.
     */
    public Drone() {
        logger.log(Level.INFO, "Drone Object Created from empty constructor.");
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
    public Drone(CarriageType carriageType, String serialnumber, String created, int carriageWeight, int id, String droneTypePointer) {
        logger.log(Level.INFO, "Drone Object created.");
        this.carriageType = carriageType;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = droneTypePointer;
    }

    //GETTER METHODS
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
            logger.log(Level.WARNING, "Error extracting the DroneTypeID", e);
            this.extractedDroneTypeID = 0;
        }
        return Integer.parseInt(matcher.group(0));
    }

    public CarriageType getCarriageType(){
        return this.carriageType;
    }

   //STATIC GETTER AND SETTER METHODS
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

    public static int getMemoryCount() {
        return memoryCount;
    }
    public static void setMemoryCount(int memoryCount) {
        Drone.memoryCount = memoryCount;
    }

    public static String getFilename() {
        return filename;
    }

    public static String getUrl() {
        return URL;
    }

    //ENUM METHOD
    public static CarriageType mapCarriageType(String carriageType) {
        return switch (carriageType) {
            case "ACT" -> CarriageType.ACT;
            case "SEN" -> CarriageType.SEN;
            case "NOT" -> CarriageType.NOT;
            default -> throw new IllegalArgumentException("Invalid CarriageType value: " + carriageType);
        };
    }

    //OTHER METHODS
    public static LinkedList<Drone> initialize(String jsonString) {
        LinkedList<Drone> drones = new LinkedList<Drone>();
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            drones.add(new Drone(
                    Drone.mapCarriageType(o.getString("carriage_type")),
                    o.getString("serialnumber"),
                    o.getString("created"),
                    o.getInt("carriage_weight"),
                    o.getInt("id"),
                    o.getString("dronetype")
            ));
        }
        setMemoryCount(getMemoryCount() + jsonArray.length());
        return drones;
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
            logger.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
        }
        return false;
    }

    public void iterateThroughList(ArrayList<DroneDynamics> myList) {
        for (int i = 0; i < myList.size(); i++) {
            myList.get(i).printDroneDynamics();
        }
    }

    private static void validateExtractedDroneTypeID(boolean boo) throws DroneTypeIdNotExtractableException {
        if (!boo) {
            throw new DroneTypeIdNotExtractableException();
        }
    }

    //PRINT METHODS
    public void printDrone() {
        logger.log(Level.INFO,"Drone id: " + this.id);
        logger.log(Level.INFO,"Serialnumber: " + this.serialnumber);
        logger.log(Level.INFO,"___________________________________________");
        logger.log(Level.INFO,"Created: " + this.created);
        logger.log(Level.INFO,"Carriage Type: " + this.carriageType);
        logger.log(Level.INFO,"Carriage weight: " + this.carriageWeight);
        logger.log(Level.INFO,"DroneTypePointer: " + this.droneTypePointer);
        logger.log(Level.INFO,"\n");
    }

    public void printAllDroneInformation() {
        logger.log(Level.INFO,"Individual Drone Information: ");
        this.printDrone();
        logger.log(Level.INFO,"DroneTypes Information: ");
        logger.log(Level.INFO,"DroneDynamics Information: ");
    }
}
