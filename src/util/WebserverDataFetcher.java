package util;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class holds methods for creating a reader and writer object.
 * It uses FileInputStreamer and FileWriter
 */
public class WebserverDataFetcher {
    private static final Logger LOGGER = Logger.getLogger(WebserverDataFetcher.class.getName());
    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    /**
     * Creates a JSON string from the provided URL.
     * @param link The URL from which to fetch the JSON data.
     * @return A JSON string representation of the data.
     */
    public static String jsonCreator(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", TOKEN);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            LOGGER.log(Level.INFO,"ResonseCode: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            LOGGER.log(Level.INFO, "JSON Data successfully retrieved from: " + link);
            return responseContent.toString();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving JSON data from " + link + "Please seek help from adminstrator.", e);
            throw new RuntimeException(e);
        }
    }
}
