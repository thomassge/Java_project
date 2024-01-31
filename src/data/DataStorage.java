package data;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
/**
 * This class serves as a container for storing various types of drone data.
 * It encapsulates information about a drone, its type and its dynamics.
 */
public class DataStorage {
    private static final Logger LOGGER = Logger.getLogger(DataStorage.class.getName());

    private Drone drone;
    private DroneType droneType;
    private ArrayList<DroneDynamics> droneDynamicsList = new ArrayList<>();

    // GETTER AND SETTER
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

    public ArrayList<DroneDynamics> getDroneDynamicsList() {
        return droneDynamicsList;
    }
    public void setDroneDynamicsList(ArrayList<DroneDynamics> droneDynamicsList) {
        this.droneDynamicsList = droneDynamicsList;
    }
}
