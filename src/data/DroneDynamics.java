package data;

import processing.JSONDerulo;
import org.json.JSONObject;

public class DroneDynamics {

    private String dronePointer;
    private String timestamp;
    private int speed;
    private float alignmentRoll;
    private float alignmentPitch;
    private float alignmentYaw;
    private double longitude;
    private double latitude;
    private int batteryStatus;
    private String lastSeen;
    private String status;

    //KONSTRUKTOR
    public DroneDynamics() {};
    public DroneDynamics(String dronePointer, String timestamp, int speed, float alignmentRoll, float droneDynamicsControlRange, float alignmentYaw, double longitude, double latitude, int batteryStatus, String lastSeen, String status) {
        //System.out.println("DroneDynamics Object created.");
        this.dronePointer = dronePointer;
        this.timestamp = timestamp;
        this.speed = speed;
        this.alignmentRoll = alignmentRoll;
        this.alignmentPitch = droneDynamicsControlRange;
        this.alignmentYaw = alignmentYaw;
        this.longitude = longitude;
        this.latitude = latitude;
        this.batteryStatus = batteryStatus;
        this.lastSeen = lastSeen;
        this.status = status;
    }

    //GETTER-Methods
    public String getDronePointer(){
        return this.dronePointer;
    }
    public String getTimestamp(){
        return this.timestamp;
    }
    public int getSpeed(){
        return this.speed;
    }
    public float getAlignmentRoll(){
        return this.alignmentRoll;
    }
    public float getAlignmentPitch(){
        return this.alignmentPitch;
    }
    public float getAlignmentYaw(){
        return this.alignmentYaw;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public int getBatteryStatus(){
        return this.batteryStatus;
    }
    public String getLastSeen(){
        return this.lastSeen;
    }
    public String getStatus() {
        return this.status;
    }

    enum Status{ // yet has to be implemented
        OF,
        ON,
        IS;
        public String getStatus() {
            return this.name();
        }
    }

    //METHODEN
    public static int getCount() {
        String checkDroneDynamics = "https://dronesim.facets-labs.com/api/dronedynamics/?limit=1";
        String jsonDroneDynamics = JSONDerulo.jsonCreator(checkDroneDynamics);
        JSONObject droneDynamicsJsonObject = new JSONObject(jsonDroneDynamics);
        return droneDynamicsJsonObject.getInt("count");
    }

    //PRINT METHODS WITH GETTER
    public void printDroneDynamics() {
        System.out.println("DronePointer: " + this.getDronePointer());
        System.out.println("___________________________________________");
        System.out.println("Timestamp: " + this.getTimestamp());
        System.out.println("Speed: " + this.getSpeed());
        System.out.println("Alignment Roll: " + this.getAlignmentRoll());
        System.out.println("DroneDynamics Control Range: " + this.getAlignmentPitch());
        System.out.println("Alignment Yaw: " + this.getAlignmentRoll());
        System.out.println("Longitude: " + this.getLongitude());
        System.out.println("Latitude: " + this.getLatitude());
        System.out.println("Battery Status: " + this.getBatteryStatus());
        System.out.println("Last Seen: " + this.getLastSeen());
        //System.out.println("Status: " + DroneDynamics.Status.getStatus());
        System.out.println("\n");
    }
}

