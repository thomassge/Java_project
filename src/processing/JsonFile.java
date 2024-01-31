package processing;

import data.DroneType;
import util.Streamer;
import util.WebserverDataFetcher;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * This abstract class contains logic that a JsonFile needs. A JsonFile holds the data that we
 * fetch from the webserver.
 */
public abstract class JsonFile {
    private static final Logger LOGGER = Logger.getLogger(DroneType.class.getName());

    /**
     * This method saves the specific data in a file.
     * @param url Takes in the url of the data that needs to be fetched and saved as a String.
     * @param limit Takes in an integer of the limit that wants to be set in the URL for data retrieving.
     * @param filename Takes in the path to the filename, that the data should be saved to.
     */
    public void saveAsFile(String url, int limit, String filename) {
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=" + limit);
        LOGGER.log(Level.INFO,"Savind  Data from Webserver in file ...");
        Streamer streamer = new Streamer();
        streamer.writer(jsonString, filename);
    }
    /**
     *  Checks the number of data entries in a specified JSON file.
     *  The method reads a limited number of characters from the file and extracts the count.
     * @param filename The file path of the JSON file to check.
     * @return The count of entries in the file or 0 if an error occurs.
     */
    public static int checkFileCount(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder jsonContent = new StringBuilder();
            int limit = 20;
            int readChars = 0;
            int currentChar = 0;
            while ((currentChar = reader.read()) != -1 && readChars < limit) {
                jsonContent.append((char) currentChar);
                readChars++;
            }
            reader.close();
            return Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "LocalCount Exception: Count is 0.");
            return 0;
        }
    }
    /**
     * Creates a new file if it does not already exist.
     * @param filename The file path of the file to be created.
     */
    public void createFile(String filename) {
        if (!(new File(filename).exists())) {
            new File(filename);
            LOGGER.log(Level.INFO, filename + " created.");
        }
    }
}
