package Objects;

import java.util.LinkedList;

public class DroneType extends Drone{

    //DRONETYPE DATA
    private int droneTypeID;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maximumSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maximumCarriage;

    //GETTER-methods
    public int getDroneTypeID(){
        return this.droneTypeID;
    }
    public String getManufacturer(){
        return this.manufacturer;
    }
    public String getTypename(){
        return this.typename;
    }
    public int getWeight(){
        return this.weight;
    }
    public int getMaximumSpeed(){
        return this.maximumSpeed;
    }
    public int getBatteryCapacity(){
        return this.batteryCapacity;
    }
    public int getControlRange(){
        return this.controlRange;
    }
    public int getMaximumCarriage(){
        return this.maximumCarriage;
    }

    //PRINT-methods to test
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

    //Constructor
}
