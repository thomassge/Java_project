package dronesim;
import data.*;
import processing.JSONDeruloHelper;

import java.io.IOException;
import java.util.LinkedList;

public class Main {
    private static JSONDeruloHelper jsonDerulo = new JSONDeruloHelper();
    private static LinkedList<Drone> drones = new LinkedList<Drone>();
    private static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();

    public static void main(String[] args) throws IOException {
        //option to call saveDroneDynamicsInFile
        drones = jsonDerulo.getDrones();
        for(Drone droneObject : drones) {
            droneObject.print();
        }


        droneTypes = jsonDerulo.getDroneTypes();
        for(DroneType droneTypeObject : droneTypes) {
            droneTypeObject.printDroneType();
        }

        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);

        jsonDerulo.addDroneDynamicsData(drones);

        drones.getFirst().getDroneDynamicsArrayList().getFirst().printDroneDynamics();

//        droneTypes = jsonDerulo.getDroneTypes();
//        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);
//        jsonDerulo.addDroneDynamicsData(drones);
//        jsonDerulo.refresh(drones, droneTypes);           ->  case noNewData: do nothing;     case NewData: append new data to local json file;

    }

}