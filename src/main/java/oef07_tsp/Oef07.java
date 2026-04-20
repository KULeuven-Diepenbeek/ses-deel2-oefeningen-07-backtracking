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
        public double totalDistance() {
            double distance = 0.0;
            for (int i = 0; i < locations().size() - 1; i++) {
                distance += locations().get(i).distanceTo(locations().get(i + 1));
            }
            return distance;
        }

        public Route extend(Location loc) {
            var newLocations = new ArrayList<>(locations);
            newLocations.add(loc);
            return new Route(newLocations);
        }

        public Route ensureClosed() {
            if (!locations().getLast().equals(locations().getFirst()))
                return this.extend(locations().getFirst());
            return this;
        }
    }

    public static Route shortestRoute(Location start, List<Location> otherLocations) {
        return shortestRoute(new ArrayList<>(otherLocations), new Route(List.of(start)), null);
    }

    private static Route shortestRoute(List<Location> toVisit, Route routeSoFar, Route shortestSoFar) {
        if (routeSoFar.locations().isEmpty()) throw new AssertionError("routeSoFar should not be empty");

        if (toVisit.isEmpty()) {
            // close tour if it's not already closed
            routeSoFar = routeSoFar.ensureClosed();
            if (shortestSoFar == null || routeSoFar.totalDistance() < shortestSoFar.totalDistance())
                return routeSoFar;
            return shortestSoFar;
        }

        // optimization 1: if closing the current route is already longer than our current best tour, abort
        if (shortestSoFar != null && routeSoFar.ensureClosed().totalDistance() >= shortestSoFar.totalDistance()) return shortestSoFar;

        // optimization 2: first try going to the nearest unvisited location
        var toVisitSorted = new ArrayList<>(toVisit);
        var lastLocation = routeSoFar.locations().getLast();
        toVisitSorted.sort(Comparator.comparing((Location l) -> l.distanceTo(lastLocation)));

        // optimization 3: just try going to the furthest location and then back to the start; the final distance will definitely be larger
        if (shortestSoFar != null && !toVisitSorted.isEmpty() && routeSoFar.extend(toVisitSorted.getLast()).ensureClosed().totalDistance() >= shortestSoFar.totalDistance()) return shortestSoFar;

        for (int i = 0; i < toVisitSorted.size(); i++) {
            var next = toVisitSorted.get(i);
            var newRoute = routeSoFar.extend(next);
            toVisitSorted.remove(i);
            shortestSoFar = shortestRoute(toVisitSorted, newRoute, shortestSoFar);
            toVisitSorted.add(i, next);
        }

        return shortestSoFar;
    }

}
