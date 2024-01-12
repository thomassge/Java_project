/**
 * This class is responsible for creating and managing the GUI for displaying
 * a list of drones and their details. It includes features for searching, viewing drone details,
 * and navigating to different parts of the application.
 */
package GUI;

import data.Drone;
import data.DroneType;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.logging.Logger;

public class DroneMenu extends JPanel implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    private JTextField searchField;
    private JButton searchButton;
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

            //Columnwidth

            /**
             * Prepares the renderer for each cell of the table. This method is overridden to cutomize
             * the appearence of cells, including alignment and other visual properties.
             *
             * @param renderer  The TableCellRenderer to prepare.
             * @param row       The row of the cell to render, where 0 is the first row.
             * @param column    The column of the cell to render, where 0 is the first column.
             * @return The Component that is used to draw the cell.
             */
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (comp instanceof JLabel) {
                    if (column == 0 || column == 1 || column == 2) {
                        ((JLabel) comp).setHorizontalAlignment(SwingConstants.LEFT); // Align DroneType and Serialnr to the left
                    } else {
                        ((JLabel) comp).setHorizontalAlignment(SwingConstants.RIGHT); // Align other columns to the right
                    }
                }
                return comp;
            }
        };

        /** Tabelle sortieren enthält fehler. Nicht wirklich nötig
         TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
         table.setRowSorter(sorter);
         sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
         // Sort by first column "0" ("ID") in ascending order
         */

        // click Drone/ Column
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        // Extract the DroneType from the selected row
                        selectedDrone = drones.get(table.convertRowIndexToModel(row));
                        openDroneDetailsFrame(selectedDrone.getDroneTypeObject());
                    }
                }
            }
        });

        //??? ACHTUNG WER KANN SAGEN WAS DAS MACHT?
        //table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        //table.setFillsViewportHeight(true);

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

        /**
         * Um die search funktion zu dynamisch zu triggern ist DocumentListener zum textfeld hinzugefügt
         * This code attaches a DocumentListener to the text field.
         * Whenever the text changes (insertion, deletion, or modification), the search() method is triggered,
         * which filters the table based on the text entered.
         */
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            /**
             * Invoked when text is inserted into the document. Triggers the search
             * functionality to filter the table based on the updated text.
             *
             * @param e The document event representing the insert action.
             */
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            /**
             * Invoked when text is removed from the document. Triggers the search
             * functionality to update the table filter based on the modified text.
             *
             * @param e The document event representing the remove action.
             */
            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }


            /**
             * Invoked when text in the document undergoes a change. This method
             * is not used in this implementation but is required by the DocumentListener interface.
             *
             * @param e The document event representing the change.
             */
            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String searchText = searchField.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));

                // After applying the filter, check if the selected row is still visible and select it
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    if (!table.getSelectionModel().isSelectedIndex(selectedRow)) {
                        // If the selected row is not visible, select the row for the peviously selected drone
                        selectedRow = table.getRowSorter().convertRowIndexToView(drones.indexOf(selectedDrone));
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        //this.add(searchPanel, BorderLayout.NORTH);

        LOGGER.info("DroneMenu initialized.");
    }

    // Method for new frame with details

    /**
     * Opens a detailed frame showing specific information about a selected drone type.
     *
     * @param drone The DroneType object containing the details to be displayed.
     */
    private void openDroneDetailsFrame(DroneType drone) {
        JFrame detailsFrame = new JFrame("Drone Details");
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));

        // Add labels to display drone details
        detailsPanel.add(new JLabel("ID: " + drone.getDroneTypeID()));
        detailsPanel.add(new JLabel("Manufacturer: " + drone.getManufacturer()));
        detailsPanel.add(new JLabel("Typename: " + drone.getTypename()));
        detailsPanel.add(new JLabel("Weight: " + drone.getWeight()));
        detailsPanel.add(new JLabel("Maximum Speed: " + drone.getMaximumSpeed()));
        detailsPanel.add(new JLabel("Battery Capacity: " + drone.getBatteryCapacity()));
        detailsPanel.add(new JLabel("Control Range: " + drone.getControlRange()));
        detailsPanel.add(new JLabel("Maximum Carriage: " + drone.getMaximumCarriage()));

        detailsFrame.add(detailsPanel);
        detailsFrame.pack();
        //detailsFrame.setSize(200,200); if we want to decide about framesize
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }

    //creating the main frame "Drone Simulator"

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
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("DroneType", KeyEvent.VK_T);
        menuItem.setActionCommand("dronet"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);

        menuItem = new JMenuItem("DroneDynamic", KeyEvent.VK_D);
        menuItem.setActionCommand("droned"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Credits", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("credits"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);

        /* //hf gl
        JMenu menu2 = new JMenu("Refresh");
        menu2.setMnemonic(KeyEvent.VK_R);
        menuBar.add(menu2);
         */

        /*
        //Könnte man noch adden, wenn einer Lust hat bittegerne
        //achtung bei klick dark/light schließt alles (z 207 actionperformed() else quit)
        //CHange to dark/lightmode
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(menu);

        ButtonGroup group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("Light Mode");
        rbMenuItem.setSelected(true); // Set the default mode to Light Mode
        rbMenuItem.setMnemonic(KeyEvent.VK_L);
        rbMenuItem.setActionCommand("lightMode");
        rbMenuItem.addActionListener(this);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Dark Mode");
        rbMenuItem.setMnemonic(KeyEvent.VK_D);
        rbMenuItem.setActionCommand("darkMode");
        rbMenuItem.addActionListener(this);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);
 */

        //search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        //serchPanel.add(searchButton);
        menuBar.add(searchPanel);

        LOGGER.info("Menu Bar created.");
        return menuBar;
    }

    // Click action

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
        } else {
            quit();
        }
    }

    /**
     * Quits the application
     */
    public static void quit() {
        LOGGER.info("Exiting application.");
        System.exit(0);
    }
}
