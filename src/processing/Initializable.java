package processing;

import util.Streamer;
import util.WebserverDataFetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public interface Initializable<T> {

    public T initialise();

    public default void saveAsFile(String url, int limit, String filename) {
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=" + limit);
        //LOGGER.log(Level.INFO,"Copying Drone Data from Webserver in file ...");

        Streamer streamer = new Streamer();
        streamer.writer(jsonString, filename);
    }

    public default int checkLocalCount(String filename) {
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
            //LOGGER.log(Level.INFO, "LocalCount Exception: Count is 0.");
            return 0;
        }
    }

    public default void createFile(String filename) {
        if (!(new File(filename).exists())) {
            new File(filename);
            //LOGGER.log(Level.INFO, filename + " created.");
        }
    }



}
