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
    private static final int numberOfDrones = 20;

    public static void main(String[] args) {
        //pagination
        Drone[] drone = new Drone[numberOfDrones];
        String forCreatingDrones = jsonCreator(ENDPOINT_URL);
        jsonToDroneObject(drone, forCreatingDrones);


        DroneType[] dronetypes = new DroneType[numberOfDrones];
        for(int i = 0; i < numberOfDrones; i++) {
            //String forCreatingDroneTypes = jsonCreatorForDroneType();
            //jsonToObject();
            //dronetypes[i] = jsonCreatorForDroneType(dronetypes[i], drone[i].droneTypePointer);
            //System.out.println(drone[i].droneTypePointer);
            String forCreatingDroneTypes = jsonCreatorForDroneType(dronetypes[i], drone[i].droneTypePointer);
            jsonToObject(dronetypes[i], forCreatingDroneTypes);
            //dronetypes[i].printDroneType(); // Zur Kontrolle
        }


    }

    public static String jsonCreator(String link) {
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
            }   // Erschafft den "json String"

            //System.out.println("responseContent.toString() in jsonCreator Methode: " + responseContent.toString());
            //hier reicht theoretisch eine abfrage um eine funktion aufzurufen die dronetype json handled, kp wie daher neue connector Funktion.
            return responseContent.toString();
            //jsonToDroneObject(responseContent.toString()); In Main verschoben


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } // returns json String

    private static void jsonToDroneObject(Drone[] object, String toTransform) {
        JSONObject wholeHtml = new JSONObject(toTransform);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");
        System.out.println(wholeHtml.toString()); //Hat Next Pointer
        // System.out.println(jsonArray.toString()); //Keinen Next Pointer

        final int len = jsonArray.length();
        System.out.println(len);

        int j = len;
        for (int i = 0; i < len; i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            object[i] = new Drone(o.getString("carriage_type"), o.getString("serialnumber"), o.getString("created"), o.getInt("carriage_weight"), o.getInt("id"), o.getString("dronetype"));
            //jsonToObject(dronetypes[i], drone[i].droneTypePointer);
            //dronetypes[i] = new DroneType(drone[i].droneTypePointer, i);
            //connector(drone[i].droneTypePointer);
            //object[i].printDrone(); // Zur Kontrolle

            //jsonToDroneObject(object,jsonCreator(nextPointer));
        }

        // paginating through json limit
        String nextPointer = wholeHtml.getString("next");
        JSONObject additional = new JSONObject(jsonCreator(nextPointer));
        JSONArray jsonArray2 = additional.getJSONArray("results");

        int z = 0;
        if (wholeHtml.isNull("next") == false && ((j < numberOfDrones) == true)) { //evtl. noch abfrage ob next pointer überhaupt vorhanden ist
            while(z < jsonArray2.length()) {
                JSONObject o = jsonArray2.getJSONObject(z);
                object[j] = new Drone(o.getString("carriage_type"), o.getString("serialnumber"), o.getString("created"), o.getInt("carriage_weight"), o.getInt("id"), o.getString("dronetype"));
                j++;
                z++;
            }
        }
    }

    public static String jsonCreatorForDroneType(DroneType object, String pointer) {
        try {
            // Step 2: Create a URL object
            URL url = new URL(pointer);

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

            System.out.println("responseContent.toString() in connectorToDroneType Methode: " + responseContent.toString());

            //return jsonToObject(object, responseContent.toString());
            return responseContent.toString();

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void jsonToObject(DroneType object, String toTransform) {
        JSONObject wholeHtml = new JSONObject(toTransform);
        //System.out.println("wholeHtml.toString() in jsonToObject Methode: " + wholeHtml.toString());
        object = new DroneType(wholeHtml.getInt("id"), wholeHtml.getString("manufacturer"), wholeHtml.getString("typename"), wholeHtml.getInt("weight"), wholeHtml.getInt("max_speed"), wholeHtml.getInt("battery_capacity"), wholeHtml.getInt("control_range"), wholeHtml.getInt("max_carriage"));
    };


}


