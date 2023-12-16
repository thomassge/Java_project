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

    //KONSTRUKTOR
    public DroneType() {};

    public DroneType(int droneTypeID, String manufacturer, String typename,  int weight, int maximumSpeed, int batteryCapacity, int controlRange, int maximumCarriage) {
        System.out.println("DroneType Object created: ");
        this.droneTypeID = droneTypeID;
        this.manufacturer = manufacturer;
        this.typename = typename;
        this.weight = weight;
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maximumCarriage = maximumCarriage;
        this.printDroneType();
    }

    // PRINT METHODE ZUR KONTROLLE
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
}
