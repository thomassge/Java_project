package processing;

import data.*;
import logging.LogMain;
import org.json.JSONArray;
import org.json.JSONObject;
import processing.JSONDeruloHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Refresh {
    JSONDeruloHelper helper = new JSONDeruloHelper();

    public void refresh(LinkedList<Drone> drones, LinkedList<DroneType> droneTypes) throws IOException {
        try {
            if (helper.droneObject.getServerCount() > helper.getNumberOfDrones()) {
                String modifiedDroneURL = helper.getDronesUrl() + "?offset=" + helper.getNumberOfDrones();
                String forCreatingDroneObjects = helper.jsonCreator(modifiedDroneURL);
                helper.individualDroneJsonToObject(forCreatingDroneObjects, drones);
                LogMain.getLogger().log(Level.INFO,"New Drones added");
            } else {
                LogMain.getLogger().log(Level.INFO,"No new Drone Information in the database");
            }

            if (helper.droneTypesObject.getServerCount() > helper.getNumberOfDroneTypes()) {
                String modifiedDroneTypeURL = helper.getDroneTypesUrl() + "?offset=" + helper.getNumberOfDroneTypes();
                String forCreatingDroneTypeObjects = helper.jsonCreator(modifiedDroneTypeURL);
                helper.droneTypeJsonToObject(forCreatingDroneTypeObjects, droneTypes);
                helper.droneTypeToDroneLinker(droneTypes, drones);
                LogMain.getLogger().log(Level.INFO,"New DroneTypes added");
            } else {
                LogMain.getLogger().log(Level.INFO,"No new DroneType Information in the database");
            }

            // this (offset)method works for new data that was appended to the tail of the database (json string),
            // but not if new data was inserted somewhere in the middle
            //problem with this method is, that if the data is being replaced like on 27.12.23 it might produce unsinn since the offset is not a valid abgrenzer yo
            if (helper.droneDynamicsObject.getServerCount() > helper.getNumberOfDroneDynamics()) {
                String modifiedDroneDynamicsURL = helper.getDroneDynamicsUrl() + "?offset=" + helper.getNumberOfDroneDynamics();
                String forCreatingDroneDynamics = helper.jsonCreator(modifiedDroneDynamicsURL);
                refreshDroneDynamics(drones, modifiedDroneDynamicsURL);
                LogMain.getLogger().log(Level.INFO,"New DroneDynamics added");
            } else {
                LogMain.getLogger().log(Level.INFO,"No new DroneDynamic Information in the database");
            }
            LogMain.getLogger().log(Level.INFO, "Data successfully updated.");
        }   catch (Exception e) {
            LogMain.getLogger().log(Level.SEVERE, "Fehler beim Aktualisieren der Daten.", e);
            throw new RuntimeException(e);
        }
    }

    public void refreshDroneDynamics(LinkedList<Drone> drones, String modifiedDroneDynamicsURL) {

        String myJson = helper.jsonCreator(modifiedDroneDynamicsURL);

        JSONObject myJsonObject = new JSONObject(myJson);
        JSONArray jsonArray = myJsonObject.getJSONArray("results");

        for (int z = 0; z < helper.getNumberOfDrones(); z++) { // code insists that number of drones >= number of drones that have dronedynamics
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
        helper.numberOfDroneDynamics = helper.numberOfDroneDynamics + jsonArray.length(); // Update numberOfDroneDynamics if refresh() creates new DroneDynamics data
    }

    public static void main(String[] args) {

    }
}
