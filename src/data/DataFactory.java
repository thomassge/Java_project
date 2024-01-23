package data;

import gui.DroneMenu;
import org.json.JSONArray;
import org.json.JSONObject;
import processing.Streamable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataFactory extends Refresher {
    private static final Logger LOGGER = Logger.getLogger(DroneMenu.class.getName());

    // FIELDS
    private ArrayList<DataStorage> dataStorage;
    private LinkedList<Drone> drones = new LinkedList<>();
    private LinkedList<DroneType> droneTypes = new LinkedList<>();
    private ArrayList<DroneDynamics> droneDynamics = new ArrayList<>();

    // CONSTRUCTOR
    /**
     * The first time, the DataFactory constructor is called, it checks if our Files need a
     * refresh, and refreshes if so. This happens, because it extends Refresher. It then fetches all data from the server.
     * It then links the data and stores it in the dataStorage ArrayList.
     * Every other time, it just refreshes the data in the Refresher constructor.
     */
    public DataFactory() {
        if(isInitial) {
            LOGGER.log(Level.INFO,"Initial Fetch started");
            fetchAll();
            setDataStorage(linker());
            isInitial = false;
        }
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
    /**
     * This method checks for a refresh by comparing the local and server count of every data type.
     * If any of them is different, all data is refreshed.
     * It's refreshed by deleting the old data and fetching the new one.
     * The new data is then linked.
     */
    public void refresh() {
        checkForRefresh();
        if(isRefreshNeeded) {
            deleteData();
            fetchAll();
            setDataStorage(linker());
        }
    }

    private void deleteData() {
        setDrones(null);
        setDroneTypes(null);
        setDroneDynamics(null);
        setDataStorage(null);
    }

    private void fetchAll() {
        System.out.println("File Drone Fetch");
        fileFetch(Drone.getFilename());

        System.out.println("File DroneType Fetch");
        fileFetch(DroneType.getFilename());

        System.out.println("File DroneDynamics Fetch");
        fileFetch(DroneDynamics.getFilename());

        //this.dataStorage = linker();
    }

    private void fileFetch(String filename) {
        String jsonString = Streamable.reader(filename);
        switch(checkObject(jsonString)) {
            case "Drone":
                setDrones(Drone.initialize(jsonString));
                break;
            case "DroneType":
                setDroneTypes(DroneType.initialize(jsonString));
                break;
            case "DroneDynamics":
                setDroneDynamics(DroneDynamics.initialize(jsonString));
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

    // METHODS FOR LINKING DATA
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
        for(DroneType obj : droneTypes) {
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
