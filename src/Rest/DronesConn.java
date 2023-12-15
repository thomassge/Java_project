package Rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static Rest.HelloRest.out_drones;

public class DronesConn {
    //Connection to Database Drones
    private static final String ENDPOINT_URL_DRONES = "https://dronesim.facets-labs.com/api/drones/?format=json";
    //Token for Access
    private static final String TOKEN = "Token a3b2258a368b90330410da51a8937de91ada6f33";

    public static void main(String[] args) {
        System.out.println("Query starts...");

        URL url;
        try {
            url = new URL(ENDPOINT_URL_DRONES);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Authorization", TOKEN);
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            System.out.println("Response Code " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString()); // Process your response
            //test(response.toString());
            out_drones(response.toString());
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("General IO Exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    }


