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
        this.carriage_type = carriage_type;
        this.serialnumber = serialnumber;
        this.created = created;
        this.carriageWeight = carriageWeight;
        this.id = id;
        this.droneTypePointer = DroneTypePointer;
    }

    public void printDrone() {
        System.out.println("Drone id: " + this.id);
        System.out.println("___________________________________________");
        System.out.println("Serialnumber: " + this.serialnumber);
        System.out.println("Created: " + this.created);
        System.out.println("Carriage Type: " + this.carriage_type);
        System.out.println("Carriage weight: " + this.carriageWeight);
        System.out.println("DroneTypePointer: " + this.droneTypePointer);
        System.out.println("\n");
    }

    public static void main(String[] args) {
    }

}
