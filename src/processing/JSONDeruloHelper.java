/**
 * This is a helper class that contains methods to retrieve, save and re-fetch data from
 * the webserver if needed.
 */

package processing;

import data.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.lang.*;


public class JSONDeruloHelper {

    //public static JSONDeruloHelper helper = new JSONDeruloHelper();
    protected Drone droneObject = new Drone();
    protected DroneType droneTypesObject = new DroneType();
    protected DroneDynamics droneDynamicsObject = new DroneDynamics();

    private static final String DRONES_URL = "https://dronesim.facets-labs.com/api/drones/";
    private static final String DRONETYPES_URL = "https://dronesim.facets-labs.com/api/dronetypes/";
    private static final String DRONEDYNAMICS_URL = "https://dronesim.facets-labs.com/api/dronedynamics/";
    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    //might be unnecessary since the invention of local- and serverDronecount
    private int numberOfDrones;
    private int numberOfDroneTypes;
    private int numberOfDroneDynamics;

    public int getNumberOfDrones() {
        return numberOfDrones;
    }
    public int getNumberOfDroneTypes() {
        return numberOfDroneTypes;
    }
    public int getNumberOfDroneDynamics() {
        return numberOfDroneDynamics;
    }

    public static String getDronesUrl() {
        return DRONES_URL;
    }
    public static String getDroneTypesUrl() {
        return DRONETYPES_URL;
    }
    public static String getDroneDynamicsUrl() {
        return DRONEDYNAMICS_URL;
    }

    public LinkedList<Drone> getData() throws IOException {
        //Creating Drone objects and filling them with data
        LinkedList<Drone> drones = getDrones();

        //Creating DroneType objects and link them our Drone objects
        LinkedList <DroneType> droneTypes = getDroneTypes();
        droneTypeToDroneLinker(droneTypes, drones);

        //Add drone dynamics objects to our Drone objects
        addDroneDynamicsData(drones);

        return drones;
    }

    //Creating Drone Objects with Data from "Drones" Database
    public LinkedList<Drone> getDrones() {
        droneObject.saveAsFile(); //checks for refresh when initializing dronedata for the first time

        String myJson;
        try {
            myJson = new Scanner(new File("drones.json")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        LinkedList<Drone> drones = new LinkedList<Drone>();
        individualDroneJsonToObject(myJson, drones);

        return drones;
    }

    public LinkedList<DroneType> getDroneTypes() {
        droneTypesObject.saveAsFile();

        String myJson;
        try {
            myJson = new Scanner(new File("dronetypes.json")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
        droneTypeJsonToObject(myJson, droneTypes);

        return droneTypes;
    }

    /**
     *     Creates DroneDynamic Objects off the File and stores them in the appropriate Drone via LinkedList
     */
    public void addDroneDynamicsData(LinkedList<Drone> drones) throws IOException {
        droneDynamicsObject.saveAsFile();

        String myJson;
        myJson = new Scanner(new File("dronedynamics.json")).useDelimiter("\\Z").next();

        JSONObject myJsonObject = new JSONObject(myJson);
        JSONArray jsonArray = myJsonObject.getJSONArray("results");

        // code insists that number of drones >= number of drones that have dronedynamics data (probably fine since every droneD entry has a drone url)
        for (int z = 0; z < numberOfDrones; z++) {
            if (drones.get(z).droneDynamicsArrayList == null) {
                drones.get(z).setDroneDynamicsArrayList(new ArrayList<DroneDynamics>());
            }
            String toCheck = "http://dronesim.facets-labs.com/api/drones/" + drones.get(z).getId() + "/";

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject o = jsonArray.getJSONObject(j);

                if (o.getString("drone").equals(toCheck)) {
                    drones.get(z).droneDynamicsArrayList.add(new DroneDynamics(
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
                            o.getString("status")
                    ));
                }
            }
        }
        numberOfDroneDynamics = numberOfDroneDynamics + jsonArray.length(); // Update numberOfDroneDynamics if refresh() creates new DroneDynamics data
    }

    //Connects to the webserver and gets a JSON String according to what url is provided in the Parameter
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
            System.out.println("responseCode for jsonCreator: " + responseCode);

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
    }

    //Creates Drone Objects off the JSON, which is provided as parameter
    public void individualDroneJsonToObject(String jsonString, LinkedList<Drone> drones) {
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            drones.add(new Drone(
                    o.getString("carriage_type"),
                    o.getString("serialnumber"),
                    o.getString("created"),
                    o.getInt("carriage_weight"),
                    o.getInt("id"),
                    o.getString("dronetype")
            ));
        }
        numberOfDrones = numberOfDrones + jsonArray.length(); // update numberOfDrones if refresh() created new Drone objects
    }

    //Creates DroneType Objects off the JSON, which is provided in the Parameter
    public void droneTypeJsonToObject(String jsonString, LinkedList<DroneType> droneTypes) {

        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            droneTypes.add(new DroneType(
                    o.getInt("id"),
                    o.getString("manufacturer"),
                    o.getString("typename"),
                    o.getInt("weight"),
                    o.getInt("max_speed"),
                    o.getInt("battery_capacity"),
                    o.getInt("control_range"),
                    o.getInt("max_carriage")
            ));
        }
        numberOfDroneTypes = numberOfDroneTypes + jsonArray.length(); // update numberOfDroneTypes if refresh() created new Drone objects
    }

    public void droneTypeToDroneLinker(LinkedList<DroneType> droneTypes, LinkedList<Drone> drones) {
        for(Drone droneObjectThatNeedsDroneTypeInformation : drones) {
            if(droneObjectThatNeedsDroneTypeInformation.getDroneTypeObject() == null) {

                for (DroneType droneType : droneTypes) {
                    if (droneObjectThatNeedsDroneTypeInformation.getExtractedDroneTypeID() == (droneType.getDroneTypeID())) {

                        droneObjectThatNeedsDroneTypeInformation.setDroneTypeObject(droneType);
                        break; //break added
                    }
                }
            }
            else { continue; }
        }
    }

    //Refresh database to re-fetch data
    public void refresh(LinkedList<Drone> drones, LinkedList<DroneType> droneTypes) throws IOException {

        if(drones.get(0).getServerCount() > getNumberOfDrones()) {
            String modifiedDroneURL = DRONES_URL + "?offset=" + getNumberOfDrones();
            String forCreatingDroneObjects = jsonCreator(modifiedDroneURL);
            individualDroneJsonToObject(forCreatingDroneObjects, drones);
            System.out.println("New Drones added");
        } else {
            System.out.println("No new Drone Information in the database");
        }

        if(droneTypes.get(0).getServerCount() > getNumberOfDroneTypes()) {
            String modifiedDroneTypeURL = DRONETYPES_URL + "?offset=" + getNumberOfDroneTypes();
            String forCreatingDroneTypeObjects = jsonCreator(modifiedDroneTypeURL);
            droneTypeJsonToObject(forCreatingDroneTypeObjects, droneTypes);
            droneTypeToDroneLinker(droneTypes, drones);
            System.out.println("New DroneTypes added");
        } else {
            System.out.println("No new DroneType Information in the database");
        }

        // this (offset)method works for new data that was appended to the tail of the database (json string),
        // but not if new data was inserted somewhere in the middle
        //problem with this method is, that if the data is being replaced like on 27.12.23 it might produce unsinn since the offset is not a valid abgrenzer yo
        if(droneDynamicsObject.getServerCount() > getNumberOfDroneDynamics()) {
            String modifiedDroneDynamicsURL = DRONEDYNAMICS_URL + "?offset=" + getNumberOfDroneDynamics();
            String forCreatingDroneDynamics = jsonCreator(modifiedDroneDynamicsURL);
            refreshDroneDynamics(drones, modifiedDroneDynamicsURL);
            System.out.println("New DroneDynamics added");

        } else {
            System.out.println("No new DroneDynamic Information in the database");
        }
    }
    public void refreshDroneDynamics(LinkedList<Drone> drones, String modifiedDroneDynamicsURL) {

        String myJson = jsonCreator(modifiedDroneDynamicsURL);

        JSONObject myJsonObject = new JSONObject(myJson);
        JSONArray jsonArray = myJsonObject.getJSONArray("results");

        for (int z = 0; z < numberOfDrones; z++) { // code insists that number of drones >= number of drones that have dronedynamics
            if (drones.get(z).droneDynamicsArrayList == null) {
                drones.get(z).droneDynamicsArrayList = new ArrayList<DroneDynamics>();
            }
            String toCheck = "http://dronesim.facets-labs.com/api/drones/" + drones.get(z).getId() + "/";

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject o = jsonArray.getJSONObject(j);

                if (o.getString("drone").equals(toCheck)) {
                    drones.get(z).droneDynamicsArrayList.add(new DroneDynamics(
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
                            o.getString("status")
                    ));
                }
            }
        }
        numberOfDroneDynamics = numberOfDroneDynamics + jsonArray.length(); // Update numberOfDroneDynamics if refresh() creates new DroneDynamics data
    }

    //Create a file off our Objects to "save current database state" and reload it the next time the application launches
    /*public static String fileCreatorOffObjects(LinkedList<Drone> drones) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json1 = mapper.writeValueAsString(drones.get(0).droneDynamicsArrayList);

        try (PrintWriter out = new PrintWriter("test.json")) {
            System.out.println("done");
            out.println(json1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return json1;
    }*/

    //Saves DroneDynamics JSON locally to reduce application launch speed from the second time we run the program
    /*public static void saveDroneDynamicsDataInFile() throws FileNotFoundException {
        int limit = DroneDynamics.getCount();

        if(!(helper.droneDynamicsObject.checkForNewData(limit))) {
            System.out.println("No New DroneDynamics Data to fetch from");
            return;
        }

        System.out.println("DroneDynamics Count: " + limit);
        String forCreatingDroneObjects = jsonCreator(DRONEDYNAMICS_URL + "?limit=" + limit);

        System.out.println("Saving DroneDynamic Data from Webserver in file ...");

            try (PrintWriter out = new PrintWriter("dronedynamics.json")) {
                out.println(forCreatingDroneObjects);
            }
    }*/
}