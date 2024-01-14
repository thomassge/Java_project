package data;
import data.*;

import java.util.ArrayList;

/**
 * New class for clean structuring of entries
 */
public class DroneClass {
    private Drone drone;
    private DroneType droneType;
    private ArrayList<DroneDynamics> droneDynamics;

public DroneClass(Drone drone, DroneType droneType, ArrayList<DroneDynamics> droneDynamics) {
    
}

    /**
     * GETTER AND SETTER METHODS
     *
     */
    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public DroneType getDroneType() {
        return droneType;
    }

    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

    public ArrayList<DroneDynamics> getDroneDynamics() {
        return droneDynamics;
    }

    public void setDroneDynamics(ArrayList<DroneDynamics> droneDynamics) {
        this.droneDynamics = droneDynamics;
    }
}
