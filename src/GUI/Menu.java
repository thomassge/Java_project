package GUI;

import data.*;
import dronesim.*;
import processing.*;
import services.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.IOException;

/* Wenn einer Lust hat
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
 */


public class Menu extends JPanel implements ActionListener {
    JTextArea output;
    JScrollPane scrollPane;
    private boolean DEBUG = false;

    public static LinkedList<Drone> drones = new LinkedList<>();
    public static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
    public static JSONDeruloHelper helper = new JSONDeruloHelper();


    //Konstruktor Tabelleninhalt
    public Menu() {
        super(new GridLayout(1,0));
        String[] columnNames = {
                "Nr.",
                "DroneType",
                "Serialnr",
                "ID",
                "CarrWeight",
                "CarrType"};

        // Create a data array with the size of the drones list
        Object[][] data = new Object[drones.size()][columnNames.length];

        // Loop through the drones list to populate the data array
        for (int i = 0; i < drones.size(); i++) {
            data[i][0] = i + 1; // "Nr." column

            // Fetch data for each drone and populate the respective columns
            data[i][1] = drones.get(i).getDroneTypeObject().getTypename();
            data[i][2] = drones.get(i).getSerialnumber();
            data[i][3] = drones.get(i).getId();
            data[i][4] = drones.get(i).getCarriageWeight();
            data[i][5] = drones.get(i).getCarriageType();
        };

        final JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells as non-editable
            }

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
        //um serialnr komplett zu sehen
        TableColumn column = table.getColumnModel().getColumn(2);
        column.setPreferredWidth(column.getPreferredWidth() + 30);

        //?
/*
        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

 */

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);


    }

    //?
/*
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

 */


    //Menuleiste
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

        //Suchleiste
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        menuBar.add(searchPanel);


        return menuBar;
    }

    //Aktion bei Klick
    public void actionPerformed(ActionEvent e) {
        if ("droned".equals(e.getActionCommand())) {
            opendronedWindow();
        }
        else if ("dronet".equals(e.getActionCommand())) {
            opendronetWindow();
        }
        else if ("credits".equals(e.getActionCommand())) {
            openCreditsWindow();
        }
         else {
            quit();
        }
    }
    //alt
    /*
    protected void opendronetWindow() {
        JFrame droneFrame = new JFrame("Drone Type");
        JPanel panel = new JPanel();
        droneFrame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        droneFrame.setSize(550, 550);
        droneFrame.setLocationRelativeTo(null);
        droneFrame.setVisible(true);
    }
     */

    //Klick auf DroneType öffnet neue klasse DroneD
    protected void opendronedWindow() {
        DroneD droneDWindow = new DroneD();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                droneDWindow.createAndShowGUI();
            }
        });

    }

    //Klick auf DroneType
    protected void opendronetWindow() {

        JFrame droneFrame = new JFrame("Drone Type");
        JPanel panel = new JPanel();
        droneFrame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addtoPanel(panel, "Drone Type", 0, 0);

        droneFrame.setSize(550, 550);
        droneFrame.setLocationRelativeTo(null);
        droneFrame.setVisible(true);

    }

    //Klick auf Credits
    protected void openCreditsWindow() {
        JFrame droneFrame = new JFrame("Credits");
        JPanel panel = new JPanel();
        droneFrame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addtoPanel(panel, "FRA UAS - OOP Java", 0, 0);
        addtoPanel(panel, "Professor: Prof. Dr. Müller-Bady", 0, 1);
        addtoPanel(panel, "Drone Project  by: Group 4", 0, 2);
        addtoPanel(panel, "Eyüp Korkmaz - matr.no. 1448519", 0, 3);
        addtoPanel(panel, "Leon Oet - matr.no. 1267114", 0, 4);
        addtoPanel(panel, "Marc O. Difflipp – matr.no. 1028010", 0, 5);
        addtoPanel(panel, "Robin Remines - matr.no. 1459883", 0, 6);
        addtoPanel(panel, "Thomas Levantis – matr.no. 1429473 ", 0, 7);

        droneFrame.setSize(300, 300);
        droneFrame.setLocationRelativeTo(null);
        droneFrame.setVisible(true);
    }

    //Für Inhalt Credits
    public static void addtoPanel(JPanel panel, String labelText, int gridx, int gridy){
        JLabel label = new JLabel(labelText);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gridx;
        gbc.gridy =  gridy;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(label, gbc);
    }
    //quitfkt
    protected void quit() {System.exit(0);}

    //?
/*
    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);

        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);

        return contentPane;
    }

 */
    //GUI mit Menuleiste aus Menu()
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Drone Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Menu demo = new Menu();
        frame.setJMenuBar(demo.createMenuBar());
        //demo.setOpaque(true);?
        frame.setContentPane(demo);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    public static void main(String[] args) throws IOException {
//VPN
        drones = helper.getDrones();
        droneTypes = helper.getDroneTypes();
        helper.droneTypeToDroneLinker(droneTypes, drones);
        helper.addDroneDynamicsData(drones);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {createAndShowGUI();}});
    }

}