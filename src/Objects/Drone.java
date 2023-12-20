package Objects;

import java.util.LinkedList;

public class Drone {

    private static final int numberOfDrones = 20;

    //INDIVIDUALDRONE DATA
    private String droneTypePointer;
    private String serialnumber;
    private int id;
    private int carriageWeight;
    private String carriageType;
    private String created;

    public LinkedList<DroneDynamics> droneDynamicsLinkedList;

    //GETTER-Methods
    public static int getNumberOfDrones(){
        return numberOfDrones;
    }
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

    //KONSTRUKTOR
    public Drone() {};

    public Drone(String carriageType, String serialnumber, String created, int carriageWeight, int id, String DroneTypePointer) {
        //System.out.println("Drone Object created.");
        this.carriageType = carriageType;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = DroneTypePointer;
        //printDrone(this);
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

    public void printDroneDynamics(DroneDynamics object) {
        System.out.println("DronePointer: " + object.dronePointer);
        System.out.println("___________________________________________");
        System.out.println("Timestamp: " + object.timestamp);
        System.out.println("Speed: " + object.speed);
        System.out.println("Alignment Roll: " + object.alignmentRoll);
        System.out.println("DroneDynamics Control Range: " + object.droneDynamicsControlRange);
        System.out.println("Alignment Yaw: " + object.alignmentYaw);
        System.out.println("Longitude: " + object.longitude);
        System.out.println("Latitude: " + object.latitude);
        System.out.println("Battery Status: " + object.batteryStatus);
        System.out.println("Last Seen: " + object.lastSeen);
        //System.out.println("Status: " + DroneDynamics.Status.getStatus());
        System.out.println("\n");
    }

    public void printAllDroneInformation() {
        System.out.println("All the following Information is linked to the Drone " + this.typename + " with the Serialnumber: " + this.serialnumber);

        System.out.println("Individual Drone Information: ");
        this.printDrone();

        System.out.println("DroneTypes Information: ");
        this.printDroneType();

        System.out.println("DroneDynamics Information: ");
        iterateThroughList(this.droneDynamicsLinkedList);
    }

    public void iterateThroughList(LinkedList<DroneDynamics> myList) {
        for(int i = 0; i < myList.size(); i++) {
            printDroneDynamics(myList.get(i));
        }
    }
}
