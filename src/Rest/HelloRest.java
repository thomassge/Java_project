package Rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HelloRest {

    private static final String USER_AGENT = "MOzilla FIrefox Awesome version";
    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public static void main(String[] args) {

    }

    public static void connector(String link, Drone drone) {
        System.out.println("Test started...");

        URL url;
        try {
            url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", TOKEN);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = connection.getResponseCode();

            System.out.println("Response Code " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString()); // Process your response

            // Abfrage welche Datenbank
            if (drone instanceof IndividualDrone) {
                individualDroneOutput(response.toString());
            }
            else if (drone instanceof DroneTypes) {
                droneTypesOutput(response.toString());
            }
            else if (drone instanceof DroneDynamics) {
                droneDynamicsOutput(response.toString());
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("General IO Exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void individualDroneOutput(String input) {
        JSONObject wholeFile = new JSONObject(input);
        JSONArray jsonFile = wholeFile.getJSONArray("results");
        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if(o.has("carriage_type") && o.has("carriage_weight")){
                String a = o.getString("carriage_type");
                int b = o.getInt("carriage_weight");
                int id = o.getInt("id");
                String serialnumber = o.getString("serialnumber");
                String created = o.getString("created");
                System.out.println("\n");
                System.out.println("Drone " + id + ": carriage type " + a + " (weight: " + b + "g)");
                System.out.println("Serialnumber: " + serialnumber);
                System.out.println("Created: " + created);
            }
        }

    }

    public static void droneTypesOutput(String input) {
        JSONObject wholeFile = new JSONObject(input);
        JSONArray jsonFile = wholeFile.getJSONArray("results");
        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (o.has("id") && o.has("manufacturer")) {
                int id = o.getInt("id");
                String manufacturer = o.getString("manufacturer");
                String typename = o.getString("typename");
                int weight = o.getInt("weight");
                int max_speed = o.getInt("max_speed");
                int battery_capacity = o.getInt("battery_capacity");
                int control_range = o.getInt("control_range");
                int max_carriage = o.getInt("max_carriage");

                System.out.println("\n");
                System.out.println("ID: " + id);
                System.out.println("Manufacturer: " + manufacturer);
                System.out.println("Typename: " + typename);
                System.out.println("Weight: " + weight);
                System.out.println("Maximum Speed: " + max_speed);
                System.out.println("Battery Capacity: " + battery_capacity);
                System.out.println("Control Range: " + control_range);
                System.out.println("Maximum Carriage: " + max_carriage);

            }
        }
    }

    public static void droneDynamicsOutput(String input) {
        JSONObject wholeFile = new JSONObject(input);
        JSONArray jsonFile = wholeFile.getJSONArray("results");
        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if(o.has("timestamp") && o.has("speed")) {
                String timestamp = o.getString("timestamp");
                int speed = o.getInt("speed");
                double align_roll = o.getDouble("align_roll");
                double align_pitch = o.getDouble("align_pitch");
                double align_yaw = o.getDouble("align_yaw");
                double longitude = o.getDouble("longitude");
                //double latitude = o.getDouble("latitude");
                int battery_status = o.getInt("battery_status");
                String last_seen = o.getString("last_seen");
                String status = o.getString("status");

                System.out.println("\n");
                //System.out.println("o :" + o);
                System.out.println("Timestamp: " + timestamp );
                System.out.println("Speed: " + speed );
                System.out.println("Align_roll" + align_roll);
                System.out.println("Align_pitch" + align_pitch);
                System.out.println("Longitude: " + longitude);
                //System.out.println("Latitude: " + latitude);
                System.out.println("Battery-Status: " + battery_status);
                System.out.println("Last seen: " + last_seen);
                System.out.println("Status: " + status);

            }
        }
    }
    public static String formatJson(String input) {
        final int indentSpaces = 4;
        Object json = new JSONTokener(input).nextValue();

        if (json instanceof JSONObject) {
            JSONObject o = (JSONObject) json;

            return o.toString(indentSpaces);
        } else if (json instanceof JSONArray) {
            return ((JSONArray) json).toString(indentSpaces);
        } else {
            throw new IllegalArgumentException("Input string is not a valid JSON");
        }
    }
}