/**
 * This class is responsible for generating a visual representation of drone locations using Google Maps.
 * It fetches drone data, including their dynamics and displays their positions on a map.
 */
package services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleMaps {
    private static final Logger LOGGER = Logger.getLogger(GoogleMaps.class.getName());

    private final String myKey = System.getenv("jsonDerulo");
    private final String markers = "markers=color:red%7Clabel:D%7C";
    private String googleEndpoint = "https://maps.googleapis.com/maps/api/staticmap?&size=400x400&zoom=16&scale=2&key=" + myKey + "&maptype=roadmap&";

    public String createPicture(double latitude, double longitude){
        try {
            googleEndpoint = googleEndpoint + markers  + latitude + "," + longitude;
            String destinationFile = "image.jpg";
            URL url = new URL(googleEndpoint);

            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
            LOGGER.log(Level.INFO,"Picture created...");

            return destinationFile;
        } catch (MalformedURLException e) {
            LOGGER.info("Wrong URL");
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            LOGGER.info("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}