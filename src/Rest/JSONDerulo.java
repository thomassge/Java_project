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
import java.util.LinkedList;

public class JSONDerulo {

    protected static final String ENDPOINT_URL = "http://dronesim.facets-labs.com/api/drones/?limit=20";
    protected static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public static void main(String[] args) {
        //Creating Drone Objects with Data from "Drones" Database
        Drone[] drones = new Drone[Drone.numberOfDrones];
        String forCreatingDrones = jsonCreator(ENDPOINT_URL);
        individualDroneJsonToObject(drones, forCreatingDrones);

        //Adding DroneType information to the Drone Objects via given pointer
        addDroneTypeData(drones);

        //Adding DroneDynamics information to the Drone Objects
        System.out.println("Saving DynamicData from Webserver in memory ...");
        addDroneDynamicsData(drones);

        //Testing the Data of Drone Objects which include everything
        drones[0].printAllDroneInformation(drones[0]);
    }

    public static void addDroneDynamicsData(Drone[] drones) {
        int i = 0;
        for (Drone droneObject : drones) {
            String droneDynamicsURL = "https://dronesim.facets-labs.com/api/" + drones[i].id + "/dynamics/?limit=1500&offset=0";
            String jsonDroneDynamicsData = jsonCreator(droneDynamicsURL);
            droneDynamicsJsonToObject(drones[i], jsonDroneDynamicsData);
            i++;
        }
    }

    public static void addDroneTypeData(Drone[] drones) {
        int i = 0;
        for (Drone droneObject : drones) {
            String jsonDroneTypeData = jsonCreatorForDroneType(drones[i].droneTypePointer);
            droneTypeJsonToObject(drones[i], jsonDroneTypeData);
            i++;
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

            return responseContent.toString();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } // returns json String

    public static String jsonCreatorForDroneType(String pointer) {
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

            //return jsonToObject(object, responseContent.toString());
            return responseContent.toString();

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } // DroneType Data needs a different approach for creating a JSON String because of the usage of pointers

    protected static void individualDroneJsonToObject(Drone[] objects, String toTransform) {
        JSONObject wholeHtml = new JSONObject(toTransform);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        int j = jsonArray.length();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            objects[i] = new Drone(o.getString("carriage_type"), o.getString("serialnumber"), o.getString("created"), o.getInt("carriage_weight"), o.getInt("id"), o.getString("dronetype"));
            //objects[i].printDrone(); // Zur Kontrolle
        }
    }

    public static void droneTypeJsonToObject(Drone object, String jsonString) {
        JSONObject wholeHtml = new JSONObject(jsonString);
        object.droneTypeID = wholeHtml.getInt("id");
        object.manufacturer = wholeHtml.getString("manufacturer");
        object.typename = wholeHtml.getString("typename");
        object.weight = wholeHtml.getInt("weight");
        object.maximumSpeed = wholeHtml.getInt("max_speed");
        object.batteryCapacity = wholeHtml.getInt("battery_capacity");
        object.controlRange = wholeHtml.getInt("control_range");
        object.maximumCarriage = wholeHtml.getInt("max_carriage");
    }

    public static void droneDynamicsJsonToObject(Drone object, String toTransform) {
        JSONObject wholeHtml = new JSONObject(toTransform);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        int j = jsonArray.length();
        object.droneDynamicsLinkedList = new LinkedList<DroneDynamics>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            object.droneDynamicsLinkedList.add(new DroneDynamics(o.getString("drone"), o.getString("timestamp"), o.getInt("speed"), o.getFloat("align_roll"), o.getFloat("align_pitch"), o.getFloat("align_yaw"), o.getDouble("longitude"), o.getDouble("latitude"), o.getInt("battery_status"), o.getString("last_seen"), o.getString("status")));
        }
    }
}


