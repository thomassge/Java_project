package GUI;

//import jdk.javadoc.internal.tool.JavadocLog;

import javax.swing.*;
import java.awt.*;

public class GuiDroneDynamicsTL {
    public static void main(String[] args) {
        JFrame frame = new JFrame("DroneDynamics Data of a specific Drone" );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add panel to modify and use Container-objects
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // method to create a label and add the to the panel
        addtoPanel(panel, "ID: ", 0, 0);
        addtoPanel(panel, "Name: ", 0, 1);
        addtoPanel(panel, "Serialnumber: ", 0, 2);
        addtoPanel(panel, "DroneType: ", 0, 3);
        addtoPanel(panel, "Gewicht: ", 0, 4);
        addtoPanel(panel, "Carriage Type: ", 0, 5);

        frame.setVisible(true);
    }

    public static void addtoPanel(JPanel panel, String labelText, int gridx, int gridy){
        JLabel label = new JLabel(labelText);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gridx;
        gbc.gridy =  gridy;
        gbc.anchor = GridBagConstraints.LINE_START;                     // Setzt Anker auf oberen Rand
        gbc.insets = new Insets(10, 0, 0, 0);   //
        panel.add(label, gbc);
    }
}