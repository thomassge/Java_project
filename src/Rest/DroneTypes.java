package Rest;

public class DroneTypes extends Drone{
    private static final String ENDPOINT_URL = "https://dronesim.facets-labs.com/api/dronetypes/?format=json";

    public static void main(String[] args) {
        DroneTypes DroneTypesObject = new DroneTypes();
        HelloRest.connector(ENDPOINT_URL, DroneTypesObject);
    }
}
