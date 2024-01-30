package util;

import java.io.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Streamer {
    public String reader(String filename) {
        StringBuilder responseContent;
        try {
            String data;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            responseContent = new StringBuilder();
            while ((data = reader.readLine()) != null) {
                responseContent.append(data);
            }   // Erschafft den String
            reader.close();
        } catch (FileNotFoundException e) {
            Logger.getLogger(Level.INFO + "In reader: " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            Logger.getLogger(Level.INFO + "In reader: " + e);
            throw new RuntimeException(e);
        }
        return responseContent.toString();
    }

    public void writer(String data, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            Logger.getLogger(Level.INFO + "In writer: " + e);
            throw new RuntimeException(e);
        }
    }
}