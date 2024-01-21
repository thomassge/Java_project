package data;

import gui.DroneMenu;
import org.json.JSONObject;
import util.WebserverDataFetcher;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Refresher implements Saveable {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    private static boolean isDronesNew;
    private static boolean isDroneTypesNew;
    private static boolean isDroneDynamicsNew;
    static boolean isRefreshNeeded;
    static boolean isInitial = true;

    public Refresher() {
        LOGGER.info("called Refresher constructor");
        checkForRefresh();
    }

    public static void checkForRefresh() {
        updateCount();
        isDronesNew = Drone.isNewDataAvailable();
        isDroneTypesNew = DroneType.isNewDataAvailable();
        isDroneDynamicsNew = DroneDynamics.isNewDataAvailable();
        isRefreshNeeded = isDronesNew || isDroneTypesNew || isDroneDynamicsNew;
    }

    private static void updateCount() {
        Drone.setLocalCount(Saveable.checkLocalCount(Drone.getFilename()));
        Drone.setServerCount(checkServerCount(Drone.getUrl()));

        DroneType.setLocalCount(Saveable.checkLocalCount(DroneType.getFilename()));
        DroneType.setServerCount(checkServerCount(DroneType.getUrl()));

        DroneDynamics.setLocalCount(Saveable.checkLocalCount(DroneDynamics.getFilename()));
        DroneDynamics.setServerCount(checkServerCount(DroneDynamics.getUrl()));
    }

    private static int checkServerCount(String url) { // static or nah?
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }

}
