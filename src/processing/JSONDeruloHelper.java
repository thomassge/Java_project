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
import java.util.logging.Level;
import java.util.logging.Logger;


public class JSONDeruloHelper {
    private static final Logger logger = Logger.getLogger(JSONDeruloHelper.class.getName());


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

    /**
     * Fetches and processes drone data.
     * It links drone types to drones and adds dynamics data to each drone.
     *
     * @return A LinkedList of Drone objects with complete data.
     * @throws IOException if there is an error in fetching or processing the data.
     */
    public LinkedList<Drone> getData() throws IOException {
        logger.log(Level.INFO, "Data is pulled...");

        //Creating Drone objects and filling them with data
        LinkedList<Drone> drones = getDrones();

        //Creating DroneType objects and link them our Drone objects
        LinkedList <DroneType> droneTypes = getDroneTypes();
        logger.log(Level.INFO, "Data successfully retrieved. Data is linked...");
        droneTypeToDroneLinker(droneTypes, drones);

        //Add drone dynamics objects to our Drone objects
        addDroneDynamicsData(drones);

        logger.log(Level.INFO, "Data link completed.");

        return drones;
    }


    //Creating Drone Objects with Data from "Drones" Database

    /**
     * Fetches drone data from a JSON file and converts it into Drone objects.
     *
     * @return A LinkedList of Drone objects.
     * @throws FileNotFoundException if the JSON file is not found.
     */
    public LinkedList<Drone> getDrones() throws FileNotFoundException {
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

    /**
     * Fetches drone type data and converts it into DroneType objects.
     *
     * @return A LinkedList of DroneType objects.
     */
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
     * Adds drone dynamics data to the provided list of drones.
     *
     * @param drone The list of drones to which the dynamics data will be added.
     * @throws IOException if there is an error in fetching or processing the data.
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

    //Creates Drone Objects off the JSON, which is provided as parameter

    /**
     * Converts individual drone data from JSON to Drone objects.
     *
     * @param jsonString The JSON string containing drone data.
     * @param drones The list where Drone objects will be added.
     */
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

    /**
     * Converts drone type data from JSON to DroneType objects.
     *
     * @param jsonString The JSON string containing drone type data.
     * @param droneTypes The list where DroneType objects will be added.
     */
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

    /**
     * Links drone to drones in the provided lists.
     *
     * @param droneTypes The list of DroneType objects.
     * @param drones The list of Drone objects.
     */
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

    /**
     * Refreshes the data by re-fetching from the webserver and updating the lists.
     *
     * @param drones The list of Drone objects to be refreshed.
     * @param droneTypes The list of DroneType objects to be refreshed.
     * @throws IOException if there is an error during data refresh.
     */
    public void refresh(LinkedList<Drone> drones, LinkedList<DroneType> droneTypes) throws IOException {
    try {
        if (drones.get(0).getServerCount() > getNumberOfDrones()) {
            String modifiedDroneURL = DRONES_URL + "?offset=" + getNumberOfDrones();
            String forCreatingDroneObjects = jsonCreator(modifiedDroneURL);
            individualDroneJsonToObject(forCreatingDroneObjects, drones);
            logger.log(Level.INFO,"New Drones added");
        } else {
            logger.log(Level.INFO,"No new Drone Information in the database");
        }

        if (droneTypes.get(0).getServerCount() > getNumberOfDroneTypes()) {
            String modifiedDroneTypeURL = DRONETYPES_URL + "?offset=" + getNumberOfDroneTypes();
            String forCreatingDroneTypeObjects = jsonCreator(modifiedDroneTypeURL);
            droneTypeJsonToObject(forCreatingDroneTypeObjects, droneTypes);
            droneTypeToDroneLinker(droneTypes, drones);
            logger.log(Level.INFO,"New DroneTypes added");
        } else {
            logger.log(Level.INFO,"No new DroneType Information in the database");
        }

        // this (offset)method works for new data that was appended to the tail of the database (json string),
        // but not if new data was inserted somewhere in the middle
        //problem with this method is, that if the data is being replaced like on 27.12.23 it might produce unsinn since the offset is not a valid abgrenzer yo
        if (droneDynamicsObject.getServerCount() > getNumberOfDroneDynamics()) {
            String modifiedDroneDynamicsURL = DRONEDYNAMICS_URL + "?offset=" + getNumberOfDroneDynamics();
            String forCreatingDroneDynamics = jsonCreator(modifiedDroneDynamicsURL);
            refreshDroneDynamics(drones, modifiedDroneDynamicsURL);
            logger.log(Level.INFO,"New DroneDynamics added");

        } else {
            logger.log(Level.INFO,"No new DroneDynamic Information in the database");
        }
        logger.log(Level.INFO, "Data successfully updated.");
    }   catch (Exception e) {
        logger.log(Level.SEVERE, "Fehler beim Aktualisieren der Daten.", e);
        throw new RuntimeException(e);
    }
    }

    /**
     * Refreshes the list of drones with new drone dynamics data fetching from the specified URL.
     * This method updates the drone dynamics data for each drone in the list if new data is available.
     *
     * @param drones The list of drones to update with new drone dynamics data.
     * @param modifiedDroneDynamicsURL The URL to fetch the latest drone dynamics data.
     */
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
            logger.log(Level.INFO,"done");
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
            logger.log(Level.INFO,"No New DroneDynamics Data to fetch from");
            return;
        }

        logger.log(Level.INFO,"DroneDynamics Count: " + limit);
        String forCreatingDroneObjects = jsonCreator(DRONEDYNAMICS_URL + "?limit=" + limit);

        logger.log(Level.INFO,"Saving DroneDynamic Data from Webserver in file ...");

            try (PrintWriter out = new PrintWriter("dronedynamics.json")) {
                out.println(forCreatingDroneObjects);
            }
    }*/
}