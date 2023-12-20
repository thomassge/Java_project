package Rest;

import java.util.LinkedList;

public class Drone {

    static final int numberOfDrones = 20;

    //INDIVIDUALDRONE DATA
    String droneTypePointer;
    String serialnumber;
    int id;
    int carriageWeight;
    String carriageType;
    String created;

    //DRONETYPE DATA
    public LinkedList<DroneDynamics> droneDynamicsLinkedList;
    int droneTypeID;
    String manufacturer;
    String typename;
    int weight;
    int maximumSpeed;
    int batteryCapacity;
    int controlRange;
    int maximumCarriage;

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

    public void printDroneType() {
        System.out.println("DroneType id: " + this.droneTypeID);
        System.out.println("___________________________________________");
        System.out.println("Manufacturer: " + this.manufacturer );
        System.out.println("TypeName: " + this.typename);
        System.out.println("Weight: " + this.weight);
        System.out.println("Maximum Speed: " + this.maximumSpeed);
        System.out.println("BatteryCapacity: " + this.batteryCapacity);
        System.out.println("Control Range (int): " + this.controlRange);
        System.out.println("Maximum Carriage: " + this.maximumCarriage);
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
