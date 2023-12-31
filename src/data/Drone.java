package data;

import processing.JSONDerulo;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class Drone {

    //INDIVIDUALDRONE DATA
    private String droneTypePointer;
    private String serialnumber;
    private int id;
    private int extractedDroneTypeID;
    private int carriageWeight;
    private String carriageType;
    private String created;

    private DroneType droneTypeObject;

    public ArrayList<DroneDynamics> droneDynamicsArrayList;

    //KONSTRUKTOR
    public Drone() {
        System.out.println("Drone Object Created from empty constructor.");
    };
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

    public DroneType getDroneTypeObject() {return this.droneTypeObject;}
    public String getDroneTypePointer(){
        return this.droneTypePointer;
    }
    public String getSerialnumber(){
        return this.serialnumber;
    }
    public int getId(){
        return this.id;
    }
    public int getCarriageWeight(){
        return this.carriageWeight;
    }
    public String getCarriageType(){
        return this.carriageType;
    }
    public String getCreated(){
        return this.created;
    }
    public int getExtractedDroneTypeID() { // code insists that there are max /99/ drones -> implement REGEX
        //CODE TO EXTRACT THE NUMBERS IN "http://dronesim.facets-labs.com/api/dronetypes/67/", MORE SPECIFICALLY EXTRACT THE CHARACTERS BETWEEN THE LAST TWO SLASHES
        String extractedString = this.droneTypePointer.substring(47,49);
        return Integer.valueOf(extractedString);
    }

    // METHOD TO LINK FITTING DRONETYPE OBJECT TO RIGHT DRONE OBJECT
    /*public static void droneTypeToDroneLinker(LinkedList<DroneType> droneTypes, LinkedList<Drone> drones) {
        int i = 0;
        for(Drone droneObjectThatNeedsDroneTypeInformation : drones) {
            System.out.println("Erste SChleife: " + i);
            int j = 0;
            for (Drone droneObject : drones) {
                System.out.println("Zweite Schleife: " + j);
                if (drones.get(i).getExtractedDroneTypeID() == (droneTypes.get(j).getDroneTypeID())) {
                    System.out.println("Hallo" + drones.get(i).droneTypePointer);
                    drones.get(i).droneTypeObject = droneTypes.get(j);
                    i++;
                    break; //break added
                }
                j++;
            }
        }
    } */
    public static void droneTypeToDroneLinker(LinkedList<DroneType> droneTypes, LinkedList<Drone> drones) {
        for(Drone droneObjectThatNeedsDroneTypeInformation : drones) {
            if(droneObjectThatNeedsDroneTypeInformation.droneTypeObject == null) {

                for (DroneType droneType : droneTypes) {
                    if (droneObjectThatNeedsDroneTypeInformation.getExtractedDroneTypeID() == (droneType.getDroneTypeID())) {

                        droneObjectThatNeedsDroneTypeInformation.droneTypeObject = droneType;
                        break; //break added
                    }
                }
            }
            else { continue; }
        }
    }

    // get id's or more data to check for duplicates, request wird eh gemacht
    public static int getCount() {
        String checkDrones = "https://dronesim.facets-labs.com/api/drones/?limit=1";
        String jsonDrones = JSONDerulo.jsonCreator(checkDrones);
        JSONObject droneJsonObject = new JSONObject(jsonDrones);
        return droneJsonObject.getInt("count");
    }

    //PRINT-METHODEN ZUR KONTROLLE
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

    public void printAllDroneInformation() {
        System.out.println("All the following Information is linked to the Drone " + this.droneTypeObject.getTypename() + " with the Serialnumber: " + this.serialnumber);

        System.out.println("Individual Drone Information: ");
        this.printDrone();

        System.out.println("DroneTypes Information: ");
        this.droneTypeObject.printDroneType();

        System.out.println("DroneDynamics Information: ");
        iterateThroughList(this.droneDynamicsArrayList);
    }

    public void iterateThroughList(ArrayList<DroneDynamics> myList) {
        for(int i = 0; i < myList.size(); i++) {
            myList.get(i).printDroneDynamics();
        }
    }
}