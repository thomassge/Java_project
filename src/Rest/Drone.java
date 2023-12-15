package Rest;

import org.json.JSONObject;

public class Drone {

    //INDIVIDUALDRONE VARIABLES
    private static int idIndividualDrone;
    private static String dronetype;
    private static String created;
    private static String serialnumber;
    private static int carriageWeight;
    private static String carriageType;

    //DRONETYPES VARIABLES
    private static int idDroneType;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maxSpeed;
    private int batteryCapacity;
    private int controlRangeDroneType;
    private int maxCarriage;

    // DRONEDYNAMICS VARIABLES
    private static int idDroneDynamics;
    private String timestamp;
    private String drone;
    private int speed;
    private double alignmentRoll;
    private double controlRangeDroneDynamics;
    private double alignmentYaw;
    private double longitude;
    private double latitude;
    private int batteryStatus;
    private String lastSeen;
    private DroneStatus status;

    enum DroneStatus {
        ON,
        OF,
        IS;
    }

    //KONSTRUKTOR
    public Drone(int idIndividualDrone, String DroneType) {
        this.idIndividualDrone=idIndividualDrone;
        this.dronetype=dronetype;
    };
    public Drone() {};

    // GETTER METHODEN

    public static void main(String[] args) {
    }

}
