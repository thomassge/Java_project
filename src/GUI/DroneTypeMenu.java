package GUI;

import data.DroneType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.RowFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DroneTypeMenu extends JPanel {
    private JFrame frame;

    public DroneTypeMenu(LinkedList<DroneType> droneTypes) {
        super(new BorderLayout());

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


        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        table.setFillsViewportHeight(true);

        //edits specific column width
        TableColumn columnNr = table.getColumnModel().getColumn(0);
        columnNr.setPreferredWidth(columnNr.getPreferredWidth() - 45);

        TableColumn columnID = table.getColumnModel().getColumn(1);
        columnID.setPreferredWidth(columnID.getPreferredWidth() + 5);

        JScrollPane scrollPane = new JScrollPane(table);
        this.setLayout(new BorderLayout()); // Setting a layout manager to the container
        this.add(scrollPane, BorderLayout.CENTER);

        // Search func
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        });

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Back");
        exitItem.addActionListener(e -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            currentFrame.dispose();
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        //hf gl
        JMenu menu2 = new JMenu("Refresh");
        menuBar.add(menu2);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        menuBar.add(searchPanel);

        frame = new JFrame("Drone Types");
        frame.setJMenuBar(menuBar);

        frame.setContentPane(this);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        exitItem.addActionListener(e -> {
            if (frame != null) {
                frame.dispose();
            }
        });

        this.frame = frame; // Assign the frame reference to the class variable


    }

    public static void createDroneTypeTableGUI(LinkedList<DroneType> droneTypes) {
        new DroneTypeMenu(droneTypes);
    }
}
























