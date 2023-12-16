package Rest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.lang.*;

public class JSONDerulo {
    // Step 1: Define the URL
    private static final String ENDPOINT_URL = "https://dronesim.facets-labs.com/api/drones/?format=json";
    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public static void main(String[] args) {
        connector(ENDPOINT_URL);
    }

    public static void connector(String link) {
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
            System.out.println("responseCode: " + responseCode);

            // Step 6: Read and display response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Speichert den InputStream

            String line;
            StringBuilder responseContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }   // Speichert html "quellcode" in responseContent

            JSONDeruloTransformer(responseContent.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void JSONDeruloTransformer(String toTransform) {
        JSONObject wholeHtml = new JSONObject(toTransform);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");
        System.out.println(wholeHtml.toString()); //Hat Next Pointer
        // System.out.println(jsonArray.toString()); //Keinen Next Pointer

        final int len = jsonArray.length();

        Drone[] drone = new Drone[len];
        DroneType[] dronetypes = new DroneType[len];

        for (int i = 0; i < len; i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            drone[i] = new Drone(o.getString("carriage_type"), o.getString("serialnumber"), o.getString("created"), o.getInt("carriage_weight"), o.getInt("id"), o.getString("dronetype"));
            //dronetypes[i] = new DroneType(drone[i].droneTypePointer, i);
            //connector(drone[i].droneTypePointer);
            drone[i].printDrone(); // Zur Kontrolle

        }

        if (wholeHtml.has("next")) {
            if (wholeHtml.isNull("next") == false) {
                String nextPointer = wholeHtml.getString("next");
                connector(nextPointer);
            }

        }
    }
}


