package data;

import java.util.ArrayList;
import java.util.LinkedList;

public class DataStorage {

    private LinkedList<Drone> drones = new LinkedList<>();
    private LinkedList<DroneType> droneTypes = new LinkedList<>();
    private ArrayList<DroneDynamics> droneDynamics = new ArrayList<>();

    private ArrayList<DataStorage> data = new ArrayList<>();


    public DataStorage() {
        DataFactory factory = new DataFactory(this);
    }


    public ArrayList<DataStorage> getData() {
        return data;
    }

    public void setData(ArrayList<DataStorage> data) {
        this.data = data;
    }

    public LinkedList<Drone> getDrones() {
        return drones;
    }

    public void setDrones(LinkedList<Drone> drones) {
        this.drones = drones;
    }

    public LinkedList<DroneType> getDroneTypes() {
        return droneTypes;
    }

    public void setDroneTypes(LinkedList<DroneType> droneTypes) {
        this.droneTypes = droneTypes;
    }

    public ArrayList<DroneDynamics> getDroneDynamics() {
        return droneDynamics;
    }

    public void setDroneDynamics(ArrayList<DroneDynamics> droneDynamics) {
        this.droneDynamics = droneDynamics;
    }

    public static void main(String[] args) {
        ArrayList<DataStorage> data = new ArrayList<>();


        DataStorage dataStorage = new DataStorage();

        DroneType.droneTypeToDroneLinker(dataStorage.droneTypes, dataStorage.drones);

        DroneDynamics.droneDynamicsLinker(dataStorage);



        dataStorage.drones.getFirst().printDrone();

        int i = 0;
        int I = 2;
    }

}
