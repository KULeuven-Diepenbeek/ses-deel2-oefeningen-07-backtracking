package oef02_nqueens;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Oef02 {

    public record QueenPosition(int row, int col) {
        public boolean conflicts(QueenPosition pos) {
            return pos.row() == row || pos.col() == col ||
                    Math.abs(pos.row() - row) == Math.abs(pos.col() - col);
        }
    }


    private static boolean isFree(QueenPosition pos, List<QueenPosition> queens) {
        return queens.stream().noneMatch(pos::conflicts);
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
        return eightqueens(new ArrayList<>());
    }

    private static NQueensSolution eightqueens(List<QueenPosition> queensSoFar) {
        if (queensSoFar.size() == 8) {
            return new NQueensSolution(8, Set.copyOf(queensSoFar));
        }

        var nextCol = queensSoFar.size();
        for (int row = 0; row < 8; row++) {
            var candidatePosition = new QueenPosition(row, nextCol);
            if (isFree(candidatePosition, queensSoFar)) {
                queensSoFar.add(candidatePosition);
                var solution = eightqueens(queensSoFar);
                if (solution != null) {
                    return solution;
                }
                queensSoFar.removeLast();
            }
        }

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
