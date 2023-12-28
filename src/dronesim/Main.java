package dronesim;
import data.Drone;
import processing.JSONDerulo;

import java.io.IOException;
import java.util.LinkedList;

public class Main {
    private static JSONDerulo jsonDerulo = new JSONDerulo();
    private static LinkedList<Drone> drones = new LinkedList<Drone>();

    public static void main(String[] args) throws IOException {
        drones = jsonDerulo.getData();
    }

}