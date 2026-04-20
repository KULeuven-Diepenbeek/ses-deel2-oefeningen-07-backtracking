package oef07_tsp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Oef07 {

    public record Location(String name, double x, double y) {
        public double distanceTo(Location other) {
            return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
        }
    }

    public record Route(List<Location> locations) {
    }

    public static Route shortestRoute(Location start, List<Location> otherLocations) {
        // TODO
        return null;
    }

}
