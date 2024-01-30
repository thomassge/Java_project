package processing;

import data.Drone;
import data.DroneDynamics;
import data.DroneType;
import org.json.JSONObject;
import util.WebserverDataFetcher;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Refresher {
    private static final Logger LOGGER = Logger.getLogger(Refresher.class.getName());
    static boolean isRefreshNeeded = false;

    public Refresher() {
        LOGGER.info("Called Refresher constructor");
        checkForRefresh();
    }

    public abstract void refresh();

    public boolean checkForRefresh() {
        updateCount();
        boolean isDronesNew = new Drone().isNewDataAvailable();
        boolean isDroneTypesNew = new DroneType().isNewDataAvailable();
        boolean isDroneDynamicsNew = new DroneDynamics().isNewDataAvailable();
        isRefreshNeeded = isDronesNew || isDroneTypesNew || isDroneDynamicsNew;

        return isRefreshNeeded;
    }

    private void updateCount() {
        Drone.setLocalCount(new Drone().checkFileCount(Drone.getFilename()));
        Drone.setServerCount(checkServerCount(Drone.getUrl()));

        DroneType.setLocalCount(new DroneType().checkFileCount(DroneType.getFilename()));
        DroneType.setServerCount(checkServerCount(DroneType.getUrl()));

        DroneDynamics.setLocalCount(new DroneDynamics().checkFileCount(DroneDynamics.getFilename()));
        DroneDynamics.setServerCount(checkServerCount(DroneDynamics.getUrl()));
    }

    private int checkServerCount(String url) { // static or nah?
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }
}
