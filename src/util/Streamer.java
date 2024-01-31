package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
/**
 * This class holds methods to create reader and writer objects.
 * It uses FileInputStreamer and FileWriter
 * @author Leon Oet
 */
public class Streamer {
    private static final Logger LOGGER = Logger.getLogger(WebserverDataFetcher.class.getName());

    /**
     * This method creates a BufferedReader that uses a FileInputStream.
     * @param filename Takes in the path to the file as a String
     * @return The content of the file as a String
     */
    public String reader(String filename) {
        StringBuilder responseContent;
        try {
            String data;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            responseContent = new StringBuilder();
            while ((data = reader.readLine()) != null) {
                responseContent.append(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING,"File not Found: " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOGGER.log(Level.INFO,"IO Exception: " + e);
            throw new RuntimeException(e);
        }
        return responseContent.toString();
    }

    /**
     * This method creates a FileWriter to write data into a file.
     * @param data It takes in the data to be saved as a String
     * @param filename It also takes the path to the file as a parameter
     */
    public void writer(String data, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            LOGGER.log(Level.INFO,"IO Exception: " + e);
            throw new RuntimeException(e);
        }
    }
}