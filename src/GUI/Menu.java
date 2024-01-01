package GUI;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import processing.*;
import data.*;

public class Menu extends JPanel implements ActionListener {
    JTextArea output;
    JScrollPane scrollPane;
    private boolean DEBUG = false;
    /*VPN
    public static LinkedList<Drone> drones = new LinkedList<>();
    public static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
    public static JSONDeruloHelper helper = new JSONDeruloHelper();
    */

    //Menu ist die Tabelle
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
/*VPN
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
                        "2",
                        "Joe",
                        "Brown",
                        "Pool",
                        new Integer(10),
                        new Boolean(false)}
        };

        final JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        table.setFillsViewportHeight(true);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuBar.add(menu);

        menuItem = new JMenuItem("Drone");
        menu.add(menuItem);

        menuItem = new JMenuItem("DroneType");
        menu.add(menuItem);

        menuItem = new JMenuItem("DroneDynamic");
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Credits");
        menuItem.setActionCommand("credits"); // Set action command for the drone menu item
        menuItem.addActionListener(this); // Add ActionListener for the drone menu item
        menu.add(menuItem);


        menu = new JMenu("Menu2");
        menuBar.add(menu);


        return menuBar;
    }

    public void actionPerformed(ActionEvent e) {
        if ("credits".equals(e.getActionCommand())) {
            openDroneWindow();
        }
         else {
            quit();
        }
    }

    protected void openDroneWindow() {
        JFrame droneFrame = new JFrame("New Window");
        JLabel label = new JLabel("FRA UAS Java Project + Namen");

        //label.setHorizontalAlignment(JLabel.CENTER);
        //label.setVerticalAlignment(JLabel.LEFT);
        droneFrame.add(label);

        droneFrame.setSize(300, 200);
        droneFrame.setVisible(true);
    }
    protected void quit() {System.exit(0);}

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

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Drone Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Menu demo = new Menu();
        frame.setJMenuBar(demo.createMenuBar());
        demo.setOpaque(true);
        frame.setContentPane(demo);

        frame.setSize(350, 250);
        frame.setVisible(true);
    }




    public static void main(String[] args) throws IOException {
/*VPN
        drones = helper.getDrones();
        droneTypes = helper.getDroneTypes();
        helper.droneTypeToDroneLinker(droneTypes, drones);
        helper.addDroneDynamicsData(drones);
*/

        javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {createAndShowGUI();}});
    }
}