package Rest;

public class DroneDynamics extends Drone {

    private static final String DRONEDYNAMICS_URL = "https://dronesim.facets-labs.com/api/dronedynamics/?format=json";

    public DroneDynamics() {};

    public static void main(String[] args) {
        DroneDynamics obj = new DroneDynamics();
        HelloRest.connector(DRONEDYNAMICS_URL, obj);
    }
}
