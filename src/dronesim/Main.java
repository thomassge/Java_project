/**
 * This is our main class. The starting point of our program.
 */
package dronesim;

import data.*;
import processing.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    private static JSONDeruloHelper jsonDerulo = new JSONDeruloHelper();
    private static LinkedList<Drone> drones = new LinkedList<Drone>();
    private static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();

    public static void main(String[] args) throws IOException {
        //option to call saveDroneDynamicsInFile
        drones = jsonDerulo.getDrones();
        droneTypes = jsonDerulo.getDroneTypes();
        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);
        jsonDerulo.addDroneDynamicsData(drones);

//        for(DroneType object : droneTypes) {
//            System.out.println(object.getTypename());
//            System.out.println(object.getControlRange() + "\n");
//        }

//        droneTypes = jsonDerulo.getDroneTypes();
//        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);
//        jsonDerulo.addDroneDynamicsData(drones);
//        jsonDerulo.refresh(drones, droneTypes); ->  case noNewData: do nothing;     case NewData: append new data to local json file;

    }

}