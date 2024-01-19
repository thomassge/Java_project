package data;

import org.json.JSONArray;
import org.json.JSONObject;
import util.WebserverDataFetcher;

public class DataFactory extends Refresh {

                                    // FIELDS
    DataStorage dataStorage;

                                    // GETTER AND SETTER

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

                                    // CONSTRUCTOR

    public DataFactory(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        specificFetch(Drone.getUrl(), Drone.getServerCount(), 0);
        specificFetch(DroneType.getUrl(), DroneType.getServerCount(), 0);
        specificFetch(DroneDynamics.getUrl(), DroneDynamics.getServerCount(), 0);

    }

                                    // METHODS

    public void specificFetch(String url, int limit, int offset) {
        url = url + WebserverDataFetcher.urlModifier(limit, offset);
        String jsonString = WebserverDataFetcher.jsonCreator(url);

        switch(checkObject(jsonString)) {
            case "Drone":
                //dataStorage.setDrones(Drone.initialize(jsonString, dataStorage.getDrones()));
                Drone.initialize(jsonString, dataStorage.getDrones());
                break;
            case "DroneType":
                DroneType.initialize(jsonString, dataStorage.getDroneTypes());
                break;
            case "DroneDynamics":
                DroneDynamics.initialize(jsonString, dataStorage.getDroneDynamics());
                break;
            default:
                System.out.println(jsonString);
                break;
        }
    }

    public static String checkObject(String jsonString) {
        JSONObject wholeHtml = new JSONObject(jsonString);
        JSONArray jsonArray = wholeHtml.getJSONArray("results");
        JSONObject o = jsonArray.getJSONObject(0);

        if(o.has("carriage_type")) {
            return "Drone";
        }
        else if(o.has("max_speed")) {
            return "DroneType";
        }
        else if(o.has("timestamp")) {
            return "DroneDynamics";
        }
        else {
            return "Error";
        }
    }


}
