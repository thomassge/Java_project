package GUI;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import data.*;
import dronesim.*;
import processing.*;
import services.*;

public class Menu extends JPanel implements ActionListener {
    JTextArea output;
    JScrollPane scrollPane;
    private boolean DEBUG = false;
    //VPN
    /*
    public static LinkedList<Drone> drones = new LinkedList<>();
    public static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
    public static JSONDeruloHelper helper = new JSONDeruloHelper();
     */


    //Tabelleninhalt
    public Menu() {
        super(new GridLayout(1,0));

        String[] columnNames = {
                "Nr.",
                "DroneType",
                "Serialnr",
                "ID",
                "CarrWeight",
                "CarrType"};

        Object[][] data = {
//VPN
                /*
                {
                        "1",
                        drones.getFirst().getDroneTypeObject().getTypename(),
                        drones.getFirst().getSerialnumber(),
                        drones.getFirst().getId(),
                        drones.getFirst().getCarriageWeight(),
                        drones.getFirst().getCarriageType(),
                },

                 */

                {
                        new Integer(2),
                        "S5C",
                        "SnS5-etc..",
                        "#ID",
                        "73",
                        "ACT"}
        };

        final JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        table.setFillsViewportHeight(true);
        //?

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    //?

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



    //Menuleiste
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuBar.add(menu);

        menuItem = new JMenuItem("DroneType");
        menuItem.setActionCommand("dronet"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);

        menuItem = new JMenuItem("DroneDynamic");
        menuItem.setActionCommand("droned"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Credits");
        menuItem.setActionCommand("credits"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);

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

    //Klick auf DroneType
    protected void opendronetWindow() {
        JFrame droneFrame = new JFrame("Drone Type");
        JPanel panel = new JPanel();
        droneFrame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addtoPanel(panel, "Drone Type data", 0, 0);

        droneFrame.setSize(550, 550);
        droneFrame.setLocationRelativeTo(null);
        droneFrame.setVisible(true);
    }

    //Klick auf DroneDynamic
    protected void opendronedWindow() {
        JFrame droneFrame = new JFrame("Drone Dynamics");
        JPanel panel = new JPanel();
        droneFrame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addtoPanel(panel, "Drone Dynamics", 0, 0);

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
        /*
        drones = helper.getDrones();
        droneTypes = helper.getDroneTypes();
        helper.droneTypeToDroneLinker(droneTypes, drones);
        helper.addDroneDynamicsData(drones);
         */

        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {createAndShowGUI();}});
    }
}