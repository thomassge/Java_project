/**
 * This class creates a GUI for displaying and interacting with a list of DroneTypes.
 * It includes features such as sorting, searching and detailed view of drone types.
 */
package gui;

import data.DataFactory;
import data.DataStorage;
import data.DroneType;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.TableColumn;
import java.util.logging.Logger;

public class DroneTypeMenu extends JPanel {
    private JFrame frame;
    private DroneType selectedDrone;
    private DataFactory factory = new DataFactory();
    //private Timer refreshTimer;
    private static final Logger LOGGER = Logger.getLogger(DroneTypeMenu.class.getName());

    /**
     * Constructs a new DroneTypeMenu with the specified list of drone types.
     *
     * @param droneTypes A LinkedList of DroneType objects to be displayed.
     */
    public DroneTypeMenu(ArrayList<DataStorage> data) {
        super(new BorderLayout());
        LOGGER.info("Initializing DroneTypeMenu...");

        //creating the columns
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

        //array for columns
        Object[][] data1 = new Object[factory.getDroneTypes().size()][columnNames.length];

        //fill the columns with life
        for (int i = 0; i < factory.getDroneTypes().size(); i++) {
            data1[i][0] = factory.getDroneTypes().get(i).getDroneTypeID();
            data1[i][1] = factory.getDroneTypes().get(i).getManufacturer();
            data1[i][2] = factory.getDroneTypes().get(i).getTypename();
            data1[i][3] = factory.getDroneTypes().get(i).getWeight();
            data1[i][4] = factory.getDroneTypes().get(i).getMaximumSpeed();
            data1[i][5] = factory.getDroneTypes().get(i).getBatteryCapacity();
            data1[i][6] = factory.getDroneTypes().get(i).getControlRange();
            data1[i][7] = factory.getDroneTypes().get(i).getMaximumCarriage();
        }

        frame = new JFrame("Drone Type");

        final JTable table = new JTable(data1, columnNames) {
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
    public static void createDroneTypeTableGUI(ArrayList<DataStorage> data) {
        LOGGER.info("Creating DroneTypeTableGUI...");
        new DroneTypeMenu(data);
    }
}