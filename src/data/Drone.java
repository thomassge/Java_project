/**
 * This package cointains classes related to drone data management
 */
package data;

import org.json.JSONObject;
import processing.JSONDeruloHelper;

//import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the class where all Drone Data will be saved and called from.
 * It contains all the information that is available on the webserver.
 */
public class Drone implements Printable, Expandable {

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
     * Saves the dronetype information of the drone that this object holds
     */
    private DroneType droneTypeObject;
    /**
     * Saves an arraylist of DroneDynamics objects that are linked to this drone
     */
    public ArrayList<DroneDynamics> droneDynamicsArrayList;
    /**
     * The number of entries of drones in the last downloaded local json file
     */
    private static int localDroneCount;
    /**
     * The number of entries of drones on the webserver
     */
    private static int serverDroneCount;

    /**
     *      KONSTRUKTOREN
     */

    /**
     * The default constructor prints that it has been called.
     */
    public Drone() {
        System.out.println("Drone Object Created from empty constructor.");
    }

    /**
     * This constructor takes in all the attributes
     * an individual drone can have, according to the webserver
     *
     * @param carriageType     Enum representing the type of carriage.
     *                         It consists of: SEN(sor), ACT(uator), NOT(hing)
     * @param serialnumber     A string representing the serialnumber of the drone
     * @param created          A string representing the date and time the entry was created
     * @param carriageWeight   How much additional weight the drone currently carries
     * @param id               The id is the index of the Drone on the webserver, currently going from 71-95
     * @param DroneTypePointer A string that contains a link to the DroneType information of this drone.
     */
    public Drone(String carriageType, String serialnumber, String created, int carriageWeight, int id, String DroneTypePointer) {
        System.out.println("Drone Object created.");

        this.carriageType = carriageType;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = DroneTypePointer;
        //printDrone(this);
    }

    //GETTER-Methods
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

    /**
     * This method uses regular expressions to find a sequence of numbers in the
     * String droneTypePointer, to be able to link the fitting DroneType object
     * to the Drone object
     *
     * @return The ID of the DroneType of the drone that calls the method
     */
    public int getExtractedDroneTypeID() {
        Pattern pattern = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.droneTypePointer);
        try {
            validateExtractedDroneTypeID(matcher.find());
        } catch (DroneTypeIdNotExtractableException e) {
            this.extractedDroneTypeID = 0;
        }
        return Integer.parseInt(matcher.group(0));
    }

    /**
     * @return The DroneType object of calling drone
     */
    public DroneType getDroneTypeObject() {
        return this.droneTypeObject;
    }

    /**
     * @return The DroneDynamics arrayList of calling drone
     */
    public ArrayList<DroneDynamics> getDroneDynamicsArrayList() {
        return this.droneDynamicsArrayList;
    }

    //SETTER-Methods
    /**
     * Takes in a DroneType object to set the value of this drones DroneType object
     *
     * @param droneTypeObject The dronetype of this drone
     */
    public void setDroneTypeObject(DroneType droneTypeObject) {
        this.droneTypeObject = droneTypeObject;
    }

    /**
     * Takes in an arraylilst of DroneDynamics objects to set the value of this drones DroneDynamics list
     *
     * @param droneDynamicsArrayList All dronedynamics of this drone
     */
    public void setDroneDynamicsArrayList(ArrayList<DroneDynamics> droneDynamicsArrayList) {
        this.droneDynamicsArrayList = droneDynamicsArrayList;
    }

    //PRINT-METHODEN ZUR KONTROLLE
    /**
     * Method that prints all individual Drone information.
     */
    public void printDrone() {
        System.out.println("Drone id: " + this.id);
        System.out.println("Serialnumber: " + this.serialnumber);
        System.out.println("___________________________________________");
        System.out.println("Created: " + this.created);
        System.out.println("Carriage Type: " + this.carriageType);
        System.out.println("Carriage weight: " + this.carriageWeight);
        System.out.println("DroneTypePointer: " + this.droneTypePointer);
        System.out.println("\n");
    }

    /**
     * Method that prints <b>all</b> drone information
     */
    public void printAllDroneInformation() {
        System.out.println("All the following Information is linked to the Drone " + this.droneTypeObject.getTypename() + " with the Serialnumber: " + this.serialnumber);

        System.out.println("Individual Drone Information: ");
        this.printDrone();

        System.out.println("DroneTypes Information: ");
        this.droneTypeObject.printDroneType();

        System.out.println("DroneDynamics Information: ");
        iterateThroughList(this.droneDynamicsArrayList);
    }

    /**
     * This method is used in the printAllDroneInformation.
     * It iterates through the DroneDynamics arraylist.
     * @param myList Arraylist of DroneDynamics
     */
    public void iterateThroughList(ArrayList<DroneDynamics> myList) {
        for(int i = 0; i < myList.size(); i++) {
            myList.get(i).printDroneDynamics();
        }
    }

    /**
     * This method validates whether getExtractedDroneTypeID found a number,
     * using the given regex. Throws an Exception otherwise.
     * @param boo True if it did and false otherwise.
     * @throws DroneTypeIdNotExtractableException The Exception that is thrown if there was no number found.
     */
    private static void validateExtractedDroneTypeID(boolean boo) throws DroneTypeIdNotExtractableException {
        if (!boo) {
            throw new DroneTypeIdNotExtractableException();
        }
    }

    @Override
    public void print() {
        printDrone();
    }

    /**
     * Uses the dronesim api url of drones with limit=1 to connect to the webserver,
     * create a json string and look for the key "count" in this string,
     * which represents the current count of all Drones available.
     * It limits the requests to a single entry to prevent a big download.
     *
     * @return The current count of Drones on the webserver.
     */
    @Override
    public int getServerCount() { //alternativ regex
        String checkDrones = "https://dronesim.facets-labs.com/api/drones/?limit=1";
        String jsonDrones = JSONDeruloHelper.jsonCreator(checkDrones);
        JSONObject droneJsonObject = new JSONObject(jsonDrones);

        return droneJsonObject.getInt("count");
    }

    /**
     * This method gets the count of drones that is saved in the local json file.
     * @return The number of Drone entries in the json file.
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int getLocalCount() throws IOException {
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
    }

    /**
     * This method compares the locally saved count of drones in the json file,
     * with the count of drones that is on the webserver.
     * @param serverDroneCount The number of drones on the webserver
     * @return True if the data differs, and false if the count is the same.
     * @throws FileNotFoundException If file is not found
     */
    @Override
    public boolean checkForNewData() throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader("drones.json"))) {
            serverDroneCount = getServerCount();
            localDroneCount = getLocalCount();

            if (serverDroneCount == localDroneCount) {
                return false;
            } else {
                System.out.println("damn, refetching");
                return true;
            }
        }
        catch (FileNotFoundException fnfE) {
            return true; // could be used for refresh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method starts by saving the current server count of drones in a variable.
     * Then it passes this variable to checkForNewData to decide whether new data has to be
     * fetched or not. If new data has to be fetched, it uses the localDroneCount as a limit,
     * appended to the String URL, to reduce the entries requested.
     *
     * THE PROBLEM WITH THIS METHTOD IS THAT IT CAN NOT DIFFERENTIATE WHETHER FOR EXAMPLE
     * AN ENTRY WAS DELETED AND A NEW ONE WAS ADDED. THE OFFSET METHOD ALSO FAILS IF DATA
     * IS NOT APPENDED, BUT RANDOMLY INSERTED, TO THE DATABASE.
     *
     * if anyone knows a way to catch these cases help a mf out
     */
    @Override
    public void saveAsFile() {
        try {
            if(!(checkForNewData())) {
                System.out.println("No New Drone Data to fetch from");
                return;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("New fetching started: Current server dronecount: " + serverDroneCount);
        String forCreatingDroneObjects = JSONDeruloHelper.jsonCreator(JSONDeruloHelper.getDronesUrl() + "?limit=" + serverDroneCount);

        System.out.println("Copying Drone Data from Webserver in file ...");

        try (PrintWriter out = new PrintWriter("drones.json")) {
            out.println(forCreatingDroneObjects);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}