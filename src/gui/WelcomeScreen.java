package gui;

import data.DataStorage;
import data.DroneType;
import processing.DataFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * This class creates a JFrame with a welcome slogan and a button to open DroneMenu.
 * @author Robin Remires
 */
public class WelcomeScreen extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(WelcomeScreen.class.getName());
    private final ArrayList<DataStorage> data;
    private final LinkedList<DroneType> droneTypes;
    /**
     * Constructor for WelcomeScreen.
     * Initializes the GUI window with all components.
     */
    public WelcomeScreen() {

        DataFactory factory = new DataFactory();
        data = factory.getDataStorage();
        droneTypes = factory.getDroneTypes();
        setTitle("Welcome to the Drone Simulator!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupBackground();
        setupLayout();
        setSize(800, 550);
        setLocationRelativeTo(null);
        setVisible(true);
        LOGGER.log(Level.INFO,"Welcome Screen opened...");
    }
    /**
     * Restarts the DroneMenu GUI.
     * Typically used to refresh the GUI when new data is available.
     */
    public static void restarter(){
        showMessageDialog(null, "New Data available.\nGUI will restart now...");
        DroneMenu.droneMenuFrame.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeScreen();
            }
        });
    }
    private void setupBackground() {
        setContentPane(new BackgroundPanel());
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        addSpacer(constraints);
        addWelcomeLabel(constraints);
        addDroneMenuButton(constraints);
    }

    private void addSpacer(GridBagConstraints constraints) {
        JLabel spacer = new JLabel();
        spacer.setPreferredSize(new Dimension(0, 55));
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(spacer, constraints);
    }

    private void addWelcomeLabel(GridBagConstraints constraints) {
        JLabel helloLabel = new JLabel("Welcome to the Drone Simulator!", SwingConstants.CENTER);
        helloLabel.setFont(new Font("Arial", Font.BOLD, 34));
        helloLabel.setForeground(Color.BLACK);
        helloLabel.setOpaque(false);
        constraints.gridy = 1;
        add(helloLabel, constraints);
        LOGGER.log(Level.INFO,"Welcome Label added...");
    }

    private void addDroneMenuButton(GridBagConstraints constraints) {
        JButton droneMenuButton = new JButton("Drone Menu");
        droneMenuButton.addActionListener((ActionEvent e) -> {
            new DroneMenu(data, droneTypes);
            dispose();
        });
        constraints.gridy = 2;
        add(droneMenuButton, constraints);
    }

    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        /**
         * Constructor for BackgroundPanel.
         * Loads the background image to be displayed.
         */
        public BackgroundPanel() {
            loadImage();
        }
        private void loadImage() {
            try {
                URL imageUrl = getClass().getResource("welcomeScreenImage.jpg");
                if (imageUrl != null) {
                    BufferedImage img = ImageIO.read(imageUrl);
                    backgroundImage = img.getScaledInstance(-1, -1, Image.SCALE_SMOOTH);
                } else {
                    LOGGER.log(Level.SEVERE,"Image could not be find.");
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING,"WelcomeScreen Image could not be loaded.");
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }
}