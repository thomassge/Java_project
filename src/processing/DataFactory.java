package processing;

import data.DataStorage;
import data.Drone;
import data.DroneDynamics;
import data.DroneType;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedList;

public class DataFactory extends Refresher {
    private static final Logger LOGGER = Logger.getLogger(DataFactory.class.getName());

    private LinkedList<Drone> drones = new LinkedList<>();
    private LinkedList<DroneType> droneTypes = new LinkedList<>();
    private ArrayList<DroneDynamics> droneDynamics = new ArrayList<>();
    private ArrayList<DataStorage> dataStorage;

    // CONSTRUCTOR
    /**
     * A new DataFactory object should only be created once at the start of the program.
     * <p>
     * When it is created, it refreshes by deleting data, regenerating it from Files and linking it to our dataStorage object.
     * <p>
     * Because it extends Refresher, the Refresher constructor is called first.
     * This makes sure the files are up-to-date by updating the local- and serverCounts and comparing them.
     * This ensures that the data is always up-to-date before it is saved in memory.
     * <p>
     * It then links the data and stores it in the dataStorage ArrayList.
     */
    public DataFactory() {
            LOGGER.log(Level.INFO,"Initial Fetch started");
            refresh();
    }

    // GETTER AND SETTER
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

    public ArrayList<DataStorage> getDataStorage() {
        return dataStorage;
    }
    public void setDataStorage(ArrayList<DataStorage> dataStorage) {
        this.dataStorage = dataStorage;
    }

    // METHODS
    private void deleteData() {
        setDrones(null);
        setDroneTypes(null);
        setDroneDynamics(null);
        setDataStorage(null);
    }

    private void createData() {
        LOGGER.log(Level.INFO,"File Drone Creation");
        setDrones(Drone.create());

        LOGGER.log(Level.INFO,"File DroneType Creation");
        setDroneTypes(DroneType.create());

        LOGGER.log(Level.INFO,"File DroneDynamics Creation");
        setDroneDynamics(DroneDynamics.create());
    }

    // METHODS FOR LINKING DATA
    private ArrayList<DataStorage> dataLinker() {
        ArrayList<DataStorage> list = new ArrayList<>();

        int i = 0;
        for(Drone obj : drones) {
            DataStorage item = new DataStorage();

            item.setDrone(linkDrone(i));
            item.setDroneType(linkDroneType(i));
            item.setDroneDynamicsList(linkDroneDynamics(i));

            list.add(item);
            i++;
        }

        return list;
    }

    private Drone linkDrone(int i) {
        return drones.get(i);
    }

    private DroneType linkDroneType(int i) {
        int j = 0;
        for(DroneType obj : droneTypes) {
            if(drones.get(i).getExtractedDroneTypeID() == droneTypes.get(j).getDroneTypeID()) {
                return droneTypes.get(j);
            }
            else j++;
        }

        return null;
    }

    private ArrayList<DroneDynamics> linkDroneDynamics(int i) {
        ArrayList<DroneDynamics> list = new ArrayList<>();
        String toCheck = "http://dronesim.facets-labs.com/api/drones/" + drones.get(i).getId() + "/";

        for (DroneDynamics obj : droneDynamics) {
                if (obj.getDronePointer().equals(toCheck)) {
                    list.add(obj);
                }
        }

        return list;
    }

    /**
     * This method checks for a refresh by comparing the local and server count of every data type.
     * If any of them is different, all data is refreshed.
     * It's refreshed by deleting the old data and fetching the new one.
     * The new data is then linked.
     */
    @Override
    public void refresh() {
            deleteData();
            createData();
            setDataStorage(dataLinker());
    }
}
