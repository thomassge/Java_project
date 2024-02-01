package gui;

import data.DroneType;

import java.util.LinkedList;
import java.util.logging.Logger;
import util.JsonCreator;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * This class provides a graphical user interface for displaying a list of all drone types
 * @author Thomas Levantis, Eyüp Korkmaz, Marco Difflipp
 */
public class DroneTypeMenu {
    private static final Logger LOGGER = Logger.getLogger(DroneTypeMenu.class.getName());
    private Object[][] droneTypeMenuData;
    private JFrame droneTypeFrame;
    private JMenuBar droneTypeMenuBar;
    private JTable droneTypeTable;
    private final String[] columnNames = {"ID", "Manufacturer", "Typename", "Weight (g)", "Maximum Speed",
            "Battery Capacity", "Control Range", "Maximum Carriage"};
    private final int[] columnWidth = {-70, 0, 25, -40,  -20, -20, -20, 0};

    /**
     * Constructs a new DroneTypeMenu with the specified list of drone types.
     *
     * @param data A LinkedList of DroneType objects to be displayed.
     */
    public DroneTypeMenu(LinkedList<DroneType> data) {
        new JsonCreator();
        LOGGER.info("Initializing DroneTypeMenu...");
        initializeDroneTypeMenuData(data);
        createFrame();
        createMenuBar();
        createTable();
        JScrollPane droneTypeMenuScrollPane = new JScrollPane(droneTypeTable);
        droneTypeFrame.setJMenuBar(droneTypeMenuBar);
        droneTypeFrame.add(droneTypeMenuScrollPane);
        LOGGER.info("DroneTypeMenu initialized.");
    }

    private void initializeDroneTypeMenuData(LinkedList<DroneType> data){
        droneTypeMenuData = new Object[data.size()][columnNames.length];
        for (int selectedDrone = 0; selectedDrone < data.size(); selectedDrone++) {
            droneTypeMenuData[selectedDrone][0] = data.get(selectedDrone).getDroneTypeID();
            droneTypeMenuData[selectedDrone][1] = data.get(selectedDrone).getManufacturer();
            droneTypeMenuData[selectedDrone][2] = data.get(selectedDrone).getTypename();
            droneTypeMenuData[selectedDrone][3] = data.get(selectedDrone).getWeight();
            droneTypeMenuData[selectedDrone][4] = data.get(selectedDrone).getMaximumSpeed();
            droneTypeMenuData[selectedDrone][5] = data.get(selectedDrone).getBatteryCapacity();
            droneTypeMenuData[selectedDrone][6] = data.get(selectedDrone).getControlRange();
            droneTypeMenuData[selectedDrone][7] = data.get(selectedDrone).getMaximumCarriage();
        }
    }

    private void createFrame(){
        droneTypeFrame = new JFrame("Drone Types");
        droneTypeFrame.setSize(800, 550);
        droneTypeFrame.setLocationRelativeTo(null);
        droneTypeFrame.setVisible(true);
    }

    private void createMenuBar() {
        LOGGER.info("Creating Menu Bar...");
        droneTypeMenuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem droneTypeExit = new JMenuItem("Back");
        droneTypeMenuBar.add(menu);
        menu.add(droneTypeExit);
        droneTypeExit.addActionListener(e -> {
            if (droneTypeFrame != null) {
                droneTypeFrame.dispose();
            }
        });
        LOGGER.info("Menu Bar created.");
    }

    private void createTable(){
        droneTypeTable = new JTable(droneTypeMenuData, columnNames) {
            /**
             * Determines whether a cell in the table is editable. This implementation makes all
             * cells in the table non-editable.
             *
             * @param row     The row index of the cell.
             * @param column   The column index of the cell.
             * @return false as none of the cells are editable.
             */
            @Override
            public boolean isCellEditable ( int row, int column){
                return false;
            }
        };
        TableColumnModel columnModel = droneTypeTable.getColumnModel();
        for(int selectedColumn=0; selectedColumn<columnWidth.length; selectedColumn++){
            TableColumn column = columnModel.getColumn(selectedColumn);
            column.setPreferredWidth((column.getPreferredWidth() + columnWidth[selectedColumn]));
        }
    }
}
