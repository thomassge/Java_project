package data;
import data.*;

import java.util.ArrayList;

public class DroneClass {
    private Drone drone;
    private DroneType droneType;
    private ArrayList<DroneDynamics> droneDynamics;

    public DroneClass() {}

    public DroneClass(Drone drone, DroneType droneType, ArrayList<DroneDynamics> droneDynamics) {
        this.drone = drone;
        this.droneType = droneType;
        this.droneDynamics = droneDynamics;
    }

    public Drone getDrone() {
        return drone;
    }

    public DroneType getDroneType() {
        return droneType;
    }

    public ArrayList<DroneDynamics> getDroneDynamics() {
        return droneDynamics;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

    public void setDroneDynamics(ArrayList<DroneDynamics> droneDynamics) {
        this.droneDynamics = droneDynamics;
    }

}
