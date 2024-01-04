package GUI;

import data.DroneType;
//import jdk.javadoc.internal.doclets.toolkit.taglets.snippet.Style;
import processing.JSONDeruloHelper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static processing.JSONDeruloHelper.helper;

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

        // Die Daten für die Tabelle
        Object[][] data = new Object[droneTypes.size()][columnNames.length];
                // Weitere Datenzeilen hier einfügen...
        for (int i = 0; i < droneTypes.size(); i++) {
           // data[i][0] = i + 1; // "Nr." column

            // Fetch data for each droneType and populate the respective columns
            data[i][0] = droneTypes.get(i).getDroneTypeID();
            data[i][1] = droneTypes.get(i).getManufacturer();
            data[i][2] = droneTypes.get(i).getTypename();
            data[i][3] = droneTypes.get(i).getWeight();
            data[i][4] = droneTypes.get(i).getMaximumSpeed();
            data[i][5] = droneTypes.get(i).getBatteryCapacity();
            data[i][6] = droneTypes.get(i).getControlRange();
            data[i][7] = droneTypes.get(i).getMaximumCarriage();
        };

        // Erstellen der JTable mit den Daten und Spaltennamen
        JTable table = new JTable(data, columnNames);

        // Hinzufügen der Tabelle zur JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Das JScrollPane zum Panel hinzufügen
        add(scrollPane);
    }

    public static void createDroneTypeTableGUI() {
        JFrame frame = new JFrame("Drone Types");

        DroneTypeMenu droneTM = new DroneTypeMenu(helper.getDroneTypes());
        frame.setContentPane(droneTM);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JSONDeruloHelper helper = new JSONDeruloHelper();
        LinkedList<DroneType> droneTypes = helper.getDroneTypes();

        SwingUtilities.invokeLater(() -> createDroneTypeTableGUI());
    }
}
