package oef03_knightstour;

import java.util.List;
import java.util.Set;

public class Oef03 {

    public record KnightPosition(int row, int col) {
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
        // TODO
        return null;
    }

    public static Set<KnightsTour> all_knightstours(int n) {
        // TODO
        return null;
    }

    static void main() {
        var tour = all_knightstours(5);
        IO.println(tour);
    }


}
