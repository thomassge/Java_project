package gui;

import util.JsonCreator;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * THIS CLASS IS NOT TO BE TAKEN INTO ACCOUNT WHEN GRADING THIS PROJECT.
 * IT IS NOT NECESSARY TO THE FUNCTIONALITY OF THE PROGRAM IN ANY WAY.
 * NONETHELESS, IT IS THE MOST IMPORTANT CLASS.
 * @author Json Derulo & friends
 */
public class CreditsMenu {
    private static final Logger LOGGER = Logger.getLogger(CreditsMenu.class.getName());
    /**
     * Creates and displays the credits menu. This method sets up the GUI components
     * and populates them with the credits.
     */
    public Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
    private int colorIndex = 0;

    public CreditsMenu() {
        new JsonCreator(".idea/libraries/group4special/09.wav");

        LOGGER.info("Creating Credits menu...");

        JFrame droneFrame = new JFrame("Credits");

        ImageIcon imageIcon = new ImageIcon("src/img.png");

        JLabel backgroundLabel = new JLabel(imageIcon);

        backgroundLabel.setBounds(0, 0, 1365, 905);

        backgroundLabel.setLayout(new GridBagLayout());

        addtoPanel(backgroundLabel, "FRA UAS - OOP Java", 0, 0);
        addtoPanel(backgroundLabel, "Professor: Prof. Dr. Müller-Bady", 0, 1);
        addtoPanel(backgroundLabel, "Drone Project by: Group 4", 0, 2);
        addtoPanel(backgroundLabel, "Eyüp Korkmaz - matr.no. 1448519", 0, 3);
        addtoPanel(backgroundLabel, "Leon Oet - matr.no. 1267114", 0, 4);
        addtoPanel(backgroundLabel, "Marc O. Difflipp – matr.no. 1028010", 0, 5);
        addtoPanel(backgroundLabel, "Robin Remines - matr.no. 1459883", 0, 6);
        addtoPanel(backgroundLabel, "Thomas Levantis – matr.no. 1429473", 0, 7);

        droneFrame.getContentPane().add(backgroundLabel);
        droneFrame.setSize(1365, 905);
        droneFrame.setLocationRelativeTo(null);
        droneFrame.setVisible(true);

        LOGGER.info("Credits menu created.");
    }

    /**
     * Adds a label with specified text to a given panel at the specified grid position.
     *
     * @param panel       The JPanel to which the label should be added.
     * @param labelText   The text to be displayed on the label.
     * @param gridx       The grid x position on the panel.
     * @param gridy       The grid y position on the panel.
     */
    public void addtoPanel(JLabel panel, String labelText, int gridx, int gridy){
        LOGGER.info("Adding label to Credits panel: " + labelText);

        JLabel label = new JLabel(labelText);
        label.setForeground(colors[colorIndex]); // Set the text color to white
        label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 40));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(label, gbc);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorIndex = (colorIndex + 1) % colors.length;
                label.setForeground(colors[colorIndex]);
            }
        });
        timer.start();
        LOGGER.info("Label added successfully.");
    }

    /**
     * Quits the application.
     */
    protected void quit() {
        LOGGER.info("Exiting the application.");
        System.exit(0);
    }
}