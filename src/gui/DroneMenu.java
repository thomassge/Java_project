/**
 * This class is responsible for creating and managing the GUI for displaying
 * a list of drones and their details. It includes features for searching, viewing drone details,
 * and navigating to different parts of the application.
 */
package gui;

import data.*;
//import jdk.internal.org.jline.terminal.TerminalBuilder;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.ArrayList;


public class DroneMenu extends JPanel implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    private JTextField searchField;
    private JButton searchButton;
    private JTable table;
    private DataFactory factory;

    private Drone selectedDrone;
    private String[] columnNames = {"Nr.",
            "ID",
            "DroneType",
            "Created",
            "Serialnr",
            "CarrWeight",
            "CarrType"};
    private Object[][] droneMenuData ;


    /**
     * Constructs a new DroneMenu with the specified list of drones.
     *
     * @param drones A LinkedList of Drone objects to be displayed in the table.
     */
    public DroneMenu(ArrayList<DataStorage> data, DataFactory factory) {

        super(new GridLayout(1, 0));
        this.factory  = factory;
        LOGGER.info("Initializing DroneMenu...");

        JFrame frame = (JFrame) createFrame();
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        initiallizeGuiData(data);

        table = new JTable(droneMenuData, columnNames) {
            /**
             * Determines whether a cell in the table is editable. This implementation makes all
             * cells in the tabe non-editable.
             *
             * @param row     The row index of the cell.
             * @param column   The coloumn index of the cell.
             * @return false as none of the cells are editable.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //edits specific column width
        TableColumn columnNr = table.getColumnModel().getColumn(0);
        columnNr.setPreferredWidth(columnNr.getPreferredWidth() - 55);

        TableColumn columnID = table.getColumnModel().getColumn(1);
        columnID.setPreferredWidth(columnID.getPreferredWidth() - 55);

        TableColumn columnsr = table.getColumnModel().getColumn(4);
        columnsr.setPreferredWidth(columnsr.getPreferredWidth() + 15);

        TableColumn columnCW = table.getColumnModel().getColumn(5);
        columnCW.setPreferredWidth(columnCW.getPreferredWidth() - 35);

        TableColumn columnCT = table.getColumnModel().getColumn(6);
        columnCT.setPreferredWidth(columnCT.getPreferredWidth() - 35);

        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout()); // Setting a layout manager to the container
        this.add(scrollPane, BorderLayout.CENTER);

        LOGGER.info("DroneMenu initialized.");
    }

    /**
     * Creates and displays the main GUI frame for the drone table.
     *
     * @param drones A LinkedList of Drone objects to be displayed in the table.
     */
    public static void createDroneTableGUI(ArrayList<DataStorage> data, DataFactory factory) {
        LOGGER.info("Creating Drone Table GUI...");

        DroneMenu droneM = new DroneMenu(data, factory);

        LOGGER.info("Drone Table GUI created.");
    }

    private Frame createFrame(){
        JFrame frame = new JFrame("Drone Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);

        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }

    private String formatCreatedDateTime(String created){
        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(created);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return dateTime.format(formatter);
        } catch (Exception e){
            // Eyüp Logger
            return created;
        }
    }
    /**
     * Creates and returns the menu bar with different menu items.
     *
     * @return JMenuBar for the DroneMenu
     */
    private JMenuBar createMenuBar() {
        LOGGER.info("Creating Menu Bar...");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        addItemToMenuWithActionCommand(menu, "Drone Types", "dronet");
        addItemToMenuWithActionCommand(menu, "Drone Dynamics", "droned");
        addItemToMenuWithActionCommand(menu, "Refresh", "refresh");
        addItemToMenuWithActionCommand(menu, "Credits", "droned");

        LOGGER.info("Menu Bar created.");
        return menuBar;
    }

    private void addItemToMenuWithActionCommand(JMenu menu, String itemName, String actionCmd){
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.setActionCommand(actionCmd);
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    private void initiallizeGuiData(ArrayList<DataStorage> data){
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


    /**
     * Handles actions performed by the user, such as selecting menu items.
     *
     * @param e The ActionEvent object representing the user's action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.info("Action Performed: " + e.getActionCommand());

        if ("droned".equals(e.getActionCommand())) {
            // openDroneD();
            DroneDynamicsMenu.createDroneDynamicsOverview(factory.getDroneDynamics().getFirst());
        } else if ("dronet".equals(e.getActionCommand())) {
            DroneTypeMenu.createDroneTypeTableGUI(factory.getDataStorage());
        } else if ("credits".equals(e.getActionCommand())) {
            CreditsMenu.createCreditList();
        } else if ("refresh".equals(e.getActionCommand())){
            //  Refresh refreshaction = new Refresh();
        }
        else {
            quit();
        }
    }
    //private void openDroneD(){

    //}

    /**
     * Quits the application
     */
    private static void quit() {
        LOGGER.info("Exiting application.");
        System.exit(0);
    }
}