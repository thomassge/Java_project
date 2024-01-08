package GUI;

import data.Drone;
import data.DroneDynamics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DroneDynamicsMenu extends JPanel implements ActionListener {

    JLabel selectedImage;
    JLabel fixedImage;
    JTextArea topRightText;
    JTextArea bottomLeftText;
    private JComboBox<Integer> droneIdDropdown;
    private LinkedList<Drone> drones;

    public DroneDynamicsMenu(ArrayList<DroneDynamics> droneDynamicsArrayList) {
        super(new BorderLayout());

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
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == droneIdDropdown) {

            // Wenn ein neues Drone-ID ausgewählt wurde
            int selectedDroneId = (Integer) droneIdDropdown.getSelectedItem();

            // Hier kannst du die Logik für die Aktualisierung der GUI basierend auf der ausgewählten Drone-ID implementieren
            System.out.println("Ausgewählte Drone-ID: " + selectedDroneId);
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

    protected void createTextTopRight(int selectedDroneId) {

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
    }

    protected void createTextBottomLeft(int selectedDroneId, DroneDynamics selectedDroneDynamic) {

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
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DroneDynamicsMenu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void createDroneDynamicsOverview(ArrayList<DroneDynamics> droneDynamicsArrayList) {

        JFrame frame = new JFrame("Drone Dynamics");

        DroneDynamicsMenu droneDM = new DroneDynamicsMenu(droneDynamicsArrayList);

        frame.getContentPane().add(droneDM);
        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        droneDM.createTextTopRight((Integer) droneDM.droneIdDropdown.getSelectedItem());
    }
}