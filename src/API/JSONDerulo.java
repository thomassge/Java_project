package API;
import Objects.Drone;
import Objects.DroneType;
import Objects.DroneDynamics;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.lang.*;
import java.util.LinkedList;

public class JSONDerulo {
    // IF DRONES OR DRONETYPES IN DATABASE EXCEED THE COUNT OF 1000, THIS HAS TO BE ADJUSTED
    protected static final String DRONES_URL = "https://dronesim.facets-labs.com/api/drones/?limit=1000";
    protected static final String DRONETYPES_URL = "http://dronesim.facets-labs.com/api/dronetypes/?limit=1000";
    protected static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";
    private static int numberOfDrones; // STATIC?
    private static int numberOfDroneTypes;
    private static int numberOfDroneDynamics;


    public static void main(String[] args) throws IOException {
        //Creating Drone Objects with Data from "Drones" Database
        String forCreatingDroneObjects = jsonCreator(DRONES_URL);
        Drone[] drones = individualDroneJsonToObject(forCreatingDroneObjects);
        System.out.println("numberOfDrones: " + JSONDerulo.numberOfDrones);
        //Adding DroneType information to the Drone Objects via given "pointer" or "DroneTypeURL"
        //addDroneTypeData(drones);
        String forCreatingDroneTypeObjects = jsonCreator(DRONETYPES_URL);
        DroneType[] droneTypes = droneTypeJsonToObject(forCreatingDroneTypeObjects);
        System.out.println("numberOfDrones: " + numberOfDrones);

        //Linking droneTypes to Drones
        Objects.Drone.droneTypeToDroneLinker(droneTypes, drones);
        System.out.println("numberOfDrones: " + numberOfDrones);

        //Adding DroneDynamics information to the Drone Objects
        System.out.println("Saving DroneDynamic Data from Webserver in memory ...");

        //saveDroneDynamicsDataInFile();
        addDroneDynamicsData(drones);

        //Print Drone Object Information
        //drones[0].printAllDroneInformation();
        //drones[0].printAllDroneInformation();
        //System.out.println(drones[0].droneDynamicsLinkedList.get(1).dronePointer);
    }

    public static void saveDroneDynamicsDataInFile() throws FileNotFoundException {
        String droneDynamicsURL = "https://dronesim.facets-labs.com/api/dronedynamics/?limit=28820";

        try (PrintWriter out = new PrintWriter("filename.json")) {
            out.println(jsonCreator(droneDynamicsURL));
        }
    }

    public static void addDroneDynamicsData(Drone[] object) throws IOException {
        String myJson = new Scanner(new File("filename.json")).useDelimiter("\\Z").next();
        JSONObject myJsonObject = new JSONObject(myJson);
        JSONArray jsonArray = myJsonObject.getJSONArray("results");

        numberOfDroneDynamics = jsonArray.length();
        //if (refresh == true) && numberOfDroneDynamics != theSame {addMoreDroneDynamicsData} ------------sehr clever Bro! -TL
        for (int z = 0; z < numberOfDrones; z++) { // code insists that number of drones = number of drones that have dronedynamics
            object[z].droneDynamicsLinkedList = new LinkedList<DroneDynamics>();
            String toCheck = "http://dronesim.facets-labs.com/api/drones/" + object[z].getId() + "/";

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject o = jsonArray.getJSONObject(j);

                if (o.getString("drone").equals(toCheck)) {
                    object[z].droneDynamicsLinkedList.add(new DroneDynamics(
                            o.getString("drone"),
                            o.getString("timestamp"),
                            o.getInt("speed"),
                            o.getFloat("align_roll"),
                            o.getFloat("align_pitch"),
                            o.getFloat("align_yaw"),
                            o.getDouble("longitude"),
                            o.getDouble("latitude"),
                            o.getInt("battery_status"),
                            o.getString("last_seen"),
                            o.getString("status"))
                    );
                }
            }
        }
    }

    /*public static void addDroneTypeData(Drone[] drones) {
        int i = 0;
        for (Drone droneObject : drones) {
            String jsonDroneTypeData = jsonCreatorForDroneType(drones[i].getDroneTypePointer());
            droneTypeJsonToObject(drones[i], jsonDroneTypeData);
            i++;
        }
    }*/

    /*public static void addDroneDynamicsData(Drone[] drones) {
        int i = 0;
        for (Drone droneObject : drones) {
            String droneDynamicsURL = "https://dronesim.facets-labs.com/api/" + drones[i].id + "/dynamics/?limit=1500&offset=0";
            String jsonDroneDynamicsData = jsonCreator(droneDynamicsURL);
            droneDynamicsJsonToObject(drones[i], jsonDroneDynamicsData);
            i++;
        }
    }*/

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

    public static String jsonCreatorForDroneType(String pointer) {  // might be irrelevant at this stage
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
    } // evtl. redundant, DroneType Data needs a different approach for creating a JSON String because of the usage of pointers

    protected static Drone[] individualDroneJsonToObject(String jsonString) {
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        numberOfDrones = jsonArray.length();
        Drone[] drones = new Drone[numberOfDrones];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            drones[i] = new Drone(
                    o.getString("carriage_type"),
                    o.getString("serialnumber"),
                    o.getString("created"),
                    o.getInt("carriage_weight"),
                    o.getInt("id"),
                    o.getString("dronetype")
            );
            //drones[i].printDrone(); // Zur Kontrolle
        }
        return drones;
    }

    public static DroneType[] droneTypeJsonToObject(String jsonString) {
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        numberOfDroneTypes = jsonArray.length();
        DroneType[] droneTypes = new DroneType[numberOfDroneTypes];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            droneTypes[i] = new DroneType(
                    o.getInt("id"),
                    o.getString("manufacturer"),
                    o.getString("typename"),
                    o.getInt("weight"),
                    o.getInt("max_speed"),
                    o.getInt("battery_capacity"),
                    o.getInt("control_range"),
                    o.getInt("max_carriage")
            );
            //objects[i].printDrone(); // Zur Kontrolle
        }
        return droneTypes;
    }
}