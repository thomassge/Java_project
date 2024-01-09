/**
 * This class provides a graphical interface to display the credits for
 * the drone project. It creates a window with a list of contributors and their details.
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreditsMenu {

    // Ein Logger für diese Klasse erstellen
    private static final Logger LOGGER = Logger.getLogger(CreditsMenu.class.getName());

    //Click Credits

    /**
     * Creates and displays the credits menu. This method sets up the GUI components
     * and populates them with the credits information.
     */
    public static void createCreditList() {
        LOGGER.info("Creating Credits menu...");

        JFrame droneFrame = new JFrame("Credits");
        JPanel panel = new JPanel();
        droneFrame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addtoPanel(panel, "FRA UAS - OOP Java", 0, 0);
        addtoPanel(panel, "Professor: Prof. Dr. Müller-Bady", 0, 1);
        addtoPanel(panel, "Drone Project by: Group 4", 0, 2);
        addtoPanel(panel, "Eyüp Korkmaz - matr.no. 1448519", 0, 3);
        addtoPanel(panel, "Leon Oet - matr.no. 1267114", 0, 4);
        addtoPanel(panel, "Marc O. Difflipp – matr.no. 1028010", 0, 5);
        addtoPanel(panel, "Robin Remines - matr.no. 1459883", 0, 6);
        addtoPanel(panel, "Thomas Levantis – matr.no. 1429473", 0, 7);

        droneFrame.setSize(300, 300);
        droneFrame.setLocationRelativeTo(null);
        droneFrame.setVisible(true);

        LOGGER.info("Credits menu created.");
    }

    //for Credit content alignment

    /**
     * Adds a label with specified text to a given panel at the specified grid position.
     *
     * @param panel       The JPanel to which the label should be added.
     * @param labelText   The text to be displayed on the label.
     * @param gridx       The grid x position on the panel.
     * @param gridy       The grid y position on the panel.
     */
    public static void addtoPanel(JPanel panel, String labelText, int gridx, int gridy){
        LOGGER.info("Adding label to Credits panel: " + labelText);

        JLabel label = new JLabel(labelText);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(label, gbc);

        LOGGER.info("Label added successfully.");
    }

    //quitfkt

    /**
     * Quits the application.
     */
    protected void quit() {
        LOGGER.info("Exiting the application.");
        System.exit(0);
    }
}
