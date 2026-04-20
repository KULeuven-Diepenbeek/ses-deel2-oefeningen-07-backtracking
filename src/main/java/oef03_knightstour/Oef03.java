package oef03_knightstour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Oef03 {

    public record KnightPosition(int row, int col) {

        public Set<KnightPosition> nextPositions(int n) {
            return Stream.of(
                        new KnightPosition(row-2, col+1),
                        new KnightPosition(row-1, col+2),
                        new KnightPosition(row+1, col+2),
                        new KnightPosition(row+2, col+1),
                        new KnightPosition(row+2, col-1),
                        new KnightPosition(row+1, col-2),
                        new KnightPosition(row-1, col-2),
                        new KnightPosition(row-2, col-1)
                    )
                    .filter(p -> p.row >= 0 && p.row < n && p.col >= 0 && p.col < n)
                    .collect(Collectors.toSet());
        }
    }

    public record KnightsTour(int n, List<KnightPosition> positions) {

        @Override
        public String toString() {
            var result = "\n";
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    int when = positions.indexOf(new KnightPosition(row, col));
                    if (when != -1) {
                        result += "%2d ".formatted(when + 1);
                    } else {
                        result += " . ";
                    }
                }
                result += "\n";
            }
            return result + "\n";
        }
    }

    public static KnightsTour knightstour(int n) {
        return knightstour(n, new ArrayList<>(List.of(new KnightPosition(0, 0))));
    }

    private static KnightsTour knightstour(int n, List<KnightPosition> tourSoFar) {
        if (tourSoFar.size() == n*n)
            return new KnightsTour(n, List.copyOf(tourSoFar));

        var lastPos = tourSoFar.getLast();
        for (var nextPos : lastPos.nextPositions(n)) {
            if (!tourSoFar.contains(nextPos)) {
                tourSoFar.add(nextPos);
                var tour = knightstour(n, tourSoFar);
                if (tour != null) return tour;
                tourSoFar.removeLast();
            }
        }

        return null;
    }

    public static Set<KnightsTour> all_knightstours(int n) {
        return all_knightstours(n, new ArrayList<>(List.of(new KnightPosition(0, 0))), new HashSet<>());
    }

    private static Set<KnightsTour> all_knightstours(int n, List<KnightPosition> tourSoFar, Set<KnightsTour> solutions) {
        if (tourSoFar.size() == n*n) {
            solutions.add(new KnightsTour(n, List.copyOf(tourSoFar)));
            return solutions;
        }

        var lastPos = tourSoFar.getLast();
        for (var nextPos : lastPos.nextPositions(n)) {
            if (!tourSoFar.contains(nextPos)) {
                tourSoFar.add(nextPos);
                all_knightstours(n, tourSoFar, solutions);
                tourSoFar.removeLast();
            }
        }

        return solutions;
    }

    static void main() {
        var tour = all_knightstours(5);
        IO.println(tour);
    }


}
