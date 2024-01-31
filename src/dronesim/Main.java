package dronesim;

import gui.DroneMenu;
import processing.ThreadClass;
import gui.WelcomeScreen;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.io.IOException;
/**
 * This is our main class and entry point of the program. This class is responsible for initializing
 * the program, fetching data and managing the overall flow of the application.
 * @Author: Leon Oet, Thomas Levantis
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    /**
     * The main method that serves as the entry point for the program.
     * It initializes data and manages the flow of execution.
     *
     * @param args Command line arguments (not used in this implementation).
     * @throws IOException if an I/O error occurs during data fetchhing.
     */
    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting the program...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeScreen();
            }
        });
        ThreadClass threadClass = new ThreadClass();
        threadClass.startDataUpdaterThread();
        LOGGER.info("Program execution completed.");
    }
}