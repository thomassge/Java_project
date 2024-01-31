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
     * @param limit Takes in an integer of the limit that will be added in the URL for a specific amount of data retrieving. The limit is the fetched server count.
     * @param filename Takes in the path to the filename, that the data should be saved to.
     */
    public void saveAsFile(String url, int limit, String filename) {
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=" + limit);
        LOGGER.log(Level.INFO,"Savind  Data from Webserver in file ...");
        Streamer streamer = new Streamer();
        streamer.writer(jsonString, filename);
    }

    /**
     * This method checks the file count by reading the first twenty characters of our JsonFile.
     * It is expected that the count information is in this sequence of characters.
     * @param filename Takes in the path to the file that needs to be searched for the count number
     * @return The count as an integer
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
     * This method checks whether a file exists and creates it if not.
     * @param filename The path to the file that needs to be created/checked.
     */
    public void createFile(String filename) {
        if (!(new File(filename).exists())) {
            new File(filename);
            LOGGER.log(Level.INFO, filename + " created.");
        }
    }
}
