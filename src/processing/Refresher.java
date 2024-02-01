package processing;

import data.Drone;
import data.DroneDynamics;
import data.DroneType;

import org.json.JSONObject;
import util.WebserverDataFetcher;
import java.util.logging.Logger;

/**
 * This class holds logic that is needed to check whether there is new data to be fetched from the
 * webserver. It does not do the refresh itself this has to be done by the class that extends it,
 * hence the abstract method.
 * @author Leon Oet
 */
public abstract class Refresher {

    private static final Logger LOGGER = Logger.getLogger(Refresher.class.getName());

    private static boolean isRefreshNeeded = false;

    /**
     * The Refresher constructor checks for a refresh every time it is called.
     * It can not be instantiated so the only time it is being called is when the class
     * that extends it is called. In our case that is DataFactory.
     * Every time a DataFactory object is created (which should happen once in a program lifetime and once
     * when our Thread iis started) the constructor gets called, and it checks for a refresh.
     */
    public Refresher() {
        LOGGER.info("Called Refresher constructor");
        checkForRefresh();
    }

    public static boolean getIsRefreshNeeded() {
        return isRefreshNeeded;
    }
    public static void setIsRefreshNeeded(boolean isRefreshNeeded) {
        Refresher.isRefreshNeeded = isRefreshNeeded;
    }

    /**
     * When an Object wants to extend the Refresher class, it has to implement the logic how to refresh
     */
    public abstract void refresh();

    /**
     * This method checks whether a refresh is needed by updating the count of our local file and
     * the web server. It then compares them to check if there is new data available.
     * @return true if new data has to be fetched and false otherwise
     */
    boolean checkForRefresh() {
        updateCount();
        boolean isDronesNew = new Drone().isNewDataAvailable();
        boolean isDroneTypesNew = new DroneType().isNewDataAvailable();
        boolean isDroneDynamicsNew = new DroneDynamics().isNewDataAvailable();
        isRefreshNeeded = isDronesNew || isDroneTypesNew || isDroneDynamicsNew;
        return isRefreshNeeded;
    }

    private void updateCount() {
        Drone.setLocalCount(Drone.checkFileCount(Drone.getFilename()));
        Drone.setServerCount(checkServerCount(Drone.getUrl()));
        DroneType.setLocalCount(DroneType.checkFileCount(DroneType.getFilename()));
        DroneType.setServerCount(checkServerCount(DroneType.getUrl()));
        DroneDynamics.setLocalCount(DroneDynamics.checkFileCount(DroneDynamics.getFilename()));
        DroneDynamics.setServerCount(checkServerCount(DroneDynamics.getUrl()));
    }

    private int checkServerCount(String url) {
        String jsonString = WebserverDataFetcher.jsonCreator(url + "?limit=1");
        JSONObject obj = new JSONObject(jsonString);
        return obj.getInt("count");
    }
}
