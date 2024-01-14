package processing;

import data.Drone;
import data.DroneDynamics;
import data.DroneType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Refresh extends JSONDeruloHelper, Streamable {
     static final Logger logger = Logger.getLogger(JSONDeruloHelper.class.getName());

    /**
     * Refreshes the data by re-fetching from the webserver and updating the lists.
     *
     * @param drones The list of Drone objects to be refreshed.
     * @param droneTypes The list of DroneType objects to be refreshed.
     * @throws IOException if there is an error during data refresh.
     */
    public void refresh(LinkedList<Drone> drones, LinkedList<DroneType> droneTypes) throws IOException {
        try {
            if (droneObject.getServerCount() > getNumberOfDrones()) {
                String modifiedDroneURL = getDronesUrl() + "?offset=" + getNumberOfDrones();
                String forCreatingDroneObjects = jsonCreator(modifiedDroneURL);
                individualDroneJsonToObject(forCreatingDroneObjects, drones);
                logger.log(Level.INFO, "New Drones added");
            } else {
                logger.log(Level.INFO, "No new Drone Information in the database");
            }

            if (droneTypesObject.getServerCount() > getNumberOfDroneTypes()) {
                String modifiedDroneTypeURL = getDroneTypesUrl() + "?offset=" + getNumberOfDroneTypes();
                String forCreatingDroneTypeObjects = jsonCreator(modifiedDroneTypeURL);
                droneTypeJsonToObject(forCreatingDroneTypeObjects, droneTypes);
                droneTypeToDroneLinker(droneTypes, drones);
                logger.log(Level.INFO, "New DroneTypes added");
            } else {
                logger.log(Level.INFO, "No new DroneType Information in the database");
            }

            // this (offset)method works for new data that was appended to the tail of the database (json string),
            // but not if new data was inserted somewhere in the middle
            //problem with this method is, that if the data is being replaced like on 27.12.23 it might produce unsinn since the offset is not a valid abgrenzer yo
            if (droneDynamicsObject.getServerCount() > getNumberOfDroneDynamics()) {
                String modifiedDroneDynamicsURL = getDroneDynamicsUrl() + "?offset=" + getNumberOfDroneDynamics();
                String forCreatingDroneDynamics = jsonCreator(modifiedDroneDynamicsURL);
                refreshDroneDynamics(drones, modifiedDroneDynamicsURL);
                logger.log(Level.INFO, "New DroneDynamics added");
            } else {
                logger.log(Level.INFO, "No new DroneDynamic Information in the database");
            }
            logger.log(Level.INFO, "Data successfully updated.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Aktualisieren der Daten.", e);
            throw new RuntimeException(e);
        }
    }

        /**
         * Refreshes the list of drones with new drone dynamics data fetching from the specified URL.
         * This method updates the drone dynamics data for each drone in the list if new data is available.
         *
         * @param drones The list of drones to update with new drone dynamics data.
         * @param modifiedDroneDynamicsURL The URL to fetch the latest drone dynamics data.
         */
        public void refreshDroneDynamics(LinkedList<Drone> drones, String modifiedDroneDynamicsURL) {

            String myJson = jsonCreator(modifiedDroneDynamicsURL);

            JSONObject myJsonObject = new JSONObject(myJson);
            JSONArray jsonArray = myJsonObject.getJSONArray("results");

            for (int z = 0; z < getNumberOfDrones(); z++) { // code insists that number of drones >= number of drones that have dronedynamics
                if (drones.get(z).droneDynamicsArrayList == null) {
                    drones.get(z).droneDynamicsArrayList = new ArrayList<DroneDynamics>();
                }
                String toCheck = "http://dronesim.facets-labs.com/api/drones/" + drones.get(z).getId() + "/";

                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject o = jsonArray.getJSONObject(j);

                    if (o.getString("drone").equals(toCheck)) {
                        drones.get(z).droneDynamicsArrayList.add(new DroneDynamics(
                                o.getString("drone"),
                                o.getString("timestamp"),
                                o.getInt("speed"),
                                o.getFloat("align_roll"),
                                o.getFloat("align_pitch"),
                                o.getFloat("align_yaw"),
                                o.getDouble("longitude"),
                                o.getDouble("latitude"),
                                o.getInt("battery_status"),
                                o.getString("last_seen"),
                                o.getString("status")
                        ));
                    }
                }
            }
            numberOfDroneDynamics = numberOfDroneDynamics + jsonArray.length(); // Update numberOfDroneDynamics if refresh() creates new DroneDynamics data
        }

    }

