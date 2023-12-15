package Rest;

public class IndividualDrone extends Drone{
    String serialnumber;
    private static final String ENDPOINT_URL = "https://dronesim.facets-labs.com/api/drones/?format=json";

    public static void main(String[] args) {
        IndividualDrone individualDroneObject = new IndividualDrone();
        HelloRest.connector(ENDPOINT_URL, individualDroneObject);
    }
}
