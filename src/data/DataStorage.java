package data;

import java.util.ArrayList;
/**
 * This class functions as a blueprint for an object that holds all the data for a specific drone.
 * @author Leon Oet
 */
public class DataStorage {
    private Drone drone;
    private DroneType droneType;
    private ArrayList<DroneDynamics> droneDynamicsList = new ArrayList<>();

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
