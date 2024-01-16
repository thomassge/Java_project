/**
 * This package contains classes related to drone data management.
 */
package data;

import data.enums.CarriageType;
import data.exceptions.DroneTypeIdNotExtractableException;
import processing.Initializing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the class where all Drone Data will be saved and called from.
 * It contains all the information that is available on the webserver.
 */
public class Drone extends AbstractDroneOperations implements Initializing {
    private static final Logger logger = Logger.getLogger(Drone.class.getName());

    /**
     * INDIVIDUALDRONE DATA
     */
    private String droneTypePointer;
    private String serialnumber;
    private int id;
    private int extractedDroneTypeID;
    private int carriageWeight;
    private String created;
    private CarriageType carriageType;

    /**
     * Saves the dronetype information of the drone that this object holds.
     */
    private DroneType droneTypeObject;

    /**
     * Saves an arraylist of DroneDynamics objects that are linked to this drone.
     */
    public ArrayList<DroneDynamics> droneDynamicsArrayList;

    /**
     * The number of entries of drones in the last downloaded local json file.
     */
    private static int localCount;

    /**
     * The number of entries of drones on the webserver.
     */
    private static int serverCount;

    /**
     * The number of Objects in memory
     */
    private static int memoryCount;

    /**
     * The filename where we store downloaded data
     */
    public final static String filename = "drones.json";

    /**
     * Drones API Endpoint
     */
    private static final String URL = "https://dronesim.facets-labs.com/api/drones/";
    public static String getUrl() {
        return URL;
    }


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
     * @param DroneTypePointer Link to the DroneType information of this drone.
     */
    public Drone(CarriageType carriageType, String serialnumber, String created, int carriageWeight, int id, String DroneTypePointer) {
        logger.log(Level.INFO, "Drone Object created.");
        this.carriageType = carriageType;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = DroneTypePointer;
    }

    // GETTER-Methods

    public String getDroneTypePointer() {
        return this.droneTypePointer;
    }

    public String getSerialnumber() {
        return this.serialnumber;
    }

    public int getId() {
        return this.id;
    }

    public int getCarriageWeight() {
        return this.carriageWeight;
    }

    public CarriageType getCarriageType(){
        return this.carriageType;
    }

    public String getCreated() {
        return this.created;
    }

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

    public DroneType getDroneTypeObject() {
        return this.droneTypeObject;
    }

    public ArrayList<DroneDynamics> getDroneDynamicsArrayList() {
        return this.droneDynamicsArrayList;
    }

    public static int getMemoryCount() {
        return memoryCount;
    }

    // SETTER-Methods

    public void setDroneTypeObject(DroneType droneTypeObject) {
        this.droneTypeObject = droneTypeObject;
    }

    public void setDroneDynamicsArrayList(ArrayList<DroneDynamics> droneDynamicsArrayList) {
        this.droneDynamicsArrayList = droneDynamicsArrayList;
    }

    public static void setMemoryCount(int memoryCount) {
        Drone.memoryCount = memoryCount;
    }

    public static CarriageType mapCarriageType(String carriageType) {
        return switch (carriageType) {
            case "ACT" -> CarriageType.ACT;
            case "SEN" -> CarriageType.SEN;
            case "NOT" -> CarriageType.NOT;
            default -> throw new IllegalArgumentException("Invalid CarriageType value: " + carriageType);
        };
    }
    // PRINT-METHODEN ZUR KONTROLLE

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
        logger.log(Level.INFO,"All the following Information is linked to the Drone " + this.droneTypeObject.getTypename() + " with the Serialnumber: " + this.serialnumber);
        logger.log(Level.INFO,"Individual Drone Information: ");
        this.printDrone();
        logger.log(Level.INFO,"DroneTypes Information: ");
        this.droneTypeObject.printDroneType();
        logger.log(Level.INFO,"DroneDynamics Information: ");
        iterateThroughList(this.droneDynamicsArrayList);
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

    @Override
    public void refresh() {
    }

    @Override
    public void initialize() {
        
    }
}
