package data;

import gui.DroneMenu;
import org.json.JSONArray;
import org.json.JSONObject;
import processing.Streamable;
import util.WebserverDataFetcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

public class DataFactory extends Refresh {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    // FIELDS
    private ArrayList<DataStorage> dataStorage;
    private LinkedList<Drone> drones = new LinkedList<>();
    private LinkedList<DroneType> droneTypes = new LinkedList<>();
    private ArrayList<DroneDynamics> droneDynamics = new ArrayList<>();

    // CONSTRUCTOR
    public DataFactory() {
        if(isDronesNew) {
            System.out.println("Specific Drone Fetch");
            specificFetch(Drone.getUrl(), Drone.getServerCount(), 0);
        } else {
            System.out.println("File Drone Fetch");
            fileFetch(Drone.getFilename());
        }
        if (isDroneTypesNew) {
            System.out.println("Specific DroneType Fetch");
            specificFetch(DroneType.getUrl(), DroneType.getServerCount(), 0);
        } else {
            System.out.println("File DroneType Fetch");
            fileFetch(DroneType.getFilename());
        }
        if(isDroneDynamicsNew) {
            System.out.println("Specific DroneDynamics Fetch");
            specificFetch(DroneDynamics.getUrl(), DroneDynamics.getServerCount(), 0);
        } else {
            System.out.println("File DroneDynamics Fetch");
            fileFetch(DroneDynamics.getFilename());
        }
        this.dataStorage = linker();
    }


    // GETTER AND SETTER
    public ArrayList<DataStorage> getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(ArrayList<DataStorage> dataStorage) {
        this.dataStorage = dataStorage;
    }

    public LinkedList<Drone> getDrones() {
        return drones;
    }

    public void setDrones(LinkedList<Drone> drones) {
        this.drones = drones;
    }

    public LinkedList<DroneType> getDroneTypes() {
        return droneTypes;
    }

    public void setDroneTypes(LinkedList<DroneType> droneTypes) {
        this.droneTypes = droneTypes;
    }

    public ArrayList<DroneDynamics> getDroneDynamics() {
        return droneDynamics;
    }

    public void setDroneDynamics(ArrayList<DroneDynamics> droneDynamics) {
        this.droneDynamics = droneDynamics;
    }


                                    // METHODS

    private void specificFetch(String url, int limit, int offset) {
        url = url + WebserverDataFetcher.urlModifier(limit, offset);
        String jsonString = WebserverDataFetcher.jsonCreator(url);

        switch(checkObject(jsonString)) {
            case "Drone":
                //dataStorage.setDrones(Drone.initialize(jsonString, dataStorage.getDrones()));
                Drone.initialize(jsonString, drones);
                break;
            case "DroneType":
                DroneType.initialize(jsonString, droneTypes);
                break;
            case "DroneDynamics":
                DroneDynamics.initialize(jsonString, droneDynamics);
                break;
            default:
                LOGGER.info(jsonString);
                break;
        }
    }

    private void fileFetch(String filename) {
        String jsonString = Streamable.reader(filename);
        switch(checkObject(jsonString)) {
            case "Drone":
                Drone.initialize(jsonString, drones);
                break;
            case "DroneType":
                DroneType.initialize(jsonString, droneTypes);
                break;
            case "DroneDynamics":
                DroneDynamics.initialize(jsonString, droneDynamics);
                break;
            default:
                LOGGER.info(jsonString);
                break;
        }
    }

    private String checkObject(String jsonString) {
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

    private ArrayList<DataStorage> linker () {
        ArrayList<DataStorage> list = new ArrayList<>();

        int i = 0;
        for(Drone obj : drones) {
            DataStorage item = new DataStorage();

            item.setDrone(selectDrone(i));
            item.setDroneType(selectDroneType(i));
            item.setDroneDynamicsList(selectDroneDynamics(i));

            list.add(item);
            i++;
        }

        return list;
    }

    private Drone selectDrone(int i) {

        return drones.get(i);
        //remove from list after selection
    }

    private DroneType selectDroneType(int i) {
        int j = 0;
        for(Drone obj : drones) {
            if(drones.get(i).getExtractedDroneTypeID() == droneTypes.get(j).getDroneTypeID()) {
                return droneTypes.get(j);
                //remove from list after selection
            }
            else j++;
        }

        return null;
    }

    private ArrayList<DroneDynamics> selectDroneDynamics(int i) {
        ArrayList<DroneDynamics> list = new ArrayList<>();
        String toCheck = "http://dronesim.facets-labs.com/api/drones/" + drones.get(i).getId() + "/";

        for (DroneDynamics obj : droneDynamics) {
                if (obj.getDronePointer().equals(toCheck)) {
                    list.add(obj);
                }
        }

        return list;
    }
}