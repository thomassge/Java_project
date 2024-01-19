/**
 * This class is responsible for creating and managing the GUI for displaying
 * a list of drones and their details. It includes features for searching, viewing drone details,
 * and navigating to different parts of the application.
 */
package gui;

import data.Drone;
import data.DroneType;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.logging.Logger;

public class DroneMenu extends JPanel implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    private JTable table;
    private LinkedList<Drone> drones;
    private Drone selectedDrone;

    /**
     * Constructs a new DroneMenu with the specified list of drones.
     *
     * @param drones A LinkedList of Drone objects to be displayed in the table.
     */
    public DroneMenu(LinkedList<Drone> drones) {

        super(new GridLayout(1, 0));
        this.drones = drones;
        LOGGER.info("Initializing DroneMenu...");
        String[] columnNames = {
                "Nr.",
                "ID",
                "DroneType",
                "Created",
                "Serialnr",
                "CarrWeight",
                "CarrType"
        };
        //Create a data array with the size of the drones list
        Object[][] data = new Object[drones.size()][columnNames.length];

        //Fetch data for each drone and populate the respective columns
        //Loop through the drones list to fill the data array
        for (int i = 0; i < drones.size(); i++) {
            data[i][0] = i + 1; //"Nr." column
            data[i][1] = drones.get(i).getId();
            data[i][2] = drones.get(i).getDroneTypeObject().getTypename();
            data[i][3] = drones.get(i).getCreated();
            data[i][4] = drones.get(i).getSerialnumber();
            data[i][5] = drones.get(i).getCarriageWeight();
            data[i][6] = drones.get(i).getCarriageType();
        }
        //Uneditability
        table = new JTable(data, columnNames) {

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
                return false; // Set all cells as non-editable
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
    public static void createDroneTableGUI(LinkedList<Drone> drones) {
        LOGGER.info("Creating Drone Table GUI...");
        JFrame frame = new JFrame("Drone Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DroneMenu droneM = new DroneMenu(drones);

        frame.setJMenuBar(droneM.createMenuBar());
        frame.setContentPane(droneM);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        LOGGER.info("Drone Table GUI created.");
    }

    /**
     * Creates and returns the menu bar with different menu items.
     *
     * @return JMenuBar for the DroneMenu
     */
    public JMenuBar createMenuBar() {
        LOGGER.info("Creating Menu Bar...");
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");

        JMenu refresh = new JMenu("Refresh");
        refresh.setActionCommand("refresh");
        refresh.addActionListener(this);

        JMenuItem droneType = new JMenuItem("DroneType");
        droneType.setActionCommand("dronet");
        droneType.addActionListener(this);

        JMenuItem droneDynamics = new JMenuItem("DroneDynamic");
        droneDynamics.setActionCommand("droned");
        droneDynamics.addActionListener(this);

        JMenuItem credits = new JMenuItem("Credits");
        credits.setActionCommand("credits");
        credits.addActionListener(this); 

        menuBar.add(menu);
        menuBar.add(refresh);
        menu.add(droneType);
        menu.add(droneDynamics);
        menu.addSeparator();
        menu.add(credits);

        LOGGER.info("Menu Bar created.");
        return menuBar;
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
            DroneDynamicsMenu.createDroneDynamicsOverview(ProgramStart.drones.getFirst().getDroneDynamicsArrayList());
        } else if ("dronet".equals(e.getActionCommand())) {
            DroneTypeMenu.createDroneTypeTableGUI(ProgramStart.droneTypes);
        } else if ("credits".equals(e.getActionCommand())) {
            CreditsMenu.createCreditList();
        } else if ("refresh".equals(e.getActionCommand())){

        } else {
            quit();
        }
    }

    public static void quit() {
        LOGGER.info("Exiting application.");
        System.exit(0);
    }
}
