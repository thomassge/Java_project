package GUI;

import javax.swing.*;
import java.awt.*;

public class CredtisMenu {

    //Click Credits
    public static void openCreditsWindow() {
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

    //for Credit content alignment
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
}
