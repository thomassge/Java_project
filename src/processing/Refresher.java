package processing;

import data.Drone;
import data.DroneDynamics;
import data.DroneType;
import gui.DroneMenu;
import org.json.JSONObject;
import util.WebserverDataFetcher;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Refresher {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    private static boolean isDronesNew;
    private static boolean isDroneTypesNew;
    private static boolean isDroneDynamicsNew;
    static boolean isRefreshNeeded;
    static boolean isInitial = true;

    Drone droneObject = new Drone();
    DroneType droneTypeObject = new DroneType();
    DroneDynamics droneDynamicsObject = new DroneDynamics();

    public Refresher() {
        LOGGER.info("called Refresher constructor");
        checkForRefresh();
    }

    public void checkForRefresh() {
        updateCount();
        isDronesNew = droneObject.isNewDataAvailable();
        isDroneTypesNew = droneTypeObject.isNewDataAvailable();
        isDroneDynamicsNew = droneDynamicsObject.isNewDataAvailable();
        isRefreshNeeded = isDronesNew || isDroneTypesNew || isDroneDynamicsNew;
    }

    private void updateCount() {
        Drone.setLocalCount(droneObject.checkLocalCount(Drone.getFilename()));
        Drone.setServerCount(checkServerCount(Drone.getUrl()));

        DroneType.setLocalCount(droneTypeObject.checkLocalCount(DroneType.getFilename()));
        DroneType.setServerCount(checkServerCount(DroneType.getUrl()));

        DroneDynamics.setLocalCount(droneDynamicsObject.checkLocalCount(DroneDynamics.getFilename()));
        DroneDynamics.setServerCount(checkServerCount(DroneDynamics.getUrl()));
    }

    private int checkServerCount(String url) { // static or nah?
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }

    public abstract void refresh();
}
