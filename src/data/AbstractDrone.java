package data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDrone {
    protected int localObjectCount;
    protected int serverObjectCount;
    protected int memoryObjectCount;

    private static final Logger logger = Logger.getLogger(AbstractDrone.class.getName());

    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public int getLocalObjectCount() {
        return this.localObjectCount;
    }

    public int getMemoryObjectCount() {
        return this.memoryObjectCount;
    }

    public int getServerObjectCount() {
        return this.serverObjectCount;
    }

    //INITIALISE
//    abstract ArrayList<Drone> initialise(String str);

    /**
     * Creates a JSON string from the provided URL.
     *
     * @param link The URL from which to fetch the JSON data.
     * @return A JSON string representation of the data.
     */
    protected String jsonCreator(String link) {
        try {
            // Step 2: Create a URL object
            URL url = new URL(link);

            // Step 3: Open a connection
            HttpURLConnection connection; // Erstellen einer leeren Variable vom Typen HttpUrlConnection;
            connection = (HttpURLConnection) url.openConnection(); // Der Rückgabewert von openConnection ist eig. 'URLConnection', deshalb das Typecasting, da wir speziell mit HTTP arbeiten und der Rückgabewert von openConnection dementsprechend zu HttpUrlConnection wird.
            //InputStream inputStream = connection.inputStream()?

            // Step 4: Set the request method to GET and setRequestProperty -> Übergabeparameter müssen exakt diese sein für Zugriff auf den WebServer
            connection.setRequestProperty("Authorization", TOKEN);
            connection.setRequestMethod("GET"); //Der Übergabeparameter "GET" ist ein Konstruktor für die HttpURLConnection

            // Step 5: Get the HTTP response code
            int responseCode = connection.getResponseCode(); // Gibt 200 bei eienr successful request zurück, 401 sonst
            logger.log(Level.INFO,"responseCode for jsonCreator: " + responseCode);

            // Step 6: Read and display response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Speichert den InputStream

            String line;
            StringBuilder responseContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }   // Erschafft den "json String"

            logger.log(Level.INFO, "JSON data successfully received from " + link );

            return responseContent.toString();

        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Error retrieving JSON data from " + link, e);
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            logger.log(Level.SEVERE, "Error retrieving JSON data from " + link, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error retrieving JSON data from " + link, e);
            throw new RuntimeException(e);
        }
    }

    protected String reader(String filename) {

        StringBuilder responseContent;
        try {
            String data;
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            responseContent = new StringBuilder();
            while ((data = reader.readLine()) != null) {
                responseContent.append(data);
            }   // Erschafft den "json String"
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
            // TODO: IMPLEMENT FETCHING FROM SERVER
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseContent.toString();
    }

    public static void main(String[] args) {

    }
}
