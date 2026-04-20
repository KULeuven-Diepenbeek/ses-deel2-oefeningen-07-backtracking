package oef07_tsp;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static oef07_tsp.RouteAssert.assertThat;

public class Oef07Test {

    @Test
    public void test_only_start_locations() {
        var start = new Oef07.Location("origin", 0, 0);
        var route = Oef07.shortestRoute(start, List.of());
        assertThat(route)
                .startsAt(start)
                .isTourVisitingAll(List.of())
                .hasTotalDistance(0);
    }

    @Test
    public void test_1_locations() {
        var start = new Oef07.Location("origin", 0, 0);
        var locs = List.of(new Oef07.Location("L1", 1, 1));

        var route = Oef07.shortestRoute(start, locs);
        assertThat(route)
                .startsAt(start)
                .isTourVisitingAll(locs)
                .hasTotalDistance(2 * Math.sqrt(2));
    }

    @Test
    public void test_2_locations() {
        var start = new Oef07.Location("origin", 0, 0);
        var locs = List.of(
                new Oef07.Location("L1", 1, 1),
                new Oef07.Location("L2", 2, 1)
        );
        var route = Oef07.shortestRoute(start, locs);
        assertThat(route)
                .startsAt(start)
                .isTourVisitingAll(locs)
                .hasTotalDistance(Math.sqrt(2) + 1.0 + Math.sqrt(5));
    }

    @Test
    public void test_3_locations_square() {
        var start = new Oef07.Location("origin", 0, 0);
        var locs = List.of(
                new Oef07.Location("L1", 1, 1),
                new Oef07.Location("L2", 1, 0),
                new Oef07.Location("L3", 0, 1)
        );
        var route = Oef07.shortestRoute(start, locs);
        assertThat(route)
                .startsAt(start)
                .isTourVisitingAll(locs)
                .hasTotalDistance(4.0);
    }

    @Test
    public void test_example() {
        var start = new Oef07.Location("A", 0, 0);
        var locs = List.of(
                new Oef07.Location("L0",	0.723174203,	0.990898897),
                new Oef07.Location("L1",	0.253293106,	0.60880037),
                new Oef07.Location("L2",	0.805869514,	0.875412785),
                new Oef07.Location("L3",	0.716048511,	0.071917022),
                new Oef07.Location("L4",	0.796260972,	0.578716937),
                new Oef07.Location("L5",	0.908125618,	0.148914579),
                new Oef07.Location("L6",	0.975219897,	0.06559603),
                new Oef07.Location("L7",	0.069517882,	0.090752294),
                new Oef07.Location("L8",	0.424466728,	0.874391044),
                new Oef07.Location("L9",	0.575103864,	0.39649687),
                new Oef07.Location("L10",	0.25831525,	0.152792153),
                new Oef07.Location("L11",	0.26042993,	0.461681947),
                new Oef07.Location("L12",	0.439100791,	0.211405433),
                new Oef07.Location("L13",	0.614212143,	0.033457912),
                new Oef07.Location("L14",	0.688896121,	0.6841128)
        );
        var route = Oef07.shortestRoute(start, locs);
        assertThat(route)
                .startsAt(start)
                .isTourVisitingAll(locs)
                .hasTotalDistance(3.748539544057823);
    }
}


class RouteAssert extends AbstractAssert<RouteAssert, Oef07.Route> {

    protected RouteAssert(Oef07.Route route) {
        super(route, RouteAssert.class);
    }

    public static RouteAssert assertThat(Oef07.Route route) {
        return new RouteAssert(route);
    }

    public RouteAssert startsAt(Oef07.Location start) {
        isNotNull();

        if (actual().locations().isEmpty())
            failWithMessage("Expected route to start at %s, but it is empty", start);

        if (!actual().locations().getFirst().equals(start))
            failWithMessage("Expected route to start at %s, but it started at %s", start, actual().locations().getFirst());

        return this;
    }
    public RouteAssert isTourVisitingAll(Collection<Oef07.Location> locations) {
        isNotNull();

        if (!isClosedTour(actual().locations()))
            failWithMessage("Expected route to be closed (end at starting location)");

        return this;
    }

    public RouteAssert hasTotalDistance(double expectedDistance) {
        isNotNull();

        var actualDistance = totalDistance(actual().locations());
        org.assertj.core.api.Assertions.assertThat(actualDistance)
                .as("total distance")
                .isCloseTo(expectedDistance, Percentage.withPercentage(0.01));

        return this;
    }

    public double totalDistance(List<Oef07.Location> locations) {
        double distance = 0.0;
        for (int i = 0; i < locations.size() - 1; i++) {
            distance += locations.get(i).distanceTo(locations.get(i + 1));
        }
        return distance;
    }

    private boolean isClosedTour(List<Oef07.Location> locations) {
        return locations.isEmpty() || locations.getFirst().equals(locations.getLast());
    }
}