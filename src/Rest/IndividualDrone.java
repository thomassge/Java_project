package Rest;

public class IndividualDrone extends Drone{

    private static final String ENDPOINT_URL = "https://dronesim.facets-labs.com/api/drones/?format=json";

    public IndividualDrone() {};

    public static void main(String[] args) {
        IndividualDrone individualDroneObject = new IndividualDrone();
        HelloRest.connector(ENDPOINT_URL, individualDroneObject);
    }
}
