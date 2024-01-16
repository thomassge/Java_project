package data;

import org.json.JSONObject;
import processing.JSONDeruloHelper;
import processing.Streamable;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDroneOperations implements Streamable {
    private static final Logger logger = Logger.getLogger(Drone.class.getName());

    public boolean checkFile(String filename) {
        if(new File(filename).exists()) {
            logger.log(Level.INFO, filename + " exists.");
            return true;
        }
        else if(!(new File(filename).exists())) {
            File newFile = new File(filename);
            logger.log(Level.INFO, filename + " created.");
            return false;
        } // should never happen
        else return false;
    }

    public int getLocalCount(String filename, int localCount) {
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

            localCount = Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
            return localCount;
        } catch (Exception e) {
            logger.log(Level.INFO, "LocalCount Exception: Count is 0.");
            return 0;
        }
    }

    public static int getServerCount(String url) { // static or nah?
        String jsonString = JSONDeruloHelper.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }

    public void saveAsFile(String url, int limit, String filename) {
        String jsonString = JSONDeruloHelper.jsonCreator(url + "?limit=" + limit);
        logger.log(Level.INFO,"Copying Drone Data from Webserver in file ...");
        writer(jsonString, filename);
    }

    public abstract void checkForNewData();

    public abstract void refresh();
}
