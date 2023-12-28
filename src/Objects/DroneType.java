package Objects;

import fetching.JSONDerulo;
import org.json.JSONObject;

public class DroneType {

    //DRONETYPE DATA
    private int droneTypeID;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maximumSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maximumCarriage;

    //Constructor

    public DroneType() {}
    public DroneType(int droneTypeID, String manufacturer, String typename, int weight, int maximumSpeed, int batteryCapacity, int controlRange, int maximumCarriage) {
        System.out.println("DroneType Object created");
        this.droneTypeID = droneTypeID;
        this.manufacturer = manufacturer;
        this.typename = typename;
        this.weight = weight;
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maximumCarriage = maximumCarriage;
    }

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

    public static int getCount() {
        String checkDroneTypes = "https://dronesim.facets-labs.com/api/dronetypes/?limit=1";
        String jsonDroneTypes = JSONDerulo.jsonCreator(checkDroneTypes);
        JSONObject droneTypeJsonObject = new JSONObject(jsonDroneTypes);
        return droneTypeJsonObject.getInt("count");
    }

    //PRINT-methods to test without GETTER
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