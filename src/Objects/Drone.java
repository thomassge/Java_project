package Objects;

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

    public LinkedList<DroneDynamics> droneDynamicsLinkedList;

    //GETTER-Methods
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
    public int getExtractedDroneTypeID() {
        extractedDroneTypeID = 0;
        //CODE TO EXTRACT THE NUMBERS IN http://dronesim.facets-labs.com/api/dronetypes/67/, MORE SPECIFICALLY EXTRACT THE CHARACTERS BETWEEN THE LAST TWO SLASHES
        String extractedString = this.droneTypePointer.substring(47,49);
        return Integer.valueOf(extractedString);
    }

    //KONSTRUKTOR
    public Drone() {
        System.out.println("Drone Object created by default constructor.");
    };

    public Drone(String carriageType, String serialnumber, String created, int carriageWeight, int id, String DroneTypePointer) {
        System.out.println("Drone Object created.");
        this.carriageType = carriageType;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = DroneTypePointer;

        // APPROACH TO CREATE DRONETyPE OBJECTS INSIDE THE DRONE CONSTRUCTOR ------------------>>> und wie? :D -TL
        // CONNECT DATABASE AND GER DRONETYPEINFO
        //DroneType droneTypeObject = new DroneType();
        //printDrone(this);
    }

    // METHOD TO LINK FITTING DRONETYPE OBJECT TO RIGHT DRONE OBJECT
    public static void droneTypeToDroneLinker(DroneType[] droneTypes, Drone[] drones) {
        int i = 0;
        for(Drone droneObject : drones) {
            int j = 0;
            if(drones[i].getExtractedDroneTypeID() == (droneTypes[j].getDroneTypeID())) {
                drones[i].droneTypeObject = droneTypes[j];
                i++;
            };
            j++;
        }
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
        iterateThroughList(this.droneDynamicsLinkedList);
    }

    public void iterateThroughList(LinkedList<DroneDynamics> myList) {
        for(int i = 0; i < myList.size(); i++) {
            myList.get(i).printDroneDynamics();
        }
    }
}
