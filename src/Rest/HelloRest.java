package Rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HelloRest {

    private static final String USER_AGENT = "Mozilla Firefox Awesome version";

    private static final String ENDPOINT_URL1 = "https://dronesim.facets-labs.com/api/drones/?format=json";
    private static final String ENDPOINT_URL2 = "https://dronesim.facets-labs.com/api/dronetypes/?format=json";
    private static final String ENDPOINT_URL3 = "https://dronesim.facets-labs.com/api/dronedynamics/?format=json";

    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public static void out_drones(String input) {
        JSONObject wholeFile = new JSONObject(input);
        JSONArray jsonFile = wholeFile.getJSONArray("results");
        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject obj = jsonFile.getJSONObject(i);
            if(obj.has("carriage_type") && obj.has("carriage_weight")){
                String carriageType = obj.getString("carriage_type");
                int carriageWeight = obj.getInt("carriage_weight");
                int id = obj.getInt("id");
                String serialnum = obj.getString("serialnumber");
                String created = obj.getString("created");
                System.out.println("\n");
                System.out.println("Drone " + id );
                System.out.println("Serial number: " + serialnum);
                System.out.println("Carriage Type: " + carriageType);
                System.out.println("Carriage Weight: " + carriageWeight);
                System.out.println("Created: " + created);
            }
            else{
                System.out.println("Wrong dataset!");
            }
        }
    }

    /*public static void testDroneTypes(String input) {
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
    }*/

    /*public static void testDroneDynamics(String input) {
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
                double latitude = o.getDouble("latitude");
                int battery_status = o.getInt("battery_status");
                String last_seen = o.getString("last_seen");
                String status = o.getString("status");
                System.out.println("\n");
                System.out.println("Timestamp: " + timestamp );
                System.out.println("Speed: " + speed );
                System.out.println("Align_roll" + align_roll);
                System.out.println("Align_pitch" + align_pitch);
                System.out.println("Longitude: " + longitude);
                System.out.println("Latitude: " + latitude);
                System.out.println("Battery-Status: " + battery_status);
                System.out.println("Last seen: " + last_seen);
                System.out.println("Status: " + status);

            }
        }
    }*/
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