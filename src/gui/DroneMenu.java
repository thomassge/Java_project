/**
 * This class is responsible for creating and managing the GUI for displaying
 * a list of drones and their details. It includes features for searching, viewing drone details,
 * and navigating to different parts of the application.
 */
package gui;

import data.*;
import processing.DataFactory;
import util.JsonCreator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import static javax.swing.JOptionPane.showMessageDialog;

public class DroneMenu implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());
    private DataFactory factory;
    private ArrayList<DataStorage> data;
    private LinkedList<DroneType> droneTypes;
    private Object[][] droneMenuData;
    private static JFrame droneMenuFrame;
    private JMenuBar droneMenuMenuBar;
    private JTable droneMenuTable;
    private final String[] columnNames = {"Nr.", "ID", "DroneType", "Created", "Serialnr", "CarrWeight", "CarrType"};
    private final int[] columnWidth = {-55, -55, 0, 0, 0, -55, -55};

    /**
     * Constructs a new DroneMenu with the specified list of drones.
     *
     * @param data The ArrayList that holds all information that need to be displayed.
     */
    public DroneMenu() {
        factory = new DataFactory();
        this.factory = factory;

        data = factory.getDataStorage();
        droneTypes = factory.getDroneTypes();

        new JsonCreator();
        LOGGER.info("Initializing DroneMenu...");

        initializeGuiData(data);

        createFrame();
        createMenuBar();
        createTable();

        JScrollPane droneMenuScrollPane = new JScrollPane(droneMenuTable);
        droneMenuFrame.setJMenuBar(droneMenuMenuBar);
        droneMenuFrame.add(droneMenuScrollPane);

        LOGGER.info("DroneMenu initialized.");
    }

    private void initializeGuiData(ArrayList<DataStorage> data){
        droneMenuData = new Object[data.size()][columnNames.length];

        for (int i = 0; i < data.size(); i++) {
            droneMenuData[i][0] = i + 1;
            droneMenuData[i][1] = data.get(i).getDrone().getId();
            droneMenuData[i][2] = data.get(i).getDroneType().getTypename();
            droneMenuData[i][3] = formatCreatedDateTime(data.get(i).getDrone().getCreated());
            droneMenuData[i][4] = data.get(i).getDrone().getSerialnumber();
            droneMenuData[i][5] = data.get(i).getDrone().getCarriageWeight();
            droneMenuData[i][6] = data.get(i).getDrone().getCarriageType();
        }
    }

    private void createFrame(){
        droneMenuFrame = new JFrame("Drones Overview");
        droneMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        droneMenuFrame.setSize(800, 550);
        droneMenuFrame.setLocationRelativeTo(null);
        droneMenuFrame.setVisible(true);
    }

    /**
     * Creates and returns the menu bar with different menu items.
     *
     * @return JMenuBar for the DroneMenu
     */
    private void createMenuBar() {
        LOGGER.info("Creating Menu Bar...");

        droneMenuMenuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        droneMenuMenuBar.add(menu);

        addItemToMenuWithActionCommand(menu, "Drone Types", "dronet");
        addItemToMenuWithActionCommand(menu, "Drone Dynamics", "droned");
        addItemToMenuWithActionCommand(menu, "Refresh", "refresh");
        addItemToMenuWithActionCommand(menu, "Credits", "credits");

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
        TableColumnModel columnModel = droneMenuTable.getColumnModel();

        for(int i=0; i<columnWidth.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth((column.getPreferredWidth() + columnWidth[i]));
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

        if ("dronet".equals(e.getActionCommand())) {
            new DroneTypeMenu(droneTypes);
        } else if ("droned".equals(e.getActionCommand())) {
            new DroneDynamicsMenu(data);
        } else if ("refresh".equals(e.getActionCommand())) {
            LOGGER.log(Level.INFO,"Refresh Button activated");
            //if(factory.checkForRefresh()) {}
                droneMenuFrame.dispose();
                new DroneMenu();
        } else if ("credits".equals(e.getActionCommand())){
            new CreditsMenu();
        }
    }

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

    public static void restarter(){
            showMessageDialog(null, "New Data available.\nGUI will restart now...");
            droneMenuFrame.dispose();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DroneMenu();
                }
            });
    }
}