package data;

import gui.DroneMenu;
import org.json.JSONObject;
import util.WebserverDataFetcher;

import java.util.logging.Logger;

public abstract class Refresher {
    private static final Logger LOGGER = Logger.getLogger(Refresher.class.getName());

    private static boolean isDronesNew;
    private static boolean isDroneTypesNew;
    private static boolean isDroneDynamicsNew;
    static boolean isRefreshNeeded = false;
    static boolean isInitial = true;

    public Refresher() {
        LOGGER.info("called Refresher constructor");
        checkForRefresh();
    }

    public boolean checkForRefresh() {
        updateCount();
        isDronesNew = new Drone().isNewDataAvailable();
        isDroneTypesNew = new DroneType().isNewDataAvailable();
        isDroneDynamicsNew = new DroneDynamics().isNewDataAvailable();
        isRefreshNeeded = isDronesNew || isDroneTypesNew || isDroneDynamicsNew;
        return isRefreshNeeded;
    }

    private void updateCount() {
        Drone.setLocalCount(new Drone().checkLocalCount(Drone.getFilename()));
        Drone.setServerCount(checkServerCount(Drone.getUrl()));

        DroneType.setLocalCount(new DroneType().checkLocalCount(DroneType.getFilename()));
        DroneType.setServerCount(checkServerCount(DroneType.getUrl()));

        DroneDynamics.setLocalCount(new DroneDynamics().checkLocalCount(DroneDynamics.getFilename()));
        DroneDynamics.setServerCount(checkServerCount(DroneDynamics.getUrl()));
    }

    private int checkServerCount(String url) { // static or nah?
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }

    public abstract void refresh();
}
