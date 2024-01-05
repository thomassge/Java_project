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


        System.out.println(drones.get(0).getDroneDynamicsArrayList().getFirst().getLastSeen());
        System.out.println("here");

        return drones;
    }

    public static void main(String[] args) throws IOException {

        drones = getData();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {DroneMenu.createDroneTableGUI(drones);}});

    }

    /*
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
    //Click DroneType opens new class

    protected void opendronedWindow() {
        DroneDynamicsMenu droneDynamicsMenuWindow = new DroneDynamicsMenu();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                droneDynamicsMenuWindow.createAndShowGUI();
            }
        });

    }


    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);

        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);

        return contentPane;
    }
    */

}