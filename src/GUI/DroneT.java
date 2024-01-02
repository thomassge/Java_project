package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Menu öffnet DroneT bei Klick auf DroneType
public class DroneT extends JPanel
        implements ActionListener {
    JLabel picture;

    public DroneT() {
        super(new BorderLayout());

        String[] Strings = { "Drone1", "Drone2", "Drone3"};
        JComboBox List = new JComboBox(Strings);
        List.setSelectedIndex(2);//Anzahl für Dronen-ab0
        List.addActionListener(this);

        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.CENTER);
        updateLabel(Strings[List.getSelectedIndex()]);
        picture.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        picture.setPreferredSize(new Dimension(177, 122+10));

        add(List, BorderLayout.PAGE_START);
        add(picture, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }


    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String petName = (String)cb.getSelectedItem();
        updateLabel(petName);
    }

    protected void updateLabel(String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".jpg");
        picture.setIcon(icon);
        if (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
    }

    /* Return ImageIcon, or null if path invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DroneT.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    static void createAndShowGUI() {
        JFrame frame = new JFrame("DroneT");
        JComponent newContentPane = new DroneT();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        frame.setSize(550, 550);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}