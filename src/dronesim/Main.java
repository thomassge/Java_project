package dronesim;
import data.*;
import processing.JSONDerulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    private static JSONDerulo jsonDerulo = new JSONDerulo();
    private static LinkedList<Drone> drones = new LinkedList<Drone>();
    private static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();

    public static void main(String[] args) throws IOException {
        //option to call saveDroneDynamicsInFile
        drones = jsonDerulo.getDrones();
        droneTypes = jsonDerulo.getDroneTypes();
        Drone.droneTypeToDroneLinker(droneTypes, drones);
        jsonDerulo.addDroneDynamicsData(drones);


        jsonDerulo.refresh(drones, droneTypes);

        drones.get(0).getDroneDynamicsArrayList().get(1).printDroneDynamics();





    }

}