package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import dronesim.Main;



public class GUI_EK {

    private static LinkedList<data.Drone> dronesData = new LinkedList<>(); // Angenommen, diese Liste wird gefüllt.


    public static void main(String[] args) {

        JFrame frame = new JFrame("Drone Daten");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        DefaultTableModel model = getData();

        JTable table = new JTable(model);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Filter-Panel hinzufügen
        JTextField filterText = new JTextField();
        JButton filterButton = new JButton("Filter");
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterText);
        filterPanel.add(filterButton);

        filterButton.addActionListener(e -> filterData(filterText.getText(), table));

        panel.add(filterPanel, BorderLayout.NORTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static DefaultTableModel getData() {
        String[] columnNames = {"carriage_type", "serialnumber", "created", "carriage_weight", "id", "dronetype"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (data.Drone drone : dronesData) {
            model.addRow(new Object[]{
                    drone.getCarriageType(),
                    drone.getSerialnumber(),
                    drone.getCreated(),
                    drone.getCarriageWeight(),
                    drone.getId(),
                    drone.getDroneTypeObject().getWeight()
            });
        }

        return model;
    }

    public static void filterData(String filter, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<data.Drone> filteredList = dronesData.stream()
                .filter(drone -> drone.getCarriageType().contains(filter) ||
                        drone.getSerialnumber().contains(filter) ||
                        drone.getCreated().contains(filter) ||
                        String.valueOf(drone.getCarriageWeight()).contains(filter) ||
                        String.valueOf(drone.getId()).contains(filter) ||
                        String.valueOf(drone.getDroneTypeObject().getWeight()).contains(filter))
                .collect(Collectors.toList());

        // Tabelle aktualisieren
        model.setRowCount(0);
        for (data.Drone drone : filteredList) {
            model.addRow(new Object[]{
                    drone.getCarriageType(),
                    drone.getSerialnumber(),
                    drone.getCreated(),
                    drone.getCarriageWeight(),
                    drone.getId(),
                    drone.getDroneTypeObject()
            });
        }
    }
}
