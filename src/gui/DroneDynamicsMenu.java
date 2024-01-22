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
    private int selectedDroneGoogleMaps;


    public DroneDynamicsMenu(ArrayList<DataStorage> data){
        this.data = data;

        LOGGER.info("Initializing DroneDynamicsMenu...");

        JFrame  frame = createFrame();

        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        JPanel gridPanel = createGridPanel(data);
        frame.add(gridPanel);

        LOGGER.info("DroneDynamicsMenu initialized.");
    }

    private JFrame createFrame(){
        frame = new JFrame("Drone Dynamics");

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

    private JPanel createGridPanel(ArrayList<DataStorage> data){
        gridPanel = new JPanel(new GridLayout(4,1));

        createUserPanel(data);
        createDroneTypePanel();
        createDroneDynamicsPanel();
        createButtons();

        LOGGER.info("GridPanel created...");

        return gridPanel;
    }

    private void createUserPanel(ArrayList<DataStorage> data){
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        droneIdDropdown = new JComboBox();

        initializeDroneIdDropdown(data);

        JButton googleMapsButton = new JButton("Open Google Maps");
        googleMapsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GoogleMaps mapCreator = new GoogleMaps();
                        openGoogleMaps(mapCreator.createPicture(   data.get(selectedDroneGoogleMaps).getDroneDynamicsList().get(selectedArrayListValue).getLatitude(),
                                                    data.get(selectedDroneGoogleMaps).getDroneDynamicsList().get(selectedArrayListValue).getLongitude()));
                        //openGoogleMaps(selectedDroneGoogleMaps);
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
    }

    private void openGoogleMaps(String filename){

        JFrame googleFrame = new JFrame();
        String imagePath = filename;
        //ImageIcon icon = createImageIcon(imagePath, "Image not found!");

        JLabel label = new JLabel(filename);

        //JPEGImageReadParam googlePicture = new JPEGImageReadParam();
        googleFrame.add(label);

        googleFrame.setSize(400, 400);
        googleFrame.setLocationRelativeTo(null);
        googleFrame.setVisible(true);
    }

//    private ImageIcon createImageIcon(String path, String description){
//        //java.net.URL imgURL = getClass().getResource(path);
//        if(imgURL != null){
//            return new ImageIcon(imgURL, description);
//        } else {
//            LOGGER.log(Level.SEVERE, "Picture not found!");
//            return null;
//        }
//    }

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
        JPanel buttonPanel = new JPanel(new GridLayout(2, 12));
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
                            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                            displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
                            selectedDroneGoogleMaps = selectedArrayListValue;
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
                            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                            displayDroneDynamicsInformation(data, selectedDroneId, selectedArrayListValue);
                            selectedDroneGoogleMaps = selectedArrayListValue;
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
        selectedDroneGoogleMaps = 0;
        displayDroneDynamicsInformation(data, (int) droneIdDropdown.getSelectedItem(), selectedArrayListValue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {

            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
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

                break;
            }
        }
    }
}