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
import javax.swing.table.TableColumnModel;
import java.util.logging.Logger;

public class DroneTypeMenu extends JPanel {

    private JFrame frame;
    //private DataFactory factory = new DataFactory();
    private static final Logger LOGGER = Logger.getLogger(DroneTypeMenu.class.getName());
    private final String[] columnNames = {"ID", "Manufacturer", "Typename", "Weight (g)", "Maximum Speed", "Battery Capacity", "Control Range", "Maximum Carriage"};
    private final int[] columnWidth = {-70, 0, 25, -40,  -20, -20, -20, 0};
    private Object[][] droneTypeMenuData;

    /**
     * Constructs a new DroneTypeMenu with the specified list of drone types.
     *
     * @param droneTypes A LinkedList of DroneType objects to be displayed.
     */
    public DroneTypeMenu(ArrayList<DataStorage> data) {
        super(new BorderLayout());
        LOGGER.info("Initializing DroneTypeMenu...");

        initializeGuiData(data);

        JFrame frame = createFrame();
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        JTable table = createTable();

        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        LOGGER.info("DroneTypeMenu initialized.");
    }

    private JTable createTable(){
        JTable table = new JTable(droneTypeMenuData, columnNames) {
            /**
             * Determines whether a cell in the table is editable. This implementation makes all
             * cells in the tabe non-editable.
             *
             * @param row     The row index of the cell.
             * @param column   The coloumn index of the cell.
             * @return false as none of the cells are editable.
             */
            @Override
            public boolean isCellEditable ( int row, int column){
                return false;
            }
        };
        TableColumnModel columnModel = table.getColumnModel();

        for(int i=0; i<columnWidth.length; i++){
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth((column.getPreferredWidth() + columnWidth[i]));
        }
        return table;
    }

    private JFrame createFrame(){
        frame = new JFrame("Drone Types");

        frame.setContentPane(this);
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

    private void initializeGuiData(ArrayList<DataStorage> data){
        droneTypeMenuData = new Object[data.size()][columnNames.length];

        for (int i = 0; i < data.size(); i++) {
            droneTypeMenuData[i][0] = data.get(i).getDroneType().getDroneTypeID();
            droneTypeMenuData[i][1] = data.get(i).getDroneType().getManufacturer();
            droneTypeMenuData[i][2] = data.get(i).getDroneType().getTypename();
            droneTypeMenuData[i][3] = data.get(i).getDroneType().getWeight();
            droneTypeMenuData[i][4] = data.get(i).getDroneType().getMaximumSpeed();
            droneTypeMenuData[i][5] = data.get(i).getDroneType().getBatteryCapacity();
            droneTypeMenuData[i][6] = data.get(i).getDroneType().getControlRange();
            droneTypeMenuData[i][7] = data.get(i).getDroneType().getMaximumCarriage();
        }
    }
}