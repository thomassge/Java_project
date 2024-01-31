package gui;

import data.DataStorage;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import services.GoogleMaps;
import util.JsonCreator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static javax.swing.JOptionPane.showMessageDialog;
/**
 * This class provides a graphical user interface for displaying a list of all available drone dynamics data
 * @Author: Thomas Levantis, Eyüp Korkmaz, Marco Difflipp
 */
public class DroneDynamicsMenu implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneDynamicsMenu.class.getName());
    private ArrayList<DataStorage> data;
    private JFrame droneDynamicsFrame;
    private JMenuBar droneDynamicsMenuBar;
    private JPanel droneDynamicsgridPanel;
    private JComboBox<Integer> droneIdDropdown;
    private JTextArea droneTypeLabel;
    private JPanel droneTypePanel;
    private JTextArea droneDynamicsLabel;
    private JPanel droneDynamicsPanel;
    private int selectedArrayListValue = 0;
    private int selectedDroneId;
    private JPanel droneDynamicsButtonPanel;
    private JPanel droneDynamicsUserPanel;

    public DroneDynamicsMenu(ArrayList<DataStorage> data){
        new JsonCreator();
        this.data = data;
        LOGGER.info("Initializing DroneDynamicsMenu...");
        createFrame();
        createMenuBar();
        createGridPanel(data);
        droneDynamicsFrame.setJMenuBar(droneDynamicsMenuBar);
        droneDynamicsFrame.add(droneDynamicsgridPanel);
        if (droneIdDropdown.getItemCount() > 0) {
            droneIdDropdown.setSelectedIndex(0);
        }
        LOGGER.info("DroneDynamicsMenu initialized.");
    }

    private void createFrame(){
        droneDynamicsFrame = new JFrame("Drone Dynamics");
        droneDynamicsFrame.setSize(800, 550);
        droneDynamicsFrame.setLocationRelativeTo(null);
        droneDynamicsFrame.setVisible(true);
        LOGGER.log(Level.INFO,"Frame created...");
    }

    private void createMenuBar() {
        LOGGER.info("Creating Menu Bar...");
        droneDynamicsMenuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem droneDynamicsExit = new JMenuItem("Back");
        droneDynamicsMenuBar.add(menu);
        menu.add(droneDynamicsExit);
        droneDynamicsExit.addActionListener(e -> {
            if (droneDynamicsFrame != null) {
                droneDynamicsFrame.dispose();
            }
        });
        LOGGER.info("Menu Bar created.");
    }

    private void createGridPanel(ArrayList<DataStorage> data){
        droneDynamicsgridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        createUserPanel(data);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.2;
        constraints.fill = GridBagConstraints.BOTH;
        droneDynamicsgridPanel.add(droneDynamicsUserPanel, constraints);
        createDroneTypePanel();
        constraints.gridy = 1;
        constraints.weighty = 0.4;
        droneDynamicsgridPanel.add(droneTypePanel, constraints);
        createDroneDynamicsPanel();
        constraints.gridy = 2;
        constraints.weighty = 0.4;
        droneDynamicsgridPanel.add(droneDynamicsPanel, constraints);
        createButtons();
        constraints.gridy = 3;
        constraints.weighty = 0;
        droneDynamicsgridPanel.add(droneDynamicsButtonPanel, constraints);
        LOGGER.info("GridPanel created...");
    }

    private void createUserPanel(ArrayList<DataStorage> data){
        droneDynamicsUserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        droneIdDropdown = new JComboBox();
        initializeDroneIdDropdown(data);
        JButton googleMapsButton = new JButton("Open Google Maps");
        googleMapsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGoogleMaps();
            }
        });
        droneDynamicsUserPanel.add(droneIdDropdown);
        droneDynamicsUserPanel.add(googleMapsButton);
        droneDynamicsgridPanel.add(droneDynamicsUserPanel);
        LOGGER.info("User-Panel created...");
    }

    private void initializeDroneIdDropdown(ArrayList<DataStorage> data) {
        for (int selectedDrone = 0; selectedDrone < data.size(); selectedDrone++) {
            droneIdDropdown.addItem(data.get(selectedDrone).getDrone().getId());
        }
        droneIdDropdown.addActionListener(this);
        LOGGER.log(Level.INFO,"Data intialized...");
    }

    private void openGoogleMaps(){
        int selectedDrone = selectedDroneId - 71;
        if((selectedDrone >= 0) && (selectedDrone <= data.size()) ) {
            GoogleMaps mapCreator = new GoogleMaps();
            String filename = mapCreator.createPicture(
                    data.get(selectedDrone).getDroneDynamicsList().get(selectedArrayListValue).getLatitude(),
                    data.get(selectedDrone).getDroneDynamicsList().get(selectedArrayListValue).getLongitude()
            );
            JFrame droneDynamicsGoogleMapsFrame = new JFrame();
            String imagePath = filename;
            droneDynamicsGoogleMapsFrame.add(new JLabel(new ImageIcon((new ImageIcon(filename)).getImage().getScaledInstance(630, 600, java.awt.Image.SCALE_SMOOTH))));
            droneDynamicsGoogleMapsFrame.setSize(400, 400);
            droneDynamicsGoogleMapsFrame.setLocationRelativeTo(null);
            droneDynamicsGoogleMapsFrame.setVisible(true);
            LOGGER.log(Level.INFO,"GoogleMaps opened...");
        }
    }

    private void createDroneTypePanel(){
        droneTypePanel = new JPanel(new BorderLayout());
        droneTypeLabel = new JTextArea("Select Drone-ID for DroneType information!");
        droneTypeLabel.setEditable(false);
        droneTypePanel.add(droneTypeLabel, BorderLayout.CENTER);
        droneDynamicsgridPanel.add(droneTypePanel);
        LOGGER.info("DroneType Panel created...");
    }

    private void createDroneDynamicsPanel(){
        droneDynamicsPanel = new JPanel(new BorderLayout());
        droneDynamicsLabel = new JTextArea("Select Drone-ID for DroneDynamics information!");
        droneTypeLabel.setEditable(false);
        droneDynamicsPanel.add(droneDynamicsLabel, BorderLayout.CENTER);
        droneDynamicsgridPanel.add(droneDynamicsPanel);
        LOGGER.info("DroneDynamics Panel created...");
    }

    private void createButtons() {
        droneDynamicsButtonPanel = new JPanel(new GridLayout(2, 12));
        JButton[] plusButtons = new JButton[3];
        JButton[] minusButtons = new JButton[3];
        for(int selectedMinusButton = 0; selectedMinusButton<3; selectedMinusButton++ ){
            minusButtons[selectedMinusButton] = new JButton("-" + (int) Math.pow(10, selectedMinusButton));
            droneDynamicsButtonPanel.add(minusButtons[selectedMinusButton]);
        }
        for(int selectedPlusButton = 0; selectedPlusButton<3; selectedPlusButton++){
            plusButtons[selectedPlusButton] = new JButton("+" + (int) Math.pow(10, selectedPlusButton) );
            droneDynamicsButtonPanel.add(plusButtons[selectedPlusButton]);
        }
        for(int potency = 0; potency < 3; potency++){
            int valuePlus = (int) Math.pow(10, potency);
            int valueMinus = (int) Math.pow(10, potency);
            plusButtons[potency].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedArrayListValue += valuePlus;
                    if (selectedArrayListValue >= data.getFirst().getDroneDynamicsList().toArray().length){
                        outOfBoundCheck();
                    } else {
                        selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                        displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
                        LOGGER.log(Level.INFO,"selectedArrayListValue successfully incremented...");
                    }
                }
            });
            minusButtons[potency].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedArrayListValue -= valueMinus;
                    if (selectedArrayListValue < 0){
                        outOfBoundCheck();
                    } else {
                        selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                        displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
                        LOGGER.log(Level.INFO,"selectedArrayListValue successfully decremented...");
                    }
                }
            });
        }
        droneDynamicsgridPanel.add(droneDynamicsButtonPanel);
        LOGGER.info("Buttons created...");
    }

    private void outOfBoundCheck(){
        showMessageDialog(null, "TimeStamp out of Bound!\nFirst TimeStamp shown now!");
        selectedArrayListValue = 0;
        displayDroneDynamicsInformation(data, (int) droneIdDropdown.getSelectedItem(), selectedArrayListValue);
        LOGGER.log(Level.SEVERE,"selectedArrayListValue out of Bound!");
    }

    /**
     * Method is invoked when an action is performed on elements like dropdowns and buttons.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {
            selectedDroneId = (int) droneIdDropdown.getSelectedItem();
            LOGGER.info("Selected Drone-ID: " + selectedDroneId);
            if (selectedArrayListValue != 0) {
                selectedArrayListValue = 0;
                displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
            }
            displayDroneTypeInformation(data, selectedDroneId);
        }
    }

    /**
     * Displays information about the selected drone's type in the UI.
     * Updates the droneTypeLabel with relevant data based on the selected drone ID.
     * @param data             The list of DataStorage objects containing drone information.
     * @param selectedDroneId  The ID of the drone for which type information is to be displayed.
     */
    public void displayDroneTypeInformation(ArrayList<DataStorage> data, int selectedDroneId){
        for (int selectedDrone = 0; selectedDrone< data.size(); selectedDrone++) {
            if (data.get(selectedDrone).getDrone().getId() == selectedDroneId) {
                String[] droneTypeAttributes = {
                        "ID: ",
                        "Serialno: ",
                        "Drone Type: ",
                        "Carriage Weight: ",
                        "Carriage Type:"
                };
                String[] droneTypeValues = {
                        Integer.toString(selectedDroneId),
                        data.get(selectedDrone).getDrone().getSerialnumber(),
                        data.get(selectedDrone).getDroneType().getTypename(),
                        Double.toString(data.get(selectedDrone).getDrone().getCarriageWeight()),
                        String.valueOf(data.get(selectedDrone).getDrone().getCarriageType())
                };
                StringBuilder text = new StringBuilder();
                text.append("Drone information:\n");
                for (int stringIndex = 0; stringIndex < droneTypeAttributes.length; stringIndex++) {
                    text.append(String.format("%s %s\n", droneTypeAttributes[stringIndex], droneTypeValues[stringIndex]));
                }
                droneTypeLabel.setText(text.toString());
                droneTypePanel.add(droneTypeLabel);
                LOGGER.info("Drone information created for Drone ID: " + selectedDroneId);
                int selectedDroneTimeStamp = 0;
                displayDroneDynamicsInformation(data, selectedDroneId, selectedDroneTimeStamp);
                break;
            }
        }
    }

    /**
     * Displays information about the selected drone's dynamics in the UI.
     * Updates the droneDynamicsLabel with relevant data based on the selected drone ID and timestamp index.
     * @param data                     The list of DataStorage objects containing drone dynamics information.
     * @param selectedDroneId          The IDof the drone for which dynamics information is to be displayed.
     * @param selectedDroneTimeStamp   The timestamp index for the selected drone's dynamics data.
     */
    private void displayDroneDynamicsInformation(ArrayList<DataStorage> data, int selectedDroneId, int selectedDroneTimeStamp){
        for(int selectedDrone = 0; selectedDrone< data.size(); selectedDrone++){
            if (data.get(selectedDrone).getDrone().getId() == selectedDroneId){
                String[] droneDynamicsAttributes = {
                        "Dronedynamic No. " + selectedDroneTimeStamp + " of",
                        "Drone Status:",
                        "Battery Status:",
                        "Time Stamp:",
                        "Speed:",
                        "Last seen:",
                        "Longitude:",
                        "Latitude:",
                        "Alignment Pitch:",
                        "Alignment Roll:",
                        "Alignment Yaw:",
                        "Battery Info:"
                };
                String[] droneDynamicsValues = {
                        String.valueOf((data.get(selectedDrone).getDroneDynamicsList().toArray().length)-1),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getStatus()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getBatteryStatus()),
                        DroneMenu.formatCreatedDateTime(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getTimestamp()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getSpeed()),
                        DroneMenu.formatCreatedDateTime(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getLastSeen()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getLongitude()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getLatitude()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentPitch()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentRoll()),
                        String.valueOf(data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentYaw()),
                        data.get(selectedDrone).getDroneDynamicsList().get(selectedDroneTimeStamp).printBatteryInformation(data, selectedDrone,  selectedDroneTimeStamp)
                };
                StringBuilder text = new StringBuilder();
                text.append("DroneDynamics information:\n");
                for (int stringIndex = 0; stringIndex < droneDynamicsAttributes.length; stringIndex++) {
                    text.append(String.format("%s %s\n", droneDynamicsAttributes[stringIndex], droneDynamicsValues[stringIndex]));
                }
                droneDynamicsLabel.setText(text.toString());
                droneDynamicsPanel.add(droneDynamicsLabel);
                LOGGER.log(Level.INFO,"Correct TimeStamp successfully created...");
                break;
            }
        }
    }
}