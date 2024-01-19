package data;

import org.json.JSONObject;
import util.WebserverDataFetcher;

public abstract class Refresh implements Saveable {

    public Refresh() {
        System.out.println("called Refresh constructor");
        updateCount();
        checkForNewData(Drone.getFilename(), Drone.getUrl(), Drone.getLocalCount(), Drone.getServerCount());
        checkForNewData(DroneType.getFilename(), DroneType.getUrl(), DroneType.getLocalCount(), DroneType.getServerCount());
        checkForNewData(DroneDynamics.getFilename(), DroneDynamics.getUrl(), DroneDynamics.getLocalCount(), DroneDynamics.getServerCount());
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

    public static void checkForNewData(String filename, String URL, int localCount, int serverCount) {
        Saveable.checkFile(filename);

        if(serverCount == 0) {
            //logger.log(Level.SEVERE, "ServerDroneCount is 0. Please check database");
            //TODO: Own Exception
        }
        else if (localCount == serverCount) {
            //logger.log(Level.INFO, "local- and serverDroneCount identical.");
        }
        else if(localCount < serverCount) {
            Saveable.saveAsFile(URL, serverCount, filename);
        }
        else {
            //logger.log(Level.WARNING, "localDroneCount is greater than serverDroneCount. Please check database");
        }
    };

}
