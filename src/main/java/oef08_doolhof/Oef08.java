package oef08_doolhof;

import java.util.*;
import java.util.stream.Stream;

public class Oef08 {

    public static class Doolhof {

        private final int rows;
        private final int cols;
        private Position start;
        private final Set<Position> finishPositions = new HashSet<>();
        private final Set<Position> walls = new HashSet<>();

        public List<Position> shortestPath() {
            // TODO
            return null;
        }

        private Collection<Position> neighbors(Position pos) {
            if (!isValid(pos)) throw new IllegalArgumentException();

            return Stream.of(pos.left(), pos.right(), pos.up(), pos.down())
                    .filter(this::isValid)
                    .toList();
        }

        public Position getStart() {
            return start;
        }

        public boolean isFinish(Position pos) {
            return finishPositions.contains(pos);
        }

        public boolean hasWallAt(Position pos) {
            if (!isValid(pos)) throw new IllegalArgumentException();
            return walls.contains(pos);
        }

        public Set<Position> getFinishPositions() {
            return Set.copyOf(finishPositions);
        }

        public record Position(int row, int col) {
            public Position left() {
                return new Position(row, col - 1);
            }
            public Position right() {
                return new Position(row, col + 1);
            }
            public Position up() {
                return new Position(row - 1, col);
            }
            public Position down() {
                return new Position(row + 1, col);
            }

            public boolean isNeighborOf(Position other) {
                return Math.abs(row - other.row()) + Math.abs(col - other.col()) == 1;
            }

            @Override
            public String toString() {
                return "(" + row + ", " + col + ")";
            }
        }

        public Doolhof(int nbRows, int nbCols) {
            this.rows = nbRows;
            this.cols = nbCols;
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }

        public void setStart(Position pos) {
            if (!isValid(pos)) throw new IllegalArgumentException();
            this.makeFree(pos);
            this.start = pos;
        }

        public boolean isValid(Position pos) {
            return pos != null && pos.row() >= 0 && pos.row() < rows && pos.col() >= 0 && pos.col() < cols;
        }

        private void makeFinish(Position pos) {
            if (!isValid(pos)) throw new IllegalArgumentException();
            this.makeFree(pos);
            this.finishPositions.add(pos);
        }

        private void makeBlocked(Position pos) {
            if (!isValid(pos)) throw new IllegalArgumentException();
            if (pos.equals(start) || finishPositions.contains(pos)) throw new IllegalArgumentException("Cannot block start or finish");
            walls.add(pos);
        }

        private void makeFree(Position pos) {
            if (!isValid(pos)) throw new IllegalArgumentException();
            walls.remove(pos);
        }

        public static Doolhof fromString(String str) {
            var lines = str.lines().toList();
            var nbRows = lines.size();
            if (nbRows > 0) {
                var nbCols = lines.get(0).length();
                var result = new Doolhof(nbRows, nbCols);
                for (int row = 0; row < nbRows; row++) {
                    var line = lines.get(row);
                    for (int col = 0; col < nbCols; col++) {
                        var pos = new Position(row, col);
                        var ch = Character.toUpperCase(line.charAt(col));
                        if (ch == '@') {
                            result.setStart(pos);
                        } else if (ch == '$') {
                            result.makeFinish(pos);
                        } else if (ch == 'X') {
                            result.makeBlocked(pos);
                        } else if (ch == '.') {
                            result.makeFree(pos);
                        } else {
                            throw new IllegalArgumentException("Invalid character: " + ch);
                        }
                    }
                }
                return result;
            }
            return new Doolhof(0, 0);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    var pos = new Position(row, col);
                    if (walls.contains(pos)) {
                        result.append("X");
                    } else if (pos.equals(start)) {
                        result.append("@");
                    } else if (finishPositions.contains(pos)) {
                        result.append("$");
                    } else {
                        result.append(".");
                    }
                }
                result.append("\n");
            }
            return result.toString();
        }
    }
}
