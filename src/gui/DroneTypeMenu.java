/**
 * This class creates a GUI for displaying and interacting with a list of DroneTypes.
 * It includes features such as sorting, searching and detailed view of drone types.
 */
package gui;

import data.DroneType;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import javax.swing.table.TableColumn;
import java.util.logging.Logger;

public class DroneTypeMenu extends JPanel {
    private JFrame frame;
    private LinkedList<DroneType> droneTypes;
    private DroneType selectedDrone;
    private static final Logger LOGGER = Logger.getLogger(DroneTypeMenu.class.getName());
    /**
     * Constructs a new DroneTypeMenu with the specified list of drone types.
     *
     * @param droneTypes A LinkedList of DroneType objects to be displayed.
     */
    public DroneTypeMenu(LinkedList<DroneType> droneTypes) {
        super(new BorderLayout());

        this.droneTypes = droneTypes;

        LOGGER.info("Initializing DroneTypeMenu...");

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

        //fill the columns with life
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

        frame = new JFrame("Drone Type");

        final JTable table = new JTable(data, columnNames) {
            /**
             * Determines wheter a specific cell in the table is editable or not. This implementation makes
             * all cells in the table non-editable to prevent user modification.
             *
             * @param row      The row index of the cell being queried.
             * @param column   The column index of the cell being queried.
             * @return false, indicating that no cell can be edited.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //edits specific column width
        TableColumn columnNr = table.getColumnModel().getColumn(0);
        columnNr.setPreferredWidth(columnNr.getPreferredWidth() - 40);

        TableColumn columnID = table.getColumnModel().getColumn(1);
        columnID.setPreferredWidth(columnID.getPreferredWidth() + 5);

        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);


        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Menu");
        JMenuItem exitItem = new JMenuItem("Back");

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        //this closes only the windows, not the whole programm!
        exitItem.addActionListener(e -> {
            if (frame != null) {
                frame.dispose();
            }
        });
        frame.setJMenuBar(menuBar);
        frame.setContentPane(this);
        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        LOGGER.info("DroneTypeMenu initialized.");

    }
    /**
     * Creates and displays the main GUI frame for the DroneType table.
     *
     * @param droneTypes A LinkedList of DroneType objects to be displayed.
     */
    public static void createDroneTypeTableGUI(LinkedList<DroneType> droneTypes) {
        LOGGER.info("Creating DroneTypeTableGUI...");
        new DroneTypeMenu(droneTypes);
    }
}