/**
 * This class serves as the entry point of the application.
 * It initializes the application data and launches the main GUI components.
 */
package gui;

import logging.LogMain;
import data.*;
import processing.*;

import java.util.*;
import java.io.IOException;

public class ProgramStart {



    /**
     * The main method to start the application. It initializes data and sets up the GUI.
     *
     * @param args Command line arguments (not used in this application).
     * @throws IOException if there is an error in fetching or processing data.
     */
    public static void main(String[] args) throws IOException {
        LogMain lm = new LogMain();
        DataFactory factory = new DataFactory();
        ArrayList<DataStorage> data = factory.getDataStorage();


        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {DroneMenu.createDroneTableGUI(data);}});

        //Ich habe hier einen Thread hinzugefügt, der im Hintergrund (momentan) alle 5 Sekunden abfragt
        //ob es neue Daten auf dem Server gibt.
        //Falls es neue Daten gibt, wird ein Fenster erscheinen, um zu signalisieren, dass es neue Daten gibt.
        ThreadClass threadClass = new ThreadClass();
        threadClass.startDataUpdaterThread();
    }
}