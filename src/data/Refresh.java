package data;

import gui.DroneMenu;
import org.json.JSONObject;
import util.WebserverDataFetcher;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Refresh implements Saveable {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());
    static boolean isDronesNew;
    static boolean isDroneTypesNew;
    static boolean isDroneDynamicsNew;

    public Refresh() {
        LOGGER.info("called Refresh constructor");
        updateCount();
        isDronesNew = isNewDataAvailable(Drone.getFilename(), Drone.getUrl(), Drone.getLocalCount(), Drone.getServerCount());
        isDroneTypesNew = isNewDataAvailable(DroneType.getFilename(), DroneType.getUrl(), DroneType.getLocalCount(), DroneType.getServerCount());
        isDroneDynamicsNew = isNewDataAvailable(DroneDynamics.getFilename(), DroneDynamics.getUrl(), DroneDynamics.getLocalCount(), DroneDynamics.getServerCount());
    }

    public void updateCount() {
        Drone.setLocalCount(Saveable.checkLocalCount(Drone.getFilename()));
        Drone.setServerCount(checkServerCount(Drone.getUrl()));

        DroneType.setLocalCount(Saveable.checkLocalCount(DroneType.getFilename()));
        DroneType.setServerCount(checkServerCount(DroneType.getUrl()));

        DroneDynamics.setLocalCount(Saveable.checkLocalCount(DroneDynamics.getFilename()));
        DroneDynamics.setServerCount(checkServerCount(DroneDynamics.getUrl()));
    }

    public static int checkServerCount(String url) { // static or nah?
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }

    public static boolean isNewDataAvailable(String filename, String URL, int localCount, int serverCount) {
        Saveable.createFile(filename);

        if(serverCount == 0) {
            //logger.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
            //TODO: Own Exception
            return false;
        }
        else if (localCount == serverCount) {
            //logger.log(Level.INFO, "local- and serverDroneCount identical.");
            return false;
        }
        else if(localCount < serverCount) {
            LOGGER.info("Yes");
            Saveable.saveAsFile(URL, serverCount, filename);
            return true;
        }
        else {
            LOGGER.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
        }
        return false;
    }

}