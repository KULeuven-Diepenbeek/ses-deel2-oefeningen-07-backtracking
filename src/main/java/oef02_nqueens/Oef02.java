package oef02_nqueens;

import java.util.List;
import java.util.Set;

public class Oef02 {

    public record QueenPosition(int row, int col) {
        public boolean isAttackedBy(QueenPosition other) {
            return this.row == other.row || this.col == other.col || Math.abs(this.row - other.row) == Math.abs(this.col - other.col);
        }
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
        return nqueens_any(8);
    }

    public static NQueensSolution nqueens_any(int n) {
        return nqueens_any_worker(n, new java.util.ArrayList<>());
    }

    private static NQueensSolution nqueens_any_worker(int n, List<QueenPosition> queensSoFar) {
        if (queensSoFar.size() == n) {
            return new NQueensSolution(n, Set.copyOf(queensSoFar));
        }

        int nextColumn = queensSoFar.size();
        for (int row = 0; row < n; row++) {
            QueenPosition candidatePosition = new QueenPosition(row, nextColumn);
            if (queensSoFar.stream().noneMatch(candidatePosition::isAttackedBy)) {
                queensSoFar.add(candidatePosition);
                var solution = nqueens_any_worker(n, queensSoFar);
                if (solution != null) return solution;
                queensSoFar.removeLast();
            }
        }

        return null;
    }

    public static Set<NQueensSolution> nqueens_all(int n) {
        return nqueens_all_worker(n, new java.util.ArrayList<>(), new java.util.HashSet<>());
    }

    private static Set<NQueensSolution> nqueens_all_worker(int n, List<QueenPosition> queensSoFar, Set<NQueensSolution> solutions) {
        if (queensSoFar.size() == n) {
            var sol = new NQueensSolution(n, Set.copyOf(queensSoFar));
            solutions.add(sol);
           return solutions;
        }

        int nextColumn = queensSoFar.size();
        for (int row = 0; row < n; row++) {
            QueenPosition candidatePosition = new QueenPosition(row, nextColumn);
            if (queensSoFar.stream().noneMatch(candidatePosition::isAttackedBy)) {
                queensSoFar.add(candidatePosition);
                nqueens_all_worker(n, queensSoFar, solutions);
                queensSoFar.removeLast();
            }
        }

        return solutions;
    }

    static void main() {
        var sol8 = eightqueens();
        IO.println(sol8);

        var solutions = nqueens_all(5);
        IO.println(solutions);
    }
}
