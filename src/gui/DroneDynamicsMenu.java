/**
 * This class is a GUI component that displays detailed information about
 * the dynamics of drones. It is responsible for creating and managing the GUI elements
 * that present drone dynamics data to the user.
 */
package gui;

import data.DataFactory;
import data.DataStorage;
import data.Drone;
import data.DroneDynamics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDynamicsMenu extends JPanel implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneDynamicsMenu.class.getName());
    private JFrame frame;
    JLabel selectedImage;
    JTextArea topRightText;
    JTextArea bottomLeftText;
    private JComboBox<Integer> droneIdDropdown;
    private LinkedList<Drone> drones;
    private DataFactory factory;

    /**
     * Constructs a new DroneDynamicsMenu with the specified list of drone dynamics.
     *
     * @param droneDynamicsArrayList An ArrayList of DroneDynamics objects.
     */
    public DroneDynamicsMenu(ArrayList<DataStorage> data) {
        super(new BorderLayout());

        LOGGER.info("Initializing DroneDynamicsMenu...");

        JFrame frame = createFrame();
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        //frame.getContentPane().add(droneDM);

        droneIdDropdown = new JComboBox<>();
        initializeDroneIdDropdown(data);
        droneIdDropdown.addActionListener(this);
        add(droneIdDropdown);

        createComponents();

        LOGGER.info("DroneDynamicsMenu initialized.");
    }

    private void initializeDroneIdDropdown(ArrayList<DataStorage> data) {
        for (int i = 0; i < data.size(); i++) {
            droneIdDropdown.addItem(data.get(i).getDrone().getId());
        }
    }
    private void createComponents() {
        JPanel topPanel = createTopPanel();
        JPanel gridPanel = createGridPanel();
        createTextAreas();

        add(gridPanel, BorderLayout.CENTER);
    }
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        droneIdDropdown.setMaximumSize(new Dimension(50, droneIdDropdown.getPreferredSize().height));
        droneIdDropdown.setSelectedIndex(0);
        topPanel.add(droneIdDropdown, BorderLayout.NORTH);
        return topPanel;
    }
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gridPanel.add(createTopPanel(), gbc);

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

        return gridPanel;
    }
    private void createTextAreas() {
        // Set up the text areas
        topRightText = new JTextArea();
        topRightText.setEditable(false);
        topRightText.setPreferredSize(new Dimension(120, 100));

        bottomLeftText = new JTextArea();
        bottomLeftText.setEditable(false);
        bottomLeftText.setPreferredSize(new Dimension(120, 100));
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
            createTextTopRight(factory.getDataStorage(), selectedDroneId);
        }
    }

    /**
     * Creates and updates the text information displayed int the top-right section of the panel.
     *
     * @param selectedDroneId The ID of the selected drone.
     */
    protected void createTextTopRight(ArrayList<DataStorage> data, int selectedDroneId) {
        LOGGER.info("Creating top right text for Drone ID: " + selectedDroneId);

        Drone selectedDrone = null;
        for (DataStorage dataStorage : data) { //data.getDrone();
            if (dataStorage.getDrone().getId() == selectedDroneId) {
                selectedDrone = dataStorage.getDrone();
                break;
            }
        }

        if(selectedDrone != null){
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

        String[] droneValues = {Integer.toString(selectedDroneId),
                selectedDrone.getSerialnumber(),
                selectedDrone.getDroneTypeObject().getTypename(),
                Double.toString(selectedDrone.getCarriageWeight()),
                String.valueOf(selectedDrone.getCarriageType())
        };

        StringBuilder text = new StringBuilder();
        text.append("Details:\n");
        for (int i = 0; i < droneAttributes.length; i++) {
            text.append(String.format("%s: %s\n", droneAttributes[i], droneValues[i]));
        }
        topRightText.setText(text.toString());

        // Bei selectedDroneId -70 -1 (weil Drohnen in ArrayList bei [0] anfangen
        DroneDynamics selectedDroneDynamic = data.get(0).getDroneDynamicsList().getFirst();
        String selectedTimestamp = null;
        createTextBottomLeft(selectedDroneId, selectedDroneDynamic,selectedTimestamp);

        LOGGER.info("Top right text created for Drone ID: " + selectedDroneId);
    }
    }

    /**
     * Creates and updates the text information displayed in the bottom-left section of the panel.
     *
     * @param selectedDroneId        The ID of the selected drone.
     * @param selectedDroneDynamic   The DroneDynamics object associated with the selected drone.
     */
    protected void createTextBottomLeft(int selectedDroneId, DroneDynamics selectedDroneDynamic,String selectedTimestamp) {
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
                "Longitude",
                "Latitude",
                "Alignment Pitch",
                "Alignment Roll",
                "Alignment Yaw"
        };

        String[] droneValues = {
                String.valueOf(selectedDroneDynamic.getBatteryStatus()),
                selectedDroneDynamic.getTimestamp(),
                String.valueOf(selectedDroneDynamic.getSpeed()),
                selectedDroneDynamic.getLastSeen(),
                String.valueOf(selectedDroneDynamic.getLongitude()),
                String.valueOf(selectedDroneDynamic.getLatitude()),
                String.valueOf(selectedDroneDynamic.getAlignmentPitch()),
                String.valueOf(selectedDroneDynamic.getAlignmentRoll()),
                String.valueOf(selectedDroneDynamic.getAlignmentYaw())
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

        URL imgURL = DroneDynamicsMenu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            LOGGER.log(Level.SEVERE, "Couldn't find file: " + path);
            return null;
        }
    }

    private JFrame createFrame(){
        frame = new JFrame("Drone Dynamics");

        frame.setContentPane(this);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }
    private JMenuBar createMenuBar() {
        LOGGER.info("Creating Menu Bar...");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem exitItem = new JMenuItem("Back");

        menuBar.add(menu);
        menu.add(exitItem);

        exitItem.addActionListener(e -> {
            if (frame != null) {
                frame.dispose();
            }
        });

        LOGGER.info("Menu Bar created.");
        return menuBar;
    }
    /**
     * Creates and displays an overview frame for drone dynamics.
     *
     * @param droneDynamicsArrayList An ArrayList of DroneDynamics objects.
     */
    public static void createDroneDynamicsOverview(ArrayList<DataStorage> data) {
        LOGGER.info("Creating DroneDynamicsOverview frame...");

        DroneDynamicsMenu droneDM = new DroneDynamicsMenu(data);
        //frame.getContentPane().add(droneDM);

        droneDM.createTextTopRight(data,(Integer) droneDM.droneIdDropdown.getSelectedItem());

        LOGGER.info("DroneDynamicsOverview frame created.");
    }
}