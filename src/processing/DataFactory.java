/**
 * This package is responsible for processing data related to drones.
 * It includes classes for handling drone data, drone types, and drone dynamics.
 * The main class in this package is DataFactory, which manages the creation, deletion, and linking of data.
 **/
package processing;

import data.DataStorage;
import data.Drone;
import data.DroneDynamics;
import data.DroneType;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * This class holds the processing functions for the data.
 * The data is created, linked and stored here.
 * When creating a new DataFactory object, the data is refreshed.
 * @author Leon Oet
 */
public class DataFactory extends Refresher {
    private static final Logger LOGGER = Logger.getLogger(DataFactory.class.getName());

    private LinkedList<Drone> drones = new LinkedList<>();
    private LinkedList<DroneType> droneTypes = new LinkedList<>();
    private ArrayList<DroneDynamics> droneDynamics = new ArrayList<>();
    private ArrayList<DataStorage> dataStorage;

    //CONSTRUCTOR
    /**
     * A new DataFactory object should only be created once at the start of the program and at creation of our thread class.
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

    //GETTER AND SETTER
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
    //METHODS
    private void deleteData() {
        setDrones(null);
        setDroneTypes(null);
        setDroneDynamics(null);
        setDataStorage(null);
    }

    private void createData() {
        LOGGER.log(Level.INFO,"File Drone Creation");
        setDrones(new Drone().initialize());
        LOGGER.log(Level.INFO,"File DroneType Creation");
        setDroneTypes(new DroneType().initialize());
        LOGGER.log(Level.INFO,"File DroneDynamics Creation");
        setDroneDynamics(new DroneDynamics().initialize());
    }

    //METHODS FOR LINKING DATA
    private ArrayList<DataStorage> dataLinker() {
        ArrayList<DataStorage> list = new ArrayList<>();
        int droneCounter = 0;
        for(Drone _ : drones) {
            DataStorage item = new DataStorage();
            item.setDrone(linkDrone(droneCounter));
            item.setDroneType(linkDroneType(droneCounter));
            item.setDroneDynamicsList(linkDroneDynamics(droneCounter));
            list.add(item);
            droneCounter++;
        }
        return list;
    }

    private Drone linkDrone(int droneCounter) {
        return drones.get(droneCounter);
    }

    private DroneType linkDroneType(int droneCounter) {
        int droneTypeCounter = 0;
        for(DroneType _ : droneTypes) {
            if(drones.get(droneCounter).getExtractedDroneTypeID() ==
                    droneTypes.get(droneTypeCounter).getDroneTypeID()) {
                return droneTypes.get(droneTypeCounter);
            }
            else droneTypeCounter++;
        }
        return null;
    }

    private ArrayList<DroneDynamics> linkDroneDynamics(int droneCounter) {
        ArrayList<DroneDynamics> list = new ArrayList<>();
        String dronePointerToCheck = "http://dronesim.facets-labs.com/api/drones/"
                + drones.get(droneCounter).getId() + "/";
        for (DroneDynamics droneDynamicsObject : droneDynamics) {
                if (droneDynamicsObject.getDronePointer().equals(dronePointerToCheck)) {
                    list.add(droneDynamicsObject);
                }
        }
        return list;
    }

    /**
     * This method refreshes by deleting old data and re-fetching.
     * The new data is then linked and saved in this class as a dataStorage ArrayList.
     */
    @Override
    public void refresh() {
            deleteData();
            createData();
            setDataStorage(dataLinker());
    }
}
