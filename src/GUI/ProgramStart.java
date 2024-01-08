package GUI;

import data.*;
import processing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.IOException;

public class ProgramStart {

    public static JSONDeruloHelper helper = new JSONDeruloHelper();
    public static LinkedList<Drone> drones = new LinkedList<>();
    public static LinkedList<DroneType> droneTypes = new LinkedList<>();

    public static LinkedList<Drone> getData() throws IOException {
        drones = helper.getDrones();
        droneTypes = helper.getDroneTypes();
        helper.droneTypeToDroneLinker(droneTypes, drones);
        helper.addDroneDynamicsData(drones);

        return drones;
    }

    public static void main(String[] args) throws IOException {
        LogMain lm = new LogMain();
        drones = getData();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {DroneMenu.createDroneTableGUI(drones);}});

    }
}