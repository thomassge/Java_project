//package data;
//
//import org.json.JSONObject;
//import processing.JSONDeruloHelper;
//import processing.Streamable;
//import util.WebserverDataFetcher;
//
//import java.io.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public abstract class AbstractDroneOperations implements Streamable {
//
////    private static int localCount;
////    private static int serverCount;
//    private int memoryCount;
//
//    private static final Logger logger = Logger.getLogger(Drone.class.getName());
//
//
//
//
//
//
//
//
//
//    public static void checkForNewData(String filename, String URL, int localCount, int serverCount) {
//        checkFile(filename);
//        localCount = checkLocalCount(filename);
//        serverCount = checkServerCount(URL);
//
//        if(serverCount == 0) {
//            logger.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
//            //TODO: Own Exception
//        }
//        if (localCount == serverCount) {
//            logger.log(Level.INFO, "local- and serverDroneCount identical.");
//        }
//        else if(localCount < serverCount) {
//            saveAsFile(URL, serverCount, filename);
//        }
//        else {
//            logger.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
//        }
//    };
//
//    public abstract void refresh();
//}
