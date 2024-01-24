/**
 * This class creates a GUI for displaying and interacting with a list of DroneTypes.
 * It includes features such as sorting, searching and detailed view of drone types.
 */
package gui;

import data.DataStorage;
import util.jsonCreator;
import java.util.logging.Logger;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class DroneTypeMenu {

    private static final Logger LOGGER = Logger.getLogger(DroneTypeMenu.class.getName());
    private Object[][] droneTypeMenuData;
    private JFrame droneTypeFrame;
    private JMenuBar droneTypeMenuBar;
    private JTable droneTypeTable;
    private final String[] columnNames = {"ID", "Manufacturer", "Typename", "Weight (g)", "Maximum Speed", "Battery Capacity", "Control Range", "Maximum Carriage"};
    private final int[] columnWidth = {-70, 0, 25, -40,  -20, -20, -20, 0};

    /**
     * Constructs a new DroneTypeMenu with the specified list of drone types.
     *
     * @param droneTypes A LinkedList of DroneType objects to be displayed.
     */
    public DroneTypeMenu(ArrayList<DataStorage> data) {
        new jsonCreator();
        LOGGER.info("Initializing DroneTypeMenu...");

        initializeGuiData(data);

        createFrame();
        createMenuBar();
        createTable();

        JScrollPane droneTypeMenuScrollPane = new JScrollPane(droneTypeTable);
        droneTypeFrame.setJMenuBar(droneTypeMenuBar);
        droneTypeFrame.add(droneTypeMenuScrollPane);

        LOGGER.info("DroneTypeMenu initialized.");
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
        TableColumnModel columnModel = droneTypeTable.getColumnModel();

        for(int i=0; i<columnWidth.length; i++){
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth((column.getPreferredWidth() + columnWidth[i]));
        }
    }
}