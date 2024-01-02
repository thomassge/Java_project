package data;

import dronesim.*;
import processing.*;
import services.*;
import org.json.JSONObject;

import java.io.*;


public class DroneType implements Expandable {

    //DRONETYPE DATA
    private int droneTypeID;
    private String manufacturer;
    private String typename;
    private int weight;
    private int maximumSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maximumCarriage;

    //Constructor
    public DroneType() {}
    public DroneType(int droneTypeID, String manufacturer, String typename, int weight, int maximumSpeed, int batteryCapacity, int controlRange, int maximumCarriage) {
        System.out.println("DroneType Object created");
        this.droneTypeID = droneTypeID;
        this.manufacturer = manufacturer;
        this.typename = typename;
        this.weight = weight;
        this.maximumSpeed = maximumSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maximumCarriage = maximumCarriage;
    }

    //GETTER-methods
    public int getDroneTypeID(){
        return this.droneTypeID;
    }
    public String getManufacturer(){
        return this.manufacturer;
    }
    public String getTypename(){
        return this.typename;
    }
    public int getWeight(){
        return this.weight;
    }
    public int getMaximumSpeed(){
        return this.maximumSpeed;
    }
    public int getBatteryCapacity(){
        return this.batteryCapacity;
    }
    public int getControlRange(){
        return this.controlRange;
    }
    public int getMaximumCarriage(){
        return this.maximumCarriage;
    }

    public static int getCount() {
        String checkDroneTypes = "https://dronesim.facets-labs.com/api/dronetypes/?limit=1";
        String jsonDroneTypes = JSONDeruloHelper.jsonCreator(checkDroneTypes);
        JSONObject droneTypeJsonObject = new JSONObject(jsonDroneTypes);
        return droneTypeJsonObject.getInt("count");
    }

    //PRINT-methods to test without GETTER
    public void printDroneType() {
        System.out.println("DroneType id: " + this.droneTypeID);
        System.out.println("___________________________________________");
        System.out.println("Manufacturer: " + this.manufacturer );
        System.out.println("TypeName: " + this.typename);
        System.out.println("Weight: " + this.weight);
        System.out.println("Maximum Speed: " + this.maximumSpeed);
        System.out.println("BatteryCapacity: " + this.batteryCapacity);
        System.out.println("Control Range (int): " + this.controlRange);
        System.out.println("Maximum Carriage: " + this.maximumCarriage);
        System.out.println("\n");
    }

    @Override
    public int getCountOffLocalJson() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("dronetypes.json"));
        StringBuilder jsonContent = new StringBuilder();
        int limit = 20;
        int readChars = 0;
        int currentChar = 0;

        while ((currentChar = reader.read()) != -1 && readChars < limit) {
            jsonContent.append((char) currentChar);
            readChars++;
        }

        int fileDroneTypeCount = Integer.parseInt(jsonContent.toString().replaceAll("[^0-9]", ""));
        return fileDroneTypeCount;
    }

    @Override
    public boolean checkForNewData(int serverDroneTypeCount) throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader("dronetypes.json"))) {

            int fileDroneTypeCount = getCountOffLocalJson();

            if (serverDroneTypeCount == fileDroneTypeCount) {
                return false;
            } else {
                System.out.println("damn, refetching");
                return true;
            }
        }
        catch (FileNotFoundException fnfE) {
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAsFile() {
        int limit = DroneType.getCount();

        try {
            if(!(checkForNewData(limit))) {
                System.out.println("No New DroneType Data to fetch from");
                return;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("DroneTypes Count: " + limit);
        String forCreatingDroneTypeObjects = JSONDeruloHelper.jsonCreator(JSONDeruloHelper.getDroneTypesUrl() + "?limit=" + limit);

        System.out.println("Saving DroneType Data from Webserver in file ...");

        try (PrintWriter out = new PrintWriter("dronetypes.json")) {
            out.println(forCreatingDroneTypeObjects);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /*@Override
    public void print() {
        printDroneType();
    }*/

}