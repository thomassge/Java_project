package data;

import java.util.ArrayList;

public class Singleton {
    private static Singleton instance;
    private Drone droneObject;
    private DroneType droneTypeObject;
    private ArrayList<DroneDynamics> droneDynamics;


    private Singleton() {
        initializeObjects();

    }

    private void initializeObjects() {
//        droneObject = new Drone(drones);
//        droneTypeObject = new DroneType(droneTypes);
//        droneDynamicObject = new DroneDynamics(droneDynamics);
    }

    private void sortInstance() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    };

    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        instance.droneObject.printDrone();
    }


}
