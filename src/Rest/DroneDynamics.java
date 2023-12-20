package Rest;

import java.util.LinkedList;

public class DroneDynamics {

    String dronePointer;
    String timestamp;
    int speed;
    float alignmentRoll;
    float droneDynamicsControlRange;
    float alignmentYaw;
    double longitude;
    double latitude;
    int batteryStatus;
    String lastSeen;
    enum Status{ // yet has to be implemented
        OF,
        ON,
        IS;
        public String getStatus() {
            return this.name();
        }
    }
    //KONSTRUKTOR
    public DroneDynamics() {};

    public DroneDynamics(String dronePointer, String timestamp, int speed, float alignmentRoll, float droneDynamicsControlRange, float alignmentYaw, double longitude, double latitude, int batteryStatus, String lastSeen, String status) {
        //System.out.println("DroneDynamics Object created.");
        this.dronePointer = dronePointer;
        this.timestamp = timestamp;
        this.speed = speed;
        this.alignmentRoll = alignmentRoll;
        this.droneDynamicsControlRange = droneDynamicsControlRange;
        this.alignmentYaw = alignmentYaw;
        this.longitude = longitude;
        this.latitude = latitude;
        this.batteryStatus = batteryStatus;
        this.lastSeen = lastSeen;
    }
}


