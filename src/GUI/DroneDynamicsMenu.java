/**
 * This class is a GUI component that displays detailed information about
 * the dynamics of drones. It is responsible for creating and managing the GUI elements
 * that present drone dynamics data to the user.
 */
package GUI;

import data.Drone;
import data.DroneDynamics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDynamicsMenu extends JPanel implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneDynamicsMenu.class.getName());

    JLabel selectedImage;
    JLabel fixedImage;
    JTextArea topRightText;
    JTextArea bottomLeftText;
    private JComboBox<Integer> droneIdDropdown;
    private LinkedList<Drone> drones;


    /**
     * Constructs a new DroneDynamicsMenu with the specified list of drone dynamics.
     *
     * @param droneDynamicsArrayList An ArrayList of DroneDynamics objects.
     */
    public DroneDynamicsMenu(ArrayList<DroneDynamics> droneDynamicsArrayList) {
        super(new BorderLayout());

        LOGGER.info("Initializing DroneDynamicsMenu...");


        droneIdDropdown = new JComboBox<>();

        for (int i=0;i<ProgramStart.drones.size();i++) {
            droneIdDropdown.addItem(ProgramStart.drones.get(i).getId());
        }
        droneIdDropdown.addActionListener(this);
        add(droneIdDropdown);

        // Set up the combo box and the selected image on top left
        JPanel topPanel = new JPanel(new BorderLayout());
        selectedImage = new JLabel();
        droneIdDropdown.setMaximumSize(new Dimension(50, droneIdDropdown.getPreferredSize().height));
        droneIdDropdown.setSelectedIndex(0);

        topPanel.add(selectedImage, BorderLayout.CENTER);
        topPanel.add(droneIdDropdown, BorderLayout.NORTH);

        // Set up the fixed image "Drone1.jpg" on bottom right
        fixedImage = new JLabel();
        ImageIcon fixedIcon = createImageIcon("images/image.jpg");
        if (fixedIcon != null) {
            fixedImage.setIcon(fixedIcon);
        }

        // Set up the text areas
        topRightText = new JTextArea();
        topRightText.setEditable(false);
        topRightText.setPreferredSize(new Dimension(120, 100));

        bottomLeftText = new JTextArea();
        bottomLeftText.setEditable(false);
        bottomLeftText.setPreferredSize(new Dimension(120, 100));

        // Add components to the panel using GridBagLayout
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gridPanel.add(topPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.gridheight = 1;

        gridPanel.add(topRightText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;

        gridPanel.add(bottomLeftText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 2;

        gridPanel.add(fixedImage, gbc);
        add(gridPanel, BorderLayout.CENTER);
        //updateLabel("Drone1");

        LOGGER.info("DroneDynamicsMenu initialized.");

    }

    /**
     * Handles action events, typically from user interaction.
     *
     * @param e The ActionEvent object representing the event.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {

            // Wenn ein neues Drone-ID ausgewählt wurde
            int selectedDroneId = (Integer) droneIdDropdown.getSelectedItem();

            LOGGER.info("Selected Drone ID: " + selectedDroneId);

            // Hier kannst du die Logik für die Aktualisierung der GUI basierend auf der ausgewählten Drone-ID implementieren
            LOGGER.info("Ausgewählte Drone-ID: " + selectedDroneId);
            createTextTopRight(selectedDroneId);
        }
    }
/*
    protected void updateLabel(String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".jpg");

        if (icon != null) {
            selectedImage.setIcon(icon);
            updateTopRightText(name);
        }

        updateBottomLeftText(); // Update dynamic text
    }
*/

    /**
     * Creates and updates the text information displayed int the top-right section of the panel.
     *
     * @param selectedDroneId The ID of the selected drone.
     */
    protected void createTextTopRight(int selectedDroneId) {
        LOGGER.info("Creating top right text for Drone ID: " + selectedDroneId);

        Drone selectedDrone = null;
        for (Drone drone : ProgramStart.drones) {
            if (drone.getId() == selectedDroneId) {
                selectedDrone = drone;
                break;
            }
        }

        // Initialize topRightText if not initialized
        if (topRightText == null) {
            topRightText = new JTextArea();
            topRightText.setEditable(false);
            topRightText.setPreferredSize(new Dimension(120, 100));
        }
        String[] droneAttributes = {"ID",
                                    "Serialnr",
                                    "DroneType",
                                    "Weight",
                                    "CarriageType"};

        String[] droneValues = { Integer.toString(selectedDroneId),
                                selectedDrone.getSerialnumber(),
                                selectedDrone.getDroneTypeObject().getTypename(),
                                Double.toString(selectedDrone.getCarriageWeight()),
                                selectedDrone.getCarriageType()
                                };

        StringBuilder text = new StringBuilder();
        text.append("Details:\n");
        for (int i = 0; i < droneAttributes.length; i++) {
            text.append(String.format("%s: %s\n", droneAttributes[i], droneValues[i]));
        }
        topRightText.setText(text.toString());

        // Bei selectedDroneId -70 -1 (weil Drohnen in ArrayList bei [0] anfangen
        DroneDynamics selectedDroneDynamic = ProgramStart.drones.get(selectedDroneId-70-1).getDroneDynamicsArrayList().getFirst();
        createTextBottomLeft(selectedDroneId, selectedDroneDynamic);

        LOGGER.info("Top right text created for Drone ID: " + selectedDroneId);
    }

    /**
     * Creates and updates the text information displayed in the bottom-left section of the panel.
     *
     * @param selectedDroneId        The ID of the selected drone.
     * @param selectedDroneDynamic   The DroneDynamics object associated with the selected drone.
     */
    protected void createTextBottomLeft(int selectedDroneId, DroneDynamics selectedDroneDynamic) {
        LOGGER.info("Creating bottom left text for Drone ID: " + selectedDroneId);

        // Initialize bottomLeftText if not initialized
        if (bottomLeftText == null) {
            bottomLeftText = new JTextArea();
            bottomLeftText.setEditable(false);
            bottomLeftText.setPreferredSize(new Dimension(120, 100));
        }

        String[] droneAttributes =  {
                                    "Battery Status",
                                    "Time Stamp",
                                    "Speed",
                                    "Last seen",
                                    };

        String[] droneValues = {
                String.valueOf(selectedDroneDynamic.getBatteryStatus()),
                selectedDroneDynamic.getTimestamp(),
                String.valueOf(selectedDroneDynamic.getSpeed()),
                selectedDroneDynamic.getLastSeen()
        };

        StringBuilder text = new StringBuilder();
        text.append("Details:\n");
        for (int i = 0; i < droneAttributes.length; i++) {
            text.append(String.format("%s: %s\n", droneAttributes[i], droneValues[i]));
        }
        bottomLeftText.setText(text.toString());

        LOGGER.info("Bottom left text created for Drone ID: " + selectedDroneId);
    }

    /**
     * Creates an ImageIcon from the specified path.
     *
     * @param path The path to the image file.
     * @return The created ImageIcon, or null if the path is invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        LOGGER.info("Attempting to create ImageIcon from path: " + path);

        java.net.URL imgURL = DroneDynamicsMenu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            LOGGER.log(Level.SEVERE, "Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Creates and displays an overview frame for drone dynamics.
     *
     * @param droneDynamicsArrayList An ArrayList of DroneDynamics objects.
     */
    public static void createDroneDynamicsOverview(ArrayList<DroneDynamics> droneDynamicsArrayList) {
        LOGGER.info("Creating DroneDynamicsOverview frame...");

        JFrame frame = new JFrame("Drone Dynamics");
        DroneDynamicsMenu droneDM = new DroneDynamicsMenu(droneDynamicsArrayList);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        JMenuItem back = new JMenuItem("Back");
        back.setMnemonic(KeyEvent.VK_B);

        menu.add(back);
        menuBar.add(menu);

        // This closes only the window, not the whole program
        back.addActionListener(e -> {
            if (frame != null) {
                frame.dispose();
            }
        });

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(droneDM);
        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        droneDM.createTextTopRight((Integer) droneDM.droneIdDropdown.getSelectedItem());

        LOGGER.info("DroneDynamicsOverview frame created.");
    }
}