package Rest;
import Rest.HelloRest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DroneDynamics extends Drone {
    static DroneDynamics obj = new DroneDynamics();
    private static final String DRONEDYNAMICS_URL = "https://dronesim.facets-labs.com/api/dronedynamics/?format=json";
    //private static final String USER_AGENT = "MOzilla FIrefox Awesome version";
    //private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    private static int id;
    private String timestamp;
    private String drone;
    private int speed;
    private double alignmentRoll;
    private double controlRange;
    private double alignmentYaw;
    private double longitude;
    private double latitude;
    private int batteryStatus;
    private String lastSeen;
    private DroneStatus status;

    enum DroneStatus {
        ON,
        OF,
        IS;
    }



    public static void main(String[] args) {

        HelloRest.connector(DRONEDYNAMICS_URL, obj);

    }
}
