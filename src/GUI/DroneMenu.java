package GUI;

import data.Drone;
import data.DroneType;
import processing.JSONDeruloHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class DroneMenu extends JPanel implements ActionListener {

    public DroneMenu(LinkedList<Drone> drones) {

        super(new GridLayout(1, 0));

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

            // Fetch data for each drone and fill the respective columns
            data[i][1] = drones.get(i).getId();
<<<<<<< HEAD
<<<<<<< HEAD
            data[i][2] = drones.get(i).getDroneTypeObject();
=======
            //data[i][2] = drones.get(i).getDroneTypeObject().getTypename(); // ---->>>>> funktioniert nicht
>>>>>>> 756f53af4682c7c10a55b28fa708bc6a34ec970c
=======
            data[i][2] = drones.get(i).getDroneTypeObject().getTypename(); // ---->>>>> funktioniert nicht
>>>>>>> c7efd28a86d36716953fb9e2db88d597b36281ac
            data[i][3] = drones.get(i).getCreated();
            data[i][4] = drones.get(i).getSerialnumber();
            data[i][5] = drones.get(i).getCarriageWeight();
            data[i][6] = drones.get(i).getCarriageType();

        }

        // Create JTable with data and columnNames
        JTable table = new JTable(data, columnNames);

        // Hinzufügen der Tabelle zur JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Das JScrollPane zum Panel hinzufügen
        add(scrollPane);
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



    public static void quit(){
     System.exit(0);
    }

}
