package GUI;

import data.Drone;
import data.DroneType;
import processing.JSONDeruloHelper;

import javax.swing.*;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class DroneMenu extends JPanel implements ActionListener {
    private JTextField searchField;
    private JButton searchButton;
    private JTable table;
    private LinkedList<Drone> drones;
    private Drone selectedDrone;

    public DroneMenu(LinkedList<Drone> drones) {

        super(new GridLayout(1, 0));
        this.drones = drones;

        String[] columnNames = {
                "Nr.",
                "ID",
                "DroneType",
                "Created",
                "Serialnr",
                "CarrWeight",
                "CarrType"};

        // Create a data array with the size of the drones list
        Object[][] data = new Object[drones.size()][columnNames.length];

        // Loop through the drones list to fill the data array
        for (int i = 0; i < drones.size(); i++) {
            data[i][0] = i + 1; // "Nr." column
            // Fetch data for each drone and populate the respective columns
            data[i][1] = drones.get(i).getId();
            data[i][2] = drones.get(i).getDroneTypeObject().getTypename(); // ---->>>>> funktioniert nicht oder DOCH?
            data[i][3] = drones.get(i).getCreated();
            data[i][4] = drones.get(i).getSerialnumber();
            data[i][5] = drones.get(i).getCarriageWeight();
            data[i][6] = drones.get(i).getCarriageType();
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




        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        table.setFillsViewportHeight(true);

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

                // After applying the filter, check if the selected row is still visible and select it
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    if (!table.getSelectionModel().isSelectedIndex(selectedRow)) {
                        // If the selected row is not visible, select the row for the previously selected drone
                        selectedRow = table.getRowSorter().convertRowIndexToView(drones.indexOf(selectedDrone));
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }

            }
        });



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


    public static void createDroneTableGUI(LinkedList<Drone> drones) {
        JFrame frame = new JFrame("Drone Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DroneMenu droneM = new DroneMenu(drones);

        frame.setJMenuBar(droneM.createMenuBar());
        frame.setContentPane(droneM);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenu menu2;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();


        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);

        menuItem = new JMenuItem("DroneType",KeyEvent.VK_T);
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

        //hf gl
        menu2 = new JMenu("Refresh");
        menu2.setMnemonic(KeyEvent.VK_R);
        menuBar.add(menu2);

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

        //search fenster
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        //searchPanel.add(searchButton);
        menuBar.add(searchPanel);

        return menuBar;
    }

    //Click action
    public void actionPerformed(ActionEvent e) {
        if ("droned".equals(e.getActionCommand())) {
            DroneDynamicsMenu.createDroneDynamicsOverview((ProgramStart.drones.getFirst().getDroneDynamicsArrayList()));
        }
        else if ("dronet".equals(e.getActionCommand())) {
            DroneTypeMenu.createDroneTypeTableGUI(ProgramStart.droneTypes);
        }
        else if ("credits".equals(e.getActionCommand())) {
            CreditsMenu.createCreditList();
        }
        else {
            quit();
        }
    }
    public static void quit(){System.exit(0);}

}













