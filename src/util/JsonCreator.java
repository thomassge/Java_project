package util;

import java.io.*;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * THIS CLASS IS NOT TO BE TAKEN INTO ACCOUNT WHEN GRADING THIS PROJECT.
 * IT IS NOT NECESSARY TO THE FUNCTIONALITY OF THE PROGRAM IN ANY WAY.
 * NONETHELESS IT IS THE MOST IMPORTANT CLASS.
 */
public class JsonCreator extends JFrame {
    public JsonCreator() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Test Sound Clip");
        this.setSize(300, 200);
        this.setVisible(false);
        Random rand = new Random();
        int rng2 = rand.nextInt(31) + 1;
        if(rng2 == 1) {
            try {
                int rng = rand.nextInt(8) + 1;
                // Open an audio input stream.
                URL url = this.getClass().getClassLoader().getResource(".idea/libraries/group4special/DroneDynamics" + rng + ".wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public JsonCreator(String filename) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Test Sound Clip");
        this.setSize(300, 200);
        this.setVisible(false);

        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JsonCreator();
    }
}