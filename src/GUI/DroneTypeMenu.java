package GUI;

import data.DroneType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DroneTypeMenu extends JPanel {
    private JFrame frame;
    private LinkedList<DroneType> droneTypes;
    //private Timer refreshTimer;

    public DroneTypeMenu(LinkedList<DroneType> droneTypes) {
        super(new BorderLayout());
        this.droneTypes = droneTypes;

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

        Object[][] data = new Object[droneTypes.size()][columnNames.length];

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

        //unveränderbarkeit
        final JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells as non-editable
            }

            //columnbreite
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

        //click auf drone/column
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) { // Detect click abgewandelt
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        // Extract the DroneType from the selected row
                        DroneType selectedDrone = droneTypes.get(row);

                        // Open a new frame to display drone details
                        openDroneDetailsFrame(selectedDrone);
                    }
                }
            }
        });


        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        table.setFillsViewportHeight(true);

        //edits specific column width
        TableColumn columnNr = table.getColumnModel().getColumn(0);
        columnNr.setPreferredWidth(columnNr.getPreferredWidth() - 45);

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
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String searchText = searchField.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
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

        //this closes only the windows, not the whole programm
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
    }

    //Methode für neuen frame mit details
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

    public static void createDroneTypeTableGUI(LinkedList<DroneType> droneTypes) {
        new DroneTypeMenu(droneTypes);
    }
}
























