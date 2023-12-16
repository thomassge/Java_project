package Rest;

import org.json.JSONObject;

public class DroneType {
    int droneTypeID;
    String manufacturer;
    String typename;
    int weight;
    int maximumSpeed;
    int batteryCapacity;
    int controlRange;
    int maximumCarriage;

    public DroneType() {};

    public DroneType(int droneTypeID, String manufacturer, String typename,  int weight, int maximumSpeed, int batteryCapacity, int controlRange, int maximumCarriage) {
        this.droneTypeID = droneTypeID;
        this.manufacturer = manufacturer;
        this.typename = typename;
        this.weight = weight;
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maximumCarriage = maximumCarriage;
    }

    public DroneType(String droneTypePointer, int i) {
        JSONObject droneTypeJSON = new JSONObject(droneTypePointer);
        System.out.println(droneTypeJSON.toString());
    }

    public void printDroneType() {
        System.out.println("Drone id: " + this.droneTypeID);
        System.out.println("___________________________________________");
        System.out.println("Serialnumber: " + this.manufacturer );
        System.out.println("Carriage Type: " + this.typename);
        System.out.println("Carriage weight: " + this.weight);
        System.out.println("DroneTypePointer: " + this.maximumSpeed);
        System.out.println("BatteryCapacity: " + this.batteryCapacity);
        System.out.println("Control Range (int): " + this.controlRange);
        System.out.println("Maximum Carriage: " + this.maximumCarriage);
        System.out.println("\n");
    }
}
