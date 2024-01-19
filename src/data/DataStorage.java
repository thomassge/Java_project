package data;

import gui.DroneMenu;

import java.util.ArrayList;
import java.util.logging.Logger;

public class DataStorage {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    private Drone drone;
    private DroneType droneType;
    private ArrayList<DroneDynamics> droneDynamicsList = new ArrayList<>();



    public DataStorage() {
    }


    public ArrayList<DataStorage> getData() {
        ArrayList<DataStorage> data = new ArrayList<>();
        return data;
    }

//    public ArrayList<DroneType> getDroneTypeArrayList(ArrayList<DataStorage> data) {
//        ArrayList<DroneType> droneTypeArrayList = new ArrayList<>();
//        for(int i = 0; i < DroneType.getLocalCount(); i++) {
//            droneTypeArrayList.add(data.get);
//            LOGGER.info("LocalCount:" + localCount);
//        }
//
//    }

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
