package data;

import processing.Streamable;
import util.WebserverDataFetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public interface Saveable {
    public static int checkLocalCount(String filename) {
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
            //logger.log(Level.INFO, "LocalCount Exception: Count is 0.");
            return 0;
        }
    }

    public static boolean checkFile(String filename) {
        if(new File(filename).exists()) {
            //logger.log(Level.INFO, filename + " exists.");
            return true;
        }
        else if(!(new File(filename).exists())) {
            File newFile = new File(filename);
            //logger.log(Level.INFO, filename + " created.");
            return false;
        } // should never happen
        else return false;
    }

    public static void saveAsFile(String url, int limit, String filename) {
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=" + limit);
        //logger.log(Level.INFO,"Copying Drone Data from Webserver in file ...");
        Streamable.writer(jsonString, filename);
    }

}
