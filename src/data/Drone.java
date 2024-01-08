/**
 * This package contains classes related to drone data management.
 */
package data;

import org.json.JSONObject;
import processing.JSONDeruloHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the class where all Drone Data will be saved and called from.
 * It contains all the information that is available on the webserver.
 */
public class Drone implements Printable, Expandable {
    private static final Logger logger = Logger.getLogger(Drone.class.getName());


    /**
     * INDIVIDUALDRONE DATA
     */
    private String droneTypePointer;
    private String serialnumber;
    private int id;
    private int extractedDroneTypeID;
    private int carriageWeight;
    private String carriageType;
    private String created;

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
    private static int localDroneCount;

    /**
     * The number of entries of drones on the webserver.
     */
    private static int serverDroneCount;

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
    public Drone(String carriageType, String serialnumber, String created, int carriageWeight, int id, String DroneTypePointer) {
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

    public String getCarriageType() {
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

    // SETTER-Methods

    public void setDroneTypeObject(DroneType droneTypeObject) {
        this.droneTypeObject = droneTypeObject;
    }

    public void setDroneDynamicsArrayList(ArrayList<DroneDynamics> droneDynamicsArrayList) {
        this.droneDynamicsArrayList = droneDynamicsArrayList;
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
    public void print() {
        printDrone();
    }

    @Override
    public int getServerCount() {
        try{
        String checkDrones = "https://dronesim.facets-labs.com/api/drones/?limit=1";
        String jsonDrones = JSONDeruloHelper.jsonCreator(checkDrones);
        JSONObject droneJsonObject = new JSONObject(jsonDrones);
        return droneJsonObject.getInt("count");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving the server count", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getLocalCount() throws IOException {
        try{
        BufferedReader reader = new BufferedReader(new FileReader("drones.json"));
        StringBuilder jsonContent = new StringBuilder();
        int limit = 20;
        int readChars = 0;
        int currentChar = 0;

        while ((currentChar = reader.read()) != -1 && readChars < limit) {
            jsonContent.append((char) currentChar);
            readChars++;
        }

        localDroneCount = Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
        return localDroneCount;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Abrufen des lokalen Counts", e);
            throw new IOException(e);
        }
    }

    @Override
    public boolean checkForNewData() throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader("drones.json"))) {
            serverDroneCount = getServerCount();
            localDroneCount = getLocalCount();

            if (serverDroneCount == localDroneCount) {
                return false;
            } else {
                logger.log(Level.INFO,"damn, refetching");
                return true;
            }
        } catch (FileNotFoundException fnfE) {
            logger.log(Level.SEVERE, "Error checking new data", fnfE);
            return true; // could be used for refresh
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error checking new data", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAsFile() {
        try {
            if (!(checkForNewData())) {
                logger.log(Level.INFO,"No New Drone Data to fetch from");
                return;
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error saving data", e);
            throw new RuntimeException(e);
        }

        logger.log(Level.INFO,"New fetching started: Current server dronecount: " + serverDroneCount);
        String forCreatingDroneObjects = JSONDeruloHelper.jsonCreator(JSONDeruloHelper.getDronesUrl() + "?limit=" + serverDroneCount);

        logger.log(Level.INFO,"Copying Drone Data from Webserver in file ...");

        try (PrintWriter out = new PrintWriter("drones.json")) {
            out.println(forCreatingDroneObjects);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
