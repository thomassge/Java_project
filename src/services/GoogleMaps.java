///**
// * This class is responsible for generating a visual representation of drone locations using Google Maps.
// * It fetches drone data, including their dynamics and displays their positions on a map.
// */
//package services;
//
//import data.*;
//import processing.*;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//import java.util.LinkedList;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
////import com.teamdev.jxbrowser.chromium.Browser;
////import com.teamdev.jxbrowser.chromium.BrowserFactory;
//
//
//public class GoogleMaps {
//
//    private static JSONDeruloHelper jsonDerulo = new JSONDeruloHelper();
//    private static LinkedList<Drone> drones = new LinkedList<Drone>();
//    private static LinkedList<DroneType> droneTypes = new LinkedList<DroneType>();
//
//    /**
//     * Main method for the GoogleMaps class. It retrieves drone data, processes it and displays the drones' postions
//     * on a map using a static image from the Google Maps API.
//     *
//     * @param args Command line arguments (not used in this application.)
//     * @throws IOException if there is an error in processing the map or drone data.
//     */
//    public static void main(String[] args) throws IOException {
//
//
////        drones = jsonDerulo.getDrones();
////        droneTypes = jsonDerulo.getDroneTypes();
////        jsonDerulo.droneTypeToDroneLinker(droneTypes, drones);
////        jsonDerulo.addDroneDynamicsData(drones);
//        System.out.println(drones.get(0).getDroneDynamicsArrayList().get(0).getLatitude());
//
//        JFrame test = new JFrame("Google Maps");
//
//        try {
//            //String imageUrl = "https://api.tomtom.com/map/1/staticimage?key=wGzEG9NQKwuVBSDV8Wzy6BemGZp20KuR&zoom=15&center=8.678137129," +
//                    //"50.107668121&format=jpg&layer=basic&style=main&width=512&height=512&view=Unified&language=de-DE";
//            String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?&size=1280x1280&zoom=18&scale=2&key=_;)_&maptype=roadmap&";
//            String markers = "markers=color:red%7Clabel:D%7C";
//            imageUrl = imageUrl + markers  + drones.get(12).getDroneDynamicsArrayList().get(169).getLatitude() + "," + drones.get(12).getDroneDynamicsArrayList().get(169).getLongitude() + "&" + markers + drones.get(12).getDroneDynamicsArrayList().get(1352).getLatitude() + "," + drones.get(12).getDroneDynamicsArrayList().get(1352).getLongitude() + "&";
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
//
//    }
//}