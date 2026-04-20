package oef02_nqueens;

import java.util.Set;

public class Oef02 {

    public record QueenPosition(int row, int col) {
    }

    public record NQueensSolution(int n, Set<QueenPosition> queenPositions) {

        @Override
        public String toString() {
            var result = "\n";
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    if (queenPositions.contains(new QueenPosition(row, col))) {
                        result += "Q ";
                    } else {
                        result += ". ";
                    }
                }
                result += "\n";
            }
            return result + "\n";
        }
    }

    public static NQueensSolution eightqueens() {
        // TODO
        return null;
    }

    public static NQueensSolution nqueens_any(int n) {
        // TODO
        return null;
    }

    public static Set<NQueensSolution> nqueens_all(int n) {
        // TODO
        return null;
    }

    static void main() {
        var sol8 = eightqueens();
        IO.println(sol8);

        var solutions = nqueens_all(5);
        IO.println(solutions);
    }
}
