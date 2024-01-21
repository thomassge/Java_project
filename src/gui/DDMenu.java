package gui;

import data.DataStorage;
import data.DroneDynamics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DDMenu {
    private static final Logger LOGGER = Logger.getLogger(DDMenu.class.getName());
    private ArrayList<DataStorage> data;
    private JFrame frame;
    private JPanel gridPanel = new JPanel(new GridLayout(4,1));
    private JComboBox droneIdDropdown;
    private JTextArea droneTypeLabel;
    private JPanel dtPanel;
    //private JLabel droneDynamicsLabel;
    
    public DDMenu (ArrayList<DataStorage> data){
        //super(new BorderLayout());
        //frame.setLayout(new BorderLayout());
        this.data = data;
        LOGGER.info("Initializing DroneDynamicsMenu...");
        JFrame  frame = createFrame();
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        JPanel gridPanel = createGridPanel(data);
        //initializeDroneIdDropdown(data);

        // Adding Panels to Main-Panel
        frame.add(gridPanel);

        LOGGER.info("DroneDynamicsMenu initialized.");

    }

    private JFrame createFrame(){
        frame = new JFrame("Drone Dynamics");

        //frame.setContentPane(this);
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
        createidPanel(data);
        createDroneTypePanel();
        createDroneDynmicsPanel();
        createSlider();
        LOGGER.info("GridPanel created...");
        return gridPanel;
    }

    private void createidPanel(ArrayList<DataStorage> data){
        //TopLeft id Panel
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //JComboBox<String> idDropdown = new JComboBox<>(new String[]{"ID1", "ID2", "ID3"});
        droneIdDropdown = new JComboBox();
        initializeDroneIdDropdown(data);

        JButton googleMapsButton = new JButton("Google Maps öffnen");

        idPanel.add(droneIdDropdown);
        idPanel.add(googleMapsButton);
        gridPanel.add(idPanel);

        LOGGER.info("id-Panel created...");
    }

    private void createDroneTypePanel(){
        //DroneType Panel
        JPanel dtPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextArea droneTypeLabel = new JTextArea();
        dtPanel.add(droneTypeLabel);
        gridPanel.add(dtPanel);
        LOGGER.info("DroneType Panel created...");
    }

    private void createDroneDynmicsPanel(){
        //DroneDynamics Panel
        JPanel ddPanel = new JPanel(new BorderLayout());
        JLabel droneDynamicsLabel = new JLabel("DroneDynamics: This DroneDynamics Info");
        ddPanel.add(new JScrollPane(droneDynamicsLabel), BorderLayout.CENTER);
        gridPanel.add(ddPanel);
        LOGGER.info("DroneDynamics Panel created...");
    }

    private void createSlider(){
        //JSlider + Buttons
        JPanel sliderPanel = new JPanel(new BorderLayout());
        JSlider droneSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6));
        JButton[] incrementButtons = new JButton[3];
        JButton[] decrementButtons = new JButton[3];

        for (int i = 0; i < 3; i++) {
            incrementButtons[i] = new JButton("+" + Math.pow(10, i));
            decrementButtons[i] = new JButton("-" + Math.pow(10, i));
            buttonPanel.add(decrementButtons[i]);
            buttonPanel.add(incrementButtons[i]);

            int incrementValue = (int) Math.pow(10, i);
            int decrementValue = -incrementValue;

            incrementButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    droneSlider.setValue(droneSlider.getValue() + incrementValue);
                }
            });

            decrementButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    droneSlider.setValue(droneSlider.getValue() + decrementValue);
                }
            });
        }
        sliderPanel.add(droneSlider, BorderLayout.CENTER);
        sliderPanel.add(buttonPanel, BorderLayout.SOUTH);
        gridPanel.add(sliderPanel);
        LOGGER.info("Slider with Buttons created...");
    }

    private void initializeDroneIdDropdown(ArrayList<DataStorage> data) {
        for (int i = 0; i < data.size(); i++) {
            droneIdDropdown.addItem(data.get(i).getDrone().getId());
        }



       /* for(DataStorage storage : data.size()){
            droneIdDropdown.addItem(data.get(storage).getDrone());
        }

        */
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {

            // Wenn ein neues Drone-ID ausgewählt wurde
            int selectedDroneId = (int) droneIdDropdown.getSelectedItem();

            LOGGER.info("Selected Drone ID: " + selectedDroneId);

            // Hier kannst du die Logik für die Aktualisierung der GUI basierend auf der ausgewählten Drone-ID implementieren
            LOGGER.info("Ausgewählte Drone-ID: " + selectedDroneId);
            displayDroneTypeInformation(data,selectedDroneId);

        }
    }
    public void displayDroneTypeInformation(ArrayList<DataStorage> data,int selectedDroneId){

        for (DataStorage dataStorage : data) { //data.getDrone();
            if (dataStorage.getDrone().getId() == selectedDroneId) {
                String[] droneValues = {
                        Integer.toString(selectedDroneId),
                        dataStorage.getDrone().getSerialnumber(),
                        dataStorage.getDrone().getDroneTypeObject().getTypename(),
                        Double.toString(dataStorage.getDrone().getCarriageWeight()),
                        String.valueOf(dataStorage.getDrone().getCarriageType())
                };
                String[] droneAttributes = {"ID",
                        "Serialnr",
                        "Drone Type",
                        "Carriage Weight",
                        "Carriage Type"};

                StringBuilder text = new StringBuilder();
                //text.append("Details:\n");
                for (int i = 0; i < droneAttributes.length; i++) {
                    text.append(String.format("%s: %s\n", droneAttributes[i], droneValues[i]));
                }
                droneTypeLabel.setText(text.toString());
                //dtPanel.add(droneTypeLabel);
                //gridPanel.add(dtPanel);


                // Bei selectedDroneId -70 -1 (weil Drohnen in ArrayList bei [0] anfangen
                // DroneDynamics selectedDroneDynamic = data.get(0).getDroneDynamicsList().getFirst();
                //String selectedTimestamp = null;
                //createTextBottomLeft(selectedDroneId, selectedDroneDynamic,selectedTimestamp);

                LOGGER.info("Top right text created for Drone ID: " + selectedDroneId);
                break;
            }
        }
        /*if(selectedDrone != null){
            // Initialize topRightText if not initialized
            if (topRightText == null) {
                topRightText = new JTextArea();
                topRightText.setEditable(false);
                topRightText.setPreferredSize(new Dimension(120, 100));
            }

         */
           /* String[] droneAttributes = {"ID",
                    "Serialnr",
                    "DroneType",
                    "Weight",
                    "CarriageType"};
*/
           /* String[] droneValues = {Integer.toString(selectedDroneId),
                    selectedDrone.getSerialnumber(),
                    selectedDrone.getDroneTypeObject().getTypename(),
                    Double.toString(selectedDrone.getCarriageWeight()),
                    String.valueOf(selectedDrone.getCarriageType())
            };
            */

            //StringBuilder text = new StringBuilder();
            //text.append("Details:\n");
           // for (int i = 0; i < droneAttributes.length; i++) {
             //   text.append(String.format("%s: %s\n", droneAttributes[i], droneValues[i]));
            //}
            //droneTypeLabel.setText(text.toString());

            // Bei selectedDroneId -70 -1 (weil Drohnen in ArrayList bei [0] anfangen
           // DroneDynamics selectedDroneDynamic = data.get(0).getDroneDynamicsList().getFirst();
            //String selectedTimestamp = null;
            //createTextBottomLeft(selectedDroneId, selectedDroneDynamic,selectedTimestamp);

            //LOGGER.info("Top right text created for Drone ID: " + selectedDroneId);
        }
    }

