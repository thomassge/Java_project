package GUI;

import data.DroneType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DroneTypeMenu extends JPanel {

    public DroneTypeMenu(LinkedList<DroneType> droneTypes) {
        super(new BorderLayout());

        String[] columnNames = {
                "ID",
                "Manufacturer",
                "Typename",
                "Weight",
                "Maximum Speed",
                "Battery Capacity",
                "Control Range",
                "Maximum Carriage"
        };

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

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Search func
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        });

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        //hf gl
        JMenu menu2 = new JMenu("Refresh");
        menuBar.add(menu2);

        
        JMenu searchMenu = new JMenu("Search");
        searchMenu.add(searchField);
        searchMenu.add(searchButton);
        menuBar.add(searchMenu);

        JFrame frame = new JFrame("Drone Types");
        frame.setJMenuBar(menuBar);

        frame.setContentPane(this);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void createDroneTypeTableGUI(LinkedList<DroneType> droneTypes) {
        new DroneTypeMenu(droneTypes);
    }
}
