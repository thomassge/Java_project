package GUI;

import data.Drone;
import jdk.internal.org.jline.terminal.TerminalBuilder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GUI_TL {
    public static void main(String[] args) {
        JFrame frame = new JFrame("DroneSim Group 4 alleeeer");
        frame.getContentPane().setLayout(new BorderLayout());

        //JLabel label = new JLabel("Drone Simulator V1");
        //frame.getContentPane().add(label, BorderLayout.PAGE_START);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350,350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        frame.getContentPane().setLayout(new BorderLayout());
        JButton b1 = new JButton("Drone");
        JButton b2 = new JButton("DroneTypes");
        JButton b3 = new JButton("DroneDynamics");
        frame.getContentPane().add(b1, BorderLayout.CENTER);
        frame.getContentPane().add(b2, BorderLayout.LINE_START);
        frame.getContentPane().add(b3, BorderLayout.LINE_END);

        // Box Layout
        BoxLayout l = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.getContentPane().setLayout(l);
        frame.getContentPane().add(b1);
        frame.getContentPane().add(b2);
        frame.getContentPane().add(b3);

        // Card Layout
        JPanel cards = new JPanel(new CardLayout());
        JPanel card1 = new JPanel();
        card1.add(b1);
        card1.add(b2);
        card1.add(b3);

        JPanel card2 = new JPanel();
        card2.add(new JTextField(20));
        cards.add(card1, "Drones");
        //cards.add(card2, "Text");

        JPanel comboBox = new JPanel();
        String[] combos = { "Drones" };
        JComboBox<String> cb = new JComboBox<String>(combos);
        cb.setEditable(false);
        comboBox.add(cb);
        frame.getContentPane().add(comboBox, BorderLayout.PAGE_START);
        frame.getContentPane().add(cards, BorderLayout.CENTER);

        // Flow Layout
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(b1);
        frame.getContentPane().add(b2);
        frame.getContentPane().add(b3);

        // Grid Layout
        frame.getContentPane().setLayout(new GridLayout(3,0));
        frame.getContentPane().add(b1);
        frame.getContentPane().add(b2);
        frame.getContentPane().add(b3);

        // Menu Bar
        JMenuBar bar = new JMenuBar();

        JMenu menu1 = new JMenu("File");
        JMenuItem item1 = new JMenuItem("Save");
        JMenuItem item2 = new JMenuItem("Open");
        menu1.add(item1);
        menu1.add(item2);

        JMenu menu2 = new JMenu("Mode");
        JRadioButtonMenuItem item3 = new JRadioButtonMenuItem("Dark");
        JRadioButtonMenuItem item4 = new JRadioButtonMenuItem("light");
        menu2.add(item3);
        menu2.add(item4);

        ButtonGroup group = new ButtonGroup();
        group.add(item3);
        group.add(item4);

        bar.add(menu1);
        bar.add(menu2);
        frame.setJMenuBar(bar);

        // Dialogs
        /*
        JOptionPane.showConfirmDialog(frame, "Are you sure?");
        JOptionPane.showMessageDialog(frame, "Error!", "Error", JOptionPane.ERROR_MESSAGE);
        Object[] options = { "Yes", "No" };
        int n = JOptionPane.showOptionDialog(frame, "Sure?", "Question",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        */

        // Simple Table
        String[] columns = {"Serialnumber"};
        Object [] data =
                {
                        {
                                System.out.println(Drone.getSerialnumber());
                        }
                }

    }
}
