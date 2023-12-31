/**
 * This class creates a GUI for displaying and interacting with a list of DroneTypes.
 * It includes features such as sorting, searching and detailed view of drone types.
 */
package GUI;

import data.DroneType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneTypeMenu extends JPanel {
    private JFrame frame;
    private LinkedList<DroneType> droneTypes;
    private DroneType selectedDrone;
    //private Timer refreshTimer;
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

        //uneditable column
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
                return false; // Set all cells as non-editable
            }

            //columnwidth

            /**
             * Prepares the renderer for each cell of the table. This method is overridden to customize
             * the appearence of cells in the table, such as setting alignment for text.
             * It affects how each cell in the table is displayed.
             *
             * @param renderer  The TableCellRenderer to prepare.
             * @param row       The row of the cell to render, where 0 is the first row.
             * @param column    The column of the cell to render, where 0 is th first column.
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

        // Sort by first column "0" ("ID") in ascending order
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING))); // Sort by the first column ("ID") in ascending order

        //click on drone/column
        table.addMouseListener(new MouseAdapter() {

            /**
             * Handles mouse click events on the table. When a table row is double-clicked, this method is
             * invoked to perform an action, such as opening a detailed view of the selected drone type.
             *
             * @param e The MouseEvent object representing the mouse click action.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                LOGGER.info("Mouse clicked on table.");

                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        // Extract the DroneType from the selected row
                        selectedDrone = droneTypes.get(table.convertRowIndexToModel(row));
                        openDroneDetailsFrame(selectedDrone);
                    }
                }
            }
        });


        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        table.setFillsViewportHeight(true);

        //edits specific column width
        TableColumn columnNr = table.getColumnModel().getColumn(0);
        columnNr.setPreferredWidth(columnNr.getPreferredWidth() - 40);

        TableColumn columnID = table.getColumnModel().getColumn(1);
        columnID.setPreferredWidth(columnID.getPreferredWidth() + 5);

        /*
        // Initialize and start the timer for refresh every x millisec set
        refreshTimer = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // refresh action
                // fetch updated drone type data - update table
                LinkedList<DroneType> updatedDroneTypes = fetchUpdatedData(); // Replace this with your data-fetching logic
                // Update table with new data
                updateTable(updatedDroneTypes); // Implement this method to update  table
                // Repaint the GUI
                repaint();
            }
        });
        refreshTimer.start();

         */

        //??
        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout()); // Setting a layout manager to the container
        this.add(scrollPane, BorderLayout.CENTER);

        /**
         * Um die search funktion zu dynamisch zu triggern ist DocumentListener zum textfeld hinzugefügt
         * This code attaches a DocumentListener to the text field.
         * Whenever the text changes (insertion, deletion, or modification), the search() method is triggered,
         * which filters the table based on the text entered.
         */
        JTextField searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            /**
             * Invoked when text is inserted into the search field. This triggers the search
             * functionality, updating the table's filter based on the current text content.
             *
             * @param e The document event indicating that text was inserted.
             */
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                LOGGER.info("Character inserted.");
                search();
            }

            /**
             * Invoked when text is removed from the search field. This also triggers the search
             * functionality to update the table's filter based on the modified text content.
             *
             * @param e The document event indicating that text was removed.
             */
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                LOGGER.info("Character removed.");
                search();
            }

            /**
             * Invoked when a change occurs to the text in the search field. This method is part of the
             * DocumentListener interface but may not be directly used in this implementation.
             *
             * @param e The document event indicating a change.
             */
            @Override
            public void changedUpdate(DocumentEvent e) {
                LOGGER.info("Character changed.");
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
                        // If the selected row is not visible, select the row for the previously selected drone
                        selectedRow = table.getRowSorter().convertRowIndexToView(droneTypes.indexOf(selectedDrone));
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }


        });

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");
        JMenuItem exitItem = new JMenuItem("Back");

        //actionlistener for closing window
        exitItem.addActionListener(e -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            currentFrame.dispose();
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        //this closes only the windows, not the whole programm!
        exitItem.addActionListener(e -> {
            if (frame != null) {
                frame.dispose();
            }
        });

        //hf gl
        //JMenu menu2 = new JMenu("Refresh");
        //menuBar.add(menu2);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        //searchPanel.add(searchButton);
        menuBar.add(searchPanel);

        frame = new JFrame("Drone Type");
        frame.setJMenuBar(menuBar);
        frame.setContentPane(this);
        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //this.frame = frame; // Assign the frame reference to the class variable wofür???
        LOGGER.info("DroneTypeMenu initialized.");

    }

    //Method for NEW mini frame with details

    /**
     * Opens a new frame displaying details of the selected DroneType.
     *
     * @param drone The DroeType object containing details to be displayed.
     */
    private void openDroneDetailsFrame(DroneType drone) {
        LOGGER.info("Opening drone details frame...");

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
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }


    /*
    // Method placeholders, replace these with your actual data retrieval and table update logic
    private LinkedList<DroneType> fetchUpdatedData() {
        // Simulate fetching updated drone type data
        LinkedList<DroneType> updatedDroneTypes = new LinkedList<>();

        // Generate sample updated data for demonstration purposes
        for (int i = 0; i < 10; i++) {
            // Create random DroneType objects
            DroneType droneType = new DroneType(
                    i,                        // Replace with ID logic
                    "Manufacturer " + i,      // Replace with Manufacturer logic
                    "Type " + i,                        // Replace with Typename logic
                    random.nextDouble() * 100,          // Replace with Weight logic
                    random.nextDouble() * 200,          // Replace with Maximum Speed logic
                    random.nextDouble() * 1000,         // Replace with Battery Capacity logic
                    random.nextDouble() * 500,          // Replace with Control Range logic
                    random.nextDouble() * 1000          // Replace with Maximum Carriage logic
            );

            updatedDroneTypes.add(droneType);
        }
        return updatedDroneTypes;
    }

    private void updateTable(LinkedList<DroneType> updatedDroneTypes) {
        // Convert updated data to a format suitable for JTable
        Object[][] newData = new Object[updatedDroneTypes.size()][8]; // Assuming 8 columns

        // Populate newData with the updated DroneType information
        for (int i = 0; i < updatedDroneTypes.size(); i++) {
            newData[i][0] = updatedDroneTypes.get(i).getDroneTypeID();
            newData[i][1] = updatedDroneTypes.get(i).getManufacturer();
            newData[i][2] = updatedDroneTypes.get(i).getTypename();
            newData[i][3] = updatedDroneTypes.get(i).getWeight();
            newData[i][4] = updatedDroneTypes.get(i).getMaximumSpeed();
            newData[i][5] = updatedDroneTypes.get(i).getBatteryCapacity();
            newData[i][6] = updatedDroneTypes.get(i).getControlRange();
            newData[i][7] = updatedDroneTypes.get(i).getMaximumCarriage();
        }

        // Get the table model and update it with the new data
        TableModel model = new javax.swing.table.DefaultTableModel(
                newData,
                new String[]{"ID", "Manufacturer", "Typename", "Weight", "Maximum Speed", "Battery Capacity", "Control Range", "Maximum Carriage"}
        );

        // Set the new model to the table
        JTable table = (JTable) ((JScrollPane) this.getComponent(0)).getViewport().getView(); // Assuming table is the first component
        table.setModel(model);
    }
     */

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