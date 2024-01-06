package GUI;

import data.DroneType;
import processing.JSONDeruloHelper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class DroneTypeMenu extends JPanel {

    // Arbeitet direkt mit übergebener Linked List weiter
    public DroneTypeMenu(LinkedList<DroneType> droneTypes) {
        super(new GridLayout(1, 0));

        // Die Spaltennamen für die Tabelle
        String[] columnNames = {
                "ID",
                "Manufacturer",
                "Typename",
                "Weight",
                "Maximum Speed",
                "Battery Capacity",
                "Control Range",
                "Maximum Carriage"};

        Object[][] data = new Object[droneTypes.size()][columnNames.length];

        for (int i = 0; i < droneTypes.size(); i++) {
            data[i][0] = droneTypes.get(i).getDroneTypeID();
            data[i][1] = droneTypes.get(i).getManufacturer();
            data[i][2] = droneTypes.get(i).getTypename();
            data[i][3] = droneTypes.get(i).getWeight();
            data[i][4] = droneTypes.get(i).getMaximumSpeed();
            data[i][5] = droneTypes.get(i).getBatteryCapacity();
            data[i][6] = droneTypes.get(i).getControlRange();
            data[i][7] = droneTypes.get(i).getMaximumCarriage();
        }

        // Erstellen der JTable mit den Daten und Spaltennamen
        JTable table = new JTable(data, columnNames);

        // Hinzufügen der Tabelle zur JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Das JScrollPane zum Panel hinzufügen
        add(scrollPane);
    }

    public static void createDroneTypeTableGUI(LinkedList<DroneType> droneTypes) {
        JFrame frame = new JFrame("Drone Types");

        DroneTypeMenu droneTM = new DroneTypeMenu(droneTypes);
        frame.setContentPane(droneTM);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
