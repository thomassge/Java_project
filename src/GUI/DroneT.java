package GUI;

import javax.swing.*;
import java.awt.*;

public class DroneT extends JPanel {

    public DroneT() {
        super(new GridLayout(1, 0));

        // Die Spaltennamen für die Tabelle
        String[] columnNames = {"Column 1", "Column 2", "Column 3"};

        // Die Daten für die Tabelle
        Object[][] data = {
                {"Data 1", "Data 2", "Data 3"},
                {"Data 4", "Data 5", "Data 6"},
                // Weitere Datenzeilen hier einfügen...
        };

        // Erstellen der JTable mit den Daten und Spaltennamen
        JTable table = new JTable(data, columnNames);

        // Hinzufügen der Tabelle zur JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Das JScrollPane zum Panel hinzufügen
        add(scrollPane);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Drone Type");
        DroneT droneT = new DroneT();
        frame.setContentPane(droneT);
        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}
