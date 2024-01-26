package util;

import java.util.logging.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public class WebserverDataFetcher {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());
    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public static String urlModifier(int limit, int offset) {

        String limitUrl = "limit=" + limit;
        String connector = "&";
        String offsetUrl = "offset=" + offset;


        return "?" + limitUrl + connector + offsetUrl;
    }

    /**
     * Creates a JSON string from the provided URL.
     *
     * @param link The URL from which to fetch the JSON data.
     * @return A JSON string representation of the data.
     */
    public static String jsonCreator(String link) {
        try {
            // Step 2: Create a URL object
            URL url = new URL(link);

            // Step 3: Open a connection
            HttpURLConnection connection; // Creating an empty variable with type HttpUrlConnection;
            connection = (HttpURLConnection) url.openConnection(); // Der Rückgabewert von openConnection ist eig. 'URLConnection', deshalb das Typecasting, da wir speziell mit HTTP arbeiten und der Rückgabewert von openConnection dementsprechend zu HttpUrlConnection wird.

            // Step 4: Set the request method to GET and setRequestProperty -> Übergabeparameter müssen exakt diese sein für Zugriff auf den WebServer
            connection.setRequestProperty("Authorization", TOKEN);
            connection.setRequestMethod("GET"); // "GET" is a Constructor for the HttpURLConnection

            // Step 5: Get the HTTP response code
            int responseCode = connection.getResponseCode(); // Output: 200 for successful , ELSE: 401 sonst
            LOGGER.log(Level.INFO,"ResonseCode: " + responseCode);

            // Step 6: Read and display response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Speichert den InputStream

            String line;
            StringBuilder responseContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }   // Creates "json String"

            //LogMain.getLogger().log(Level.INFO, "JSON data successfully received from " + link );
            LOGGER.log(Level.INFO, "JSON Data successfully retrieved from: " + link);

            return responseContent.toString();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving JSON data from " + link, e);
            throw new RuntimeException(e);
        }
    }

}
