package GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DroneD extends JPanel implements ActionListener {
    JComboBox<String> petList;
    JLabel selectedImage;
    JLabel fixedImage;
    JTextArea topRightText;
    JTextArea bottomLeftText;

    public DroneD() {
        super(new BorderLayout());

        String[] petStrings = { "Drone1", "Drone2", "Drone3" };

        // Set up the combo box and the selected image on top left
        JPanel topPanel = new JPanel(new BorderLayout());
        selectedImage = new JLabel();
        petList = new JComboBox<>(petStrings);
        petList.setMaximumSize(new Dimension(50, petList.getPreferredSize().height));
        petList.setSelectedIndex(0);
        petList.addActionListener(this);
        topPanel.add(selectedImage, BorderLayout.CENTER);
        topPanel.add(petList, BorderLayout.NORTH);

        // Set up the fixed image "Drone1.jpg" on bottom right
        fixedImage = new JLabel();
        ImageIcon fixedIcon = createImageIcon("images/image.jpg");
        if (fixedIcon != null) {
            fixedImage.setIcon(fixedIcon);
        }

        // Set up the text areas
        topRightText = new JTextArea();
        topRightText.setEditable(false);
        topRightText.setFont(new Font("Arial", Font.PLAIN, 14));
        topRightText.setPreferredSize(new Dimension(200, 100));
        updateTopRightText("Drone1"); // Initial setup

        bottomLeftText = new JTextArea();
        bottomLeftText.setEditable(false);
        bottomLeftText.setFont(new Font("Arial", Font.PLAIN, 14));
        bottomLeftText.setPreferredSize(new Dimension(200, 100));

        // Add components to the panel using GridBagLayout
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gridPanel.add(topPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.gridheight = 1;

        gridPanel.add(topRightText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;

        gridPanel.add(bottomLeftText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 2;

        gridPanel.add(fixedImage, gbc);
        add(gridPanel, BorderLayout.CENTER);
        updateLabel("Drone1");
    }

    /** Listens to the combo box. */
    public void actionPerformed(ActionEvent e) {
        JComboBox<?> cb = (JComboBox<?>) e.getSource();
        String petName = (String) cb.getSelectedItem();
        updateLabel(petName);
    }

    protected void updateLabel(String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".jpg");

        if (icon != null) {
            selectedImage.setIcon(icon);
            updateTopRightText(name);
        }

        updateBottomLeftText(); // Update dynamic text
    }

    protected void updateTopRightText(String name) {
        String[] attributes = {"Name", "Serialnr", "DroneType", "Weight", "CarriageType"};
        String[] values = {"SomeName", "123456", "TypeA", "50kg", "TypeX"};

        StringBuilder text = new StringBuilder();
        text.append("Details:\n");
        for (int i = 0; i < attributes.length; i++) {
            text.append(String.format("%s: %s\n", attributes[i], values[i]));
        }
        topRightText.setText(text.toString());
    }

    protected void updateBottomLeftText() {
        String batteryStatus = "Full";
        String timeStamp = "12:00";
        String speed = "60 km/h";
        String lastSeen = "Yesterday";

        StringBuilder text = new StringBuilder();
        text.append("Battery status: ").append(batteryStatus).append("\n");
        text.append("Timestamp: ").append(timeStamp).append("\n");
        text.append("Speed: ").append(speed).append("\n");
        text.append("Last seen: ").append(lastSeen);
        bottomLeftText.setText(text.toString());
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DroneD.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("DroneD");
        DroneD droneD = new DroneD();
        frame.setContentPane(droneD);

        frame.pack();
        frame.setSize(600, 400); // Adjust the frame size as necessary
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

