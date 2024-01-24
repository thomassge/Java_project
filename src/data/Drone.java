package data;
/**
 * This package contains classes related to drone data management.
 */

import data.enums.CarriageType;
import data.exceptions.DroneTypeIdNotExtractableException;
import org.json.JSONArray;
import org.json.JSONObject;
import processing.Initializable;
import util.Streamer;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class holds all individual Drone data that can be retrieved from the webserver.
 */
public class Drone implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(Drone.class.getName());

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
    public Drone(CarriageType carriageType, String serialnumber, String created, int carriageWeight, int id, String droneTypePointer) {
        LOGGER.log(Level.INFO, "Drone Object created.");
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
            LOGGER.log(Level.WARNING, "Error extracting the DroneTypeID", e);
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
    private CarriageType mapCarriageType(String carriageType) {
        return switch (carriageType) {
            case "ACT" -> CarriageType.ACT;
            case "SEN" -> CarriageType.SEN;
            case "NOT" -> CarriageType.NOT;
            default -> throw new IllegalArgumentException("Invalid CarriageType value: " + carriageType);
        };
    }

    //OTHER METHODS
//    public static LinkedList<Drone> initialize(String jsonString) {
//        LinkedList<Drone> drones = new LinkedList<Drone>();
//        JSONObject wholeHtml = new JSONObject(jsonString);
//        JSONArray jsonArray = wholeHtml.getJSONArray("results");
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject o = jsonArray.getJSONObject(i);
//            drones.add(new Drone(
//                    Drone.mapCarriageType(o.getString("carriage_type")),
//                    o.getString("serialnumber"),
//                    o.getString("created"),
//                    o.getInt("carriage_weight"),
//                    o.getInt("id"),
//                    o.getString("dronetype")
//            ));
//        }
//        setMemoryCount(getMemoryCount() + jsonArray.length());
//        return drones;
//    }

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
        LOGGER.log(Level.INFO,"Drone id: " + this.id);
        LOGGER.log(Level.INFO,"Serialnumber: " + this.serialnumber);
        LOGGER.log(Level.INFO,"Created: " + this.created);
        LOGGER.log(Level.INFO,"Carriage Type: " + this.carriageType);
        LOGGER.log(Level.INFO,"Carriage weight: " + this.carriageWeight);
        LOGGER.log(Level.INFO,"DroneTypePointer: " + this.droneTypePointer);
        LOGGER.log(Level.INFO,"\n");
    }

    public void printAllDroneInformation() {
        LOGGER.log(Level.INFO,"Individual Drone Information: ");
        this.printDrone();
        LOGGER.log(Level.INFO,"DroneTypes Information: ");
        LOGGER.log(Level.INFO,"DroneDynamics Information: ");
    }

    public static LinkedList<Drone> create() {
        return new Drone().initialise();
    }

    @Override
    public LinkedList<Drone> initialise() {
        LinkedList<Drone> drones = new LinkedList<Drone>();

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
        setMemoryCount(getMemoryCount() + jsonArray.length());
        return drones;
    }
}
