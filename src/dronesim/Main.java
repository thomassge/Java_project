/**
 * This is our main class. The starting point of our program.
 */
package dronesim;

import data.*;
import processing.JSONDeruloHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    private static JSONDeruloHelper jsonDerulo = new JSONDeruloHelper();
    private static LinkedList<Drone> drones = new LinkedList<Drone>();
    private static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();

    public static void main(String[] args) throws IOException {
        drones = jsonDerulo.getData();
        droneTypes = jsonDerulo.getDroneTypes();

        for(Drone object : drones) {
            System.out.println(object.getDroneTypeObject().getTypename());
            System.out.println(object.getDroneTypeObject().getControlRange() + "\n");
        }

        jsonDerulo.refresh(drones, droneTypes);

//        drones.getFirst().saveAsFile();
//        droneTypes.getFirst().saveAsFile();
//        drones.getFirst().getDroneDynamicsArrayList().getFirst().saveAsFile();
//        can be used for refresh but refetches ALL data instead of using offset method
    }

}