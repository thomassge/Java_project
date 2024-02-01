package gui;

import data.*;
import processing.DataFactory;
import util.JsonCreator;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * This class provides a graphical user interface for displaying a list of drones along
 * with their details.
 * @author Thomas Levantis, Eyüp Korkmaz, Marco Difflipp
 */
public class DroneMenu implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());
    public static JFrame droneMenuFrame;
    private final String[] columnNames = {"Nr.", "ID", "DroneType", "Created", "Serialnr", "CarrWeight", "CarrType"};
    private final int[] columnWidth = {-55, -55, 0, 0, 0, -55, -55};
    private  ArrayList<DataStorage> data;
    private  LinkedList<DroneType> droneTypes;
    private Object[][] droneMenuData;
    private JMenuBar droneMenuMenuBar;
    private JTable droneMenuTable;

    /**
     * Constructs a new DroneMenu with the specified list of drones.
     * @param data A Arraylist of drone, drone type and drone dynamics objects to be displayed.
     *  @param droneTypes A LinkedList of DroneType objects to be displayed.
     */
    public DroneMenu(ArrayList <DataStorage> data, LinkedList <DroneType> droneTypes) {
        this.data=data;
        this.droneTypes=droneTypes;
        new JsonCreator();
        LOGGER.info("Initializing DroneMenu...");
        initializeDroneMenuData(data);
        createFrame();
        createMenuBar();
        createTable();
        JScrollPane droneMenuScrollPane = new JScrollPane(droneMenuTable);
        droneMenuFrame.setJMenuBar(droneMenuMenuBar);
        droneMenuFrame.add(droneMenuScrollPane);
        LOGGER.info("DroneMenu initialized.");
    }

    /**
     * Formats a datetime string to a more readable format.
     * @param created The original datetime string.
     * @return A formatted string.
     */
    public static String formatCreatedDateTime(String created){
        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(created);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return dateTime.format(formatter);
        } catch (Exception e){
            LOGGER.log(Level.INFO, "Wrong Format:",e);
            return created;
        }
    }

    /**
     * Handles actions performed by the user, such as selecting menu items.
     *
     * @param e The ActionEvent object representing the user's action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.info("Action Performed: " + e.getActionCommand());
        if ("DroneTypes".equals(e.getActionCommand())) {
            new DroneTypeMenu(droneTypes);
        } else if ("DroneDynamics".equals(e.getActionCommand())) {
            new DroneDynamicsMenu(data);
        } else if ("Refresh".equals(e.getActionCommand())) {
            LOGGER.log(Level.INFO,"Refresh Button activated");
            droneMenuFrame.dispose();
            new DroneMenu(data, droneTypes);
        } else if ("Credits".equals(e.getActionCommand())){
            new CreditsMenu();
        }
    }

    private void initializeDroneMenuData(ArrayList<DataStorage> data){
        droneMenuData = new Object[data.size()][columnNames.length];
        for (int selectedDrone = 0; selectedDrone < data.size(); selectedDrone++) {
            droneMenuData[selectedDrone][0] = selectedDrone + 1;
            droneMenuData[selectedDrone][1] = data.get(selectedDrone).getDrone().getId();
            droneMenuData[selectedDrone][2] = data.get(selectedDrone).getDroneType().getTypename();
            droneMenuData[selectedDrone][3] = formatCreatedDateTime(data.get(selectedDrone).getDrone().getCreated());
            droneMenuData[selectedDrone][4] = data.get(selectedDrone).getDrone().getSerialnumber();
            droneMenuData[selectedDrone][5] = data.get(selectedDrone).getDrone().getCarriageWeight();
            droneMenuData[selectedDrone][6] = data.get(selectedDrone).getDrone().getCarriageType();
        }
    }

    private void createFrame(){
        droneMenuFrame = new JFrame("Drones Overview");
        droneMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        droneMenuFrame.setSize(800, 550);
        droneMenuFrame.setLocationRelativeTo(null);
        droneMenuFrame.setVisible(true);
    }

    private void createMenuBar() {
        LOGGER.info("Creating Menu Bar...");
        droneMenuMenuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        droneMenuMenuBar.add(menu);
        addItemToMenuWithActionCommand(menu, "Drone Types", "DroneTypes");
        addItemToMenuWithActionCommand(menu, "Drone Dynamics", "DroneDynamics");
        addItemToMenuWithActionCommand(menu, "Refresh", "Refresh");
        addItemToMenuWithActionCommand(menu, "Credits", "Credits");
        LOGGER.info("Menu Bar created.");
    }

    private void addItemToMenuWithActionCommand(JMenu menu, String itemName, String actionCmd){
        JMenuItem droneMenuItem = new JMenuItem(itemName);
        droneMenuItem.setActionCommand(actionCmd);
        droneMenuItem.addActionListener(this);
        menu.add(droneMenuItem);
    }

    private void createTable(){
        droneMenuTable = new JTable(droneMenuData, columnNames) {
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
        TableColumnModel columnModel = droneMenuTable.getColumnModel();
        for(int selectedColumn = 0; selectedColumn < columnWidth.length; selectedColumn++) {
            TableColumn column = columnModel.getColumn(selectedColumn);
            column.setPreferredWidth((column.getPreferredWidth() + columnWidth[selectedColumn]));
        }
    }
}