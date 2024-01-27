/**
 * This class is responsible for generating a visual representation of drone locations using Google Maps.
 * It fetches drone data, including their dynamics and displays their positions on a map.
 */
package services;

import data.*;
import processing.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



public class GoogleMaps {
    private static final Logger LOGGER = Logger.getLogger(GoogleMaps.class.getName());

    private final String myKey = System.getenv("jsonDerulo");

    private String googleEndpoint = "https://maps.googleapis.com/maps/api/staticmap?&size=400x400&zoom=16&scale=2&key=" + myKey + "&maptype=roadmap&";

    private final String markers = "markers=color:red%7Clabel:D%7C";

    /**
     * Main method for the GoogleMaps class. It retrieves drone data, processes it and displays the drones' postions
     * on a map using a static image from the Google Maps API.
     *
     * @param args Command line arguments (not used in this application.)
     * @throws IOException if there is an error in processing the map or drone data.
     */
//    public static void main(String[] args) throws IOException {
//        LOGGER.info("Starting Google Maps main...");
//
//        DataFactory factory = new DataFactory();
//        ArrayList<DataStorage> data = factory.getDataStorage();
//
//        String myKey = System.getenv("jsonDerulo");
//
//        JFrame test = new JFrame("Google Maps");
//        try {
//            String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?&size=400x400&zoom=16&scale=2&key=" + myKey + "&maptype=roadmap&";
//            imageUrl = imageUrl + markers  + data.get(0).getDroneDynamicsList().get(0).getLatitude() + "," + data.get(0).getDroneDynamicsList().get(0).getLongitude();
//
//
//            String path = "path=color:0x0000ff|weight:5";
////            int i = 0;
////            double smallest = drones.get(12).droneDynamicsArrayList.getFirst().getLongitude();
////            double biggest = 0;
////            for(DroneDynamics object : drones.get(12).droneDynamicsArrayList) {
////                if(drones.get(12).droneDynamicsArrayList.get(i).getLongitude() < smallest) {
////                    smallest = drones.get(12).droneDynamicsArrayList.get(i).getLongitude();
////                    System.out.println("i for smallest: " + i);
////                }
////                if(drones.get(12).droneDynamicsArrayList.get(i).getLongitude() > biggest) {
////                    biggest = drones.get(12).droneDynamicsArrayList.get(i).getLongitude();
////                    System.out.println("i for biggest: " + i);
////                }
////                i++;
////            }
//
//            System.out.println("imageURL: " + imageUrl);
//            String destinationFile = "image.jpg";
//            String str = destinationFile;
//            URL url = new URL(imageUrl);
//            InputStream is = url.openStream();
//            OutputStream os = new FileOutputStream(destinationFile);
//
//            byte[] b = new byte[2048];
//            int length;
//
//            while ((length = is.read(b)) != -1) {
//                os.write(b, 0, length);
//            }
//
//            is.close();
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        test.add(new JLabel(new ImageIcon((new ImageIcon("image.jpg")).getImage().getScaledInstance(630, 600,
//                java.awt.Image.SCALE_SMOOTH))));
//
//        test.setVisible(true);
//        test.pack();

 //   }
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