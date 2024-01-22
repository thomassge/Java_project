package gui;

import data.DataStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DroneDynamicsMenu implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneDynamicsMenu.class.getName());
    private ArrayList<DataStorage> data;
    private JFrame frame;
    private JPanel gridPanel = new JPanel(new GridLayout(4,1));
    private JComboBox<Integer> droneIdDropdown;
    private JTextArea droneTypeLabel;
    private JPanel dTPanel;
    private JTextArea droneDynamicsLabel;
    private JPanel dDPanel;
    private int selectedDroneTimeStampValue = 0;

    
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
        createButtonsOnly();

        LOGGER.info("GridPanel created...");

        return gridPanel;
    }

    private void createUserPanel(ArrayList<DataStorage> data){
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        droneIdDropdown = new JComboBox();
        initializeDroneIdDropdown(data);
        JButton googleMapsButton = new JButton("Google Maps öffnen");

        userPanel.add(droneIdDropdown);
        userPanel.add(googleMapsButton);
        gridPanel.add(userPanel);

        LOGGER.info("User-Panel created...");
    }

    private void createDroneTypePanel(){
        dTPanel = new JPanel(new BorderLayout());
        droneTypeLabel = new JTextArea("DroneType: This DroneType Info");
        droneTypeLabel.setEditable(false);
        dTPanel.add(droneTypeLabel, BorderLayout.CENTER);
        gridPanel.add(dTPanel);

        LOGGER.info("DroneType Panel created...");
    }

    private void createDroneDynamicsPanel(){
        dDPanel = new JPanel(new BorderLayout());
        droneDynamicsLabel = new JTextArea("DroneDynamics: This DroneDynamics Info");
        droneTypeLabel.setEditable(false);
        dDPanel.add(droneDynamicsLabel, BorderLayout.CENTER);
        gridPanel.add(dDPanel);
        
        LOGGER.info("DroneDynamics Panel created...");
    }

private void createButtonsOnly() {
    JPanel buttonPanel = new JPanel(new GridLayout(1, 6));

        JButton plusButton1 = new JButton("+1");
        JButton plusButton10 = new JButton("+10");
        JButton plusButton100 = new JButton("+100");

        JButton minusButton1 = new JButton("-1");
        JButton minusButton10 = new JButton("-10");
        JButton minusButton100 = new JButton("-100");

        int incrementValuePlus1 = 1;
        int incrementValuePlus10 = 10;
        int incrementValuePlus100 = 100;

        int decrementValueMinus1 = -1;
        int decrementValueMinus10 = -10;
        int decrementValueMinus100 = -100;

        buttonPanel.add(plusButton1);
        buttonPanel.add(plusButton10);
        buttonPanel.add(plusButton100);

        buttonPanel.add(minusButton1);
        buttonPanel.add(minusButton10);
        buttonPanel.add(minusButton100);

        plusButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDroneTimeStampValue += incrementValuePlus1;
                int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                displayDroneDynamicsInformation(data,selectedDroneId, selectedDroneTimeStampValue);
            }
        });

        plusButton10.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 selectedDroneTimeStampValue += incrementValuePlus10;
                int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                displayDroneDynamicsInformation(data,selectedDroneId, selectedDroneTimeStampValue);
            }
        });

        plusButton100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDroneTimeStampValue += incrementValuePlus100;
                int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                displayDroneDynamicsInformation(data,selectedDroneId, selectedDroneTimeStampValue);
            }
        });

        minusButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDroneTimeStampValue -= incrementValuePlus1;
                int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
                displayDroneDynamicsInformation(data, selectedDroneId, selectedDroneTimeStampValue);
            }
        });

    minusButton10.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedDroneTimeStampValue -= incrementValuePlus10;
            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
            displayDroneDynamicsInformation(data, selectedDroneId, selectedDroneTimeStampValue);
        }
    });

    minusButton100.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedDroneTimeStampValue -= incrementValuePlus100;
            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
            displayDroneDynamicsInformation(data, selectedDroneId, selectedDroneTimeStampValue);
        }
    });

    gridPanel.add(buttonPanel);

    LOGGER.info("Buttons created...");
}

    private void initializeDroneIdDropdown(ArrayList<DataStorage> data) {
        for (int i = 0; i < data.size(); i++) {
            droneIdDropdown.addItem(data.get(i).getDrone().getId());
        }
        droneIdDropdown.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {

            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();
            LOGGER.info("Selected Drone-ID: " + selectedDroneId);

            displayDroneTypeInformation(data, selectedDroneId);
        }
    }
    public void displayDroneTypeInformation(ArrayList<DataStorage> data,int selectedDroneId){

        for (int i = 0; i< data.size(); i++) {
            if (data.get(i).getDrone().getId() == selectedDroneId) {

                String[] droneTypeAttributes = {"ID",
                        "Serialnr",
                        "Drone Type",
                        "Carriage Weight",
                        "Carriage Type"};

                String[] droneTypeValues = {
                        Integer.toString(selectedDroneId),
                        data.get(i).getDrone().getSerialnumber(),
                        data.get(i).getDroneType().getTypename(),
                        Double.toString(data.get(i).getDrone().getCarriageWeight()),
                        String.valueOf(data.get(i).getDrone().getCarriageType())};

                StringBuilder text = new StringBuilder();
                text.append("Details:\n");

                for (int j = 0; j < droneTypeAttributes.length; j++) {
                    text.append(String.format("%s: %s\n", droneTypeAttributes[j], droneTypeValues[j]));
                }

                droneTypeLabel.setText(text.toString());
                dTPanel.add(droneTypeLabel);

                LOGGER.info("Top right text created for Drone ID: " + selectedDroneId);

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
                        "Drone Status",
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
                String[] droneDynamicsValues = {
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getStatus()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getBatteryStatus()),
                        data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getTimestamp(),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getSpeed()),
                        data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getLastSeen(),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getLongitude()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getLatitude()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentPitch()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentRoll()),
                        String.valueOf(data.get(i).getDroneDynamicsList().get(selectedDroneTimeStamp).getAlignmentYaw())
                };
                StringBuilder text = new StringBuilder();
                text.append("Details:\n");

                for (int j = 0; j < droneDynamicsAttributes.length; j++) {
                    text.append(String.format("%s: %s\n", droneDynamicsAttributes[j], droneDynamicsValues[j]));
                }
                droneDynamicsLabel.setText(text.toString());
                dDPanel.add(droneDynamicsLabel);
            }
        }
    }
}