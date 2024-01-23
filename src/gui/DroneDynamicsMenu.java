package gui;

import data.DataStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import services.GoogleMaps;
import util.jsonCreator;


public class DroneDynamicsMenu implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneDynamicsMenu.class.getName());
    private ArrayList<DataStorage> data;
    private JFrame frame;
    private JPanel gridPanel;
    private JComboBox<Integer> droneIdDropdown;
    private JTextArea droneTypeLabel;
    private JPanel dTPanel;
    private JTextArea droneDynamicsLabel;
    private JPanel dDPanel;
    private int selectedArrayListValue = 0;
    //private int selectedDroneGoogleMaps;
    private int selectedDroneId;
    JPanel buttonPanel;
    JPanel userPanel;



    public DroneDynamicsMenu(ArrayList<DataStorage> data){
        new jsonCreator();
        this.data = data;

        LOGGER.info("Initializing DroneDynamicsMenu...");

        JFrame  frame = createFrame();

        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        JPanel gridPanel = createGridPanel(data);
        frame.add(gridPanel);

        // Select the first drone from the dropdown list
        if (droneIdDropdown.getItemCount() > 0) {
            droneIdDropdown.setSelectedIndex(0);
        }

        LOGGER.info("DroneDynamicsMenu initialized.");
    }

    private JFrame createFrame(){
        frame = new JFrame("Drone Dynamics");

        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        LOGGER.log(Level.INFO,"Frame created...");
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

    private JPanel createGridPanel(ArrayList<DataStorage> data){
        gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        createUserPanel(data);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.2;
        constraints.fill = GridBagConstraints.BOTH;
        gridPanel.add(userPanel, constraints);

        createDroneTypePanel();
        constraints.gridy = 1;
        constraints.weighty = 0.4;
        gridPanel.add(dTPanel, constraints);

        createDroneDynamicsPanel();
        constraints.gridy = 2;
        constraints.weighty = 0.4;
        gridPanel.add(dDPanel, constraints);

        createButtons();
        constraints.gridy = 3;
        constraints.weighty = 0;
        gridPanel.add(buttonPanel, constraints);

        LOGGER.info("GridPanel created...");

        return gridPanel;
    }

    private void createUserPanel(ArrayList<DataStorage> data){
        userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        droneIdDropdown = new JComboBox();

        initializeDroneIdDropdown(data);

        JButton googleMapsButton = new JButton("Open Google Maps");
        googleMapsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openGoogleMaps();
                    }
                });

        userPanel.add(droneIdDropdown);
        userPanel.add(googleMapsButton);
        gridPanel.add(userPanel);

        LOGGER.info("User-Panel created...");
    }

    private void initializeDroneIdDropdown(ArrayList<DataStorage> data) {
        for (int i = 0; i < data.size(); i++) {
            droneIdDropdown.addItem(data.get(i).getDrone().getId());
        }
        droneIdDropdown.addActionListener(this);
        LOGGER.log(Level.INFO,"Data intialized...");
    }

    private void openGoogleMaps(){

        int listIndex = selectedDroneId - 71; // selectedDroneId - selectedDroneId.get(0) für dynamik
        if((listIndex >= 0) && (listIndex <= data.size()) ) {
            GoogleMaps mapCreator = new GoogleMaps();
            String filename = mapCreator.createPicture( data.get(listIndex).getDroneDynamicsList().get(selectedArrayListValue).getLatitude(),
                    data.get(listIndex).getDroneDynamicsList().get(selectedArrayListValue).getLongitude());

            JFrame googleFrame = new JFrame();
            String imagePath = filename;
            googleFrame.add(new JLabel(new ImageIcon((new ImageIcon(filename)).getImage().getScaledInstance(630, 600, java.awt.Image.SCALE_SMOOTH))));

            googleFrame.setSize(400, 400);
            googleFrame.setLocationRelativeTo(null);
            googleFrame.setVisible(true);
            LOGGER.log(Level.INFO,"GoogleMaps opened...");
        }
    }

    private void createDroneTypePanel(){
        dTPanel = new JPanel(new BorderLayout());
        droneTypeLabel = new JTextArea("Select Drone-ID for DroneType information!");
        droneTypeLabel.setEditable(false);
        dTPanel.add(droneTypeLabel, BorderLayout.CENTER);
        gridPanel.add(dTPanel);

        LOGGER.info("DroneType Panel created...");
    }

    private void createDroneDynamicsPanel(){
        dDPanel = new JPanel(new BorderLayout());
        droneDynamicsLabel = new JTextArea("Select Drone-ID for DroneDynamics information!");
        droneTypeLabel.setEditable(false);
        dDPanel.add(droneDynamicsLabel, BorderLayout.CENTER);
        gridPanel.add(dDPanel);

        LOGGER.info("DroneDynamics Panel created...");
    }

    private void createButtons() {
        buttonPanel = new JPanel(new GridLayout(2, 12));
        JButton[] plusButtons = new JButton[3];
        JButton[] minusButtons = new JButton[3];

        for(int j = 0; j<3; j++ ){
            minusButtons[j] = new JButton("-" + (int) Math.pow(10, j));
            buttonPanel.add(minusButtons[j]);
        }

        for(int i = 0; i<3; i++){
            plusButtons[i] = new JButton("+" + (int) Math.pow(10, i) );
            buttonPanel.add(plusButtons[i]);
        }

        for(int k = 0; k < 3; k++){

            int valuePlus = (int) Math.pow(10, k);
            int valueMinus = (int) Math.pow(10, k);

                plusButtons[k].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedArrayListValue += valuePlus;

                        if (selectedArrayListValue >= data.getFirst().getDroneDynamicsList().toArray().length){
                            chegger();
                        } else {
                            selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                            displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
                            LOGGER.log(Level.INFO,"selectedArrayListValue successfully incremented...");
                        }
                    }
                });

                minusButtons[k].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedArrayListValue -= valueMinus;

                        if (selectedArrayListValue < 0){
                            chegger();
                        } else {
                            selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                            displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
                            LOGGER.log(Level.INFO,"selectedArrayListValue successfully decremented...");
                        }
                    }
                });
        }

        gridPanel.add(buttonPanel);

        LOGGER.info("Buttons created...");
    }

    private void chegger(){
        showMessageDialog(null, "TimeStamp out of Bound!\nFirst TimeStamp shown now!");
        selectedArrayListValue = 0;
        displayDroneDynamicsInformation(data, (int) droneIdDropdown.getSelectedItem(), selectedArrayListValue);
        LOGGER.log(Level.SEVERE,"selectedArrayListValue out of Bound!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {

            selectedDroneId = (int) droneIdDropdown.getSelectedItem();
            LOGGER.info("Selected Drone-ID: " + selectedDroneId);

            displayDroneTypeInformation(data, selectedDroneId);
        }
    }

    public void displayDroneTypeInformation(ArrayList<DataStorage> data, int selectedDroneId){
        for (int i = 0; i< data.size(); i++) {
            if (data.get(i).getDrone().getId() == selectedDroneId) {

                String[] droneTypeAttributes = {"ID: ",
                        "Serialno: ",
                        "Drone Type: ",
                        "Carriage Weight: ",
                        "Carriage Type:"};

                String[] droneTypeValues = {
                        Integer.toString(selectedDroneId),
                        data.get(i).getDrone().getSerialnumber(),
                        data.get(i).getDroneType().getTypename(),
                        Double.toString(data.get(i).getDrone().getCarriageWeight()),
                        String.valueOf(data.get(i).getDrone().getCarriageType())};

                StringBuilder text = new StringBuilder();
                text.append("Drone information:\n");

                for (int j = 0; j < droneTypeAttributes.length; j++) {
                    text.append(String.format("%s %s\n", droneTypeAttributes[j], droneTypeValues[j]));
                }

                droneTypeLabel.setText(text.toString());
                dTPanel.add(droneTypeLabel);

                LOGGER.info("Drone information created for Drone ID: " + selectedDroneId);

                int selectedDroneTimeStamp = 0;

                displayDroneDynamicsInformation(data, selectedDroneId, selectedDroneTimeStamp);

                break;
            }
        }
    }

    private void displayDroneDynamicsInformation(ArrayList<DataStorage> data, int selectedDroneId, int selectedDroneTimeStamp){
        for(int i = 0; i< data.size(); i++){
            if (data.get(i).getDrone().getId() == selectedDroneId){

                String[] droneDynamicsAttributes = {
                        "Dronedynamic No. " + (selectedDroneTimeStamp+1) + " of",
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
                        String.valueOf(data.get(i).getDroneDynamicsList().toArray().length+1),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getStatus()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getBatteryStatus()),
                        DroneMenu.formatCreatedDateTime(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getTimestamp()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getSpeed()),
                        DroneMenu.formatCreatedDateTime(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getLastSeen()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getLongitude()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getLatitude()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentPitch()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentRoll()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentYaw()),
                        data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).printBatteryInformation(data, i,  selectedDroneTimeStamp)
                };
                StringBuilder text = new StringBuilder();
                text.append("DroneDynamics information:\n");

                for (int j = 0; j < droneDynamicsAttributes.length; j++) {
                    text.append(String.format("%s %s\n", droneDynamicsAttributes[j], droneDynamicsValues[j]));
                }

                droneDynamicsLabel.setText(text.toString());
                dDPanel.add(droneDynamicsLabel);
                LOGGER.log(Level.INFO,"Correct TimeStamp successfully created...");
                break;
            }
        }
    }
}