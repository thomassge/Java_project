package Rest;
public class Drone {
    String carriage_type;
    String serialnumber;
    String created;
    int carriageWeight;
    int id;
    String droneTypePointer;

    //KONSTRUKTOR
    public Drone() {};

    public Drone(String carriage_type, String serialnumber, String created, int carriageWeight, int id, String DroneTypePointer) {
        System.out.println("Drone Object created.");
        this.carriage_type = carriage_type;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = DroneTypePointer;
        printDrone(this);
    }

    //PRINT METHODE ZUR KONTROLLE
    public void printDrone(Drone object) {
        System.out.println("Drone id: " + object.id);
        System.out.println("___________________________________________");
        System.out.println("Serialnumber: " + object.serialnumber);
        System.out.println("Created: " + object.created);
        System.out.println("Carriage Type: " + object.carriage_type);
        System.out.println("Carriage weight: " + object.carriageWeight);
        System.out.println("DroneTypePointer: " + object.droneTypePointer);
        System.out.println("\n");
    }

}
