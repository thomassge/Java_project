package GUI;
//import Rest.JSONDerulo;
import org.json.JSONObject;

import javax.smartcardio.Card;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Greece for the win");
        frame.getContentPane().setLayout(new BorderLayout());

        JButton b1 = new JButton("Drone");
        JButton b2 = new JButton("DroneType");
        JButton b3 = new JButton("DroneDynamics");

        //Display Buttons at Y-Axis
        BoxLayout l = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.getContentPane().setLayout(l);
        frame.getContentPane().add(b1);
        frame.getContentPane().add(b2);
        frame.getContentPane().add(b3);


        /*-------Display Buttons at diff. directions-------
        frame.getContentPane().add(b1, BorderLayout.NORTH);
        frame.getContentPane().add(b2, BorderLayout.EAST);
        frame.getContentPane().add(b3, BorderLayout.WEST);
        frame.getContentPane().add(b4, BorderLayout.SOUTH);
        */


        JPanel cards = new JPanel(new CardLayout());
        JPanel card1 = new JPanel();
        card1.add(b1);
        card1.add(b2);
        card1.add(b3);

        JPanel card2 = new JPanel();
        card2.add(new JTextField(20));

        cards.add(card1, "Drones");

        JPanel comboBox = new JPanel();
        String[] combos = { "Drones"};

        JComboBox<String> cb = new JComboBox<String>(combos);
        cb.setEditable(false);
      //  cb.addItemListener(new CardItemListener(cards));
        comboBox.add(cb);
        frame.getContentPane().add(comboBox, BorderLayout.PAGE_START);
        frame.getContentPane().add(cards, BorderLayout.CENTER);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}