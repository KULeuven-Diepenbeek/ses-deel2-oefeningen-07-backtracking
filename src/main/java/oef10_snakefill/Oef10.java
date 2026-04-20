package oef10_snakefill;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Oef10 {
    public interface Board {
        /**
         * Huidige positie van het hoofd van de slang
         */
        Position getCurrentHead();

        /**
         * Maakt de gegeven positie deel uit van het bord?
         */
        boolean isValid(Position pos);

        /**
         * Is de gegeven positie vrij (geen muur, geen slang)”
         */
        boolean isFree(Position pos);

        /**
         * Zijn alle posities van het bord gevuld?
         */
        boolean isFull();

        /**
         * Telt het aantal opeenvolgende vrije cellen in de gegeven
         * richting vanaf het hoofd van de slang.
         * In situatie (A) in het voorbeeld geeft deze methode
         * 5 terug voor DOWN, 2 voor RIGHT, en 0 voor LEFT en UP.
         */
        int freeCells(Direction dir);

        /**
         * Maak de slang n vakken langer in de gegeven richting.
         * In het voorbeeld ga je van (A) naar (B) met n=5 en dir=DOWN
         */
        void extendSnake(int n, Direction dir);

        /**
         * Maak de slang n vakken korter in de gegeven richting.
         * In het voorbeeld ga je van (B) naar (A) met n=5 en dir=UP
         */
        void retractSnake(int n, Direction dir);

        /**
         * Maak een kopie van het bord
         */
        Board copy();
    }

    /**
     * Deze implementatie is gegeven en verondersteld correct.
     * Je moet deze niet aanpassen.
     */
    static class BoardImpl implements Board {

        private final int nbRows;
        private final int nbCols;

        public BoardImpl(int nbRows, int nbCols, Position head) {
            this.nbRows = nbRows;
            this.nbCols = nbCols;
            this.head = head;
            this.snake.add(head);
        }

        private Position head;
        private final Set<Position> snake = new HashSet<>();
        private final Set<Position> walls = new HashSet<>();

        protected void addWalls(Position... positions) {
            for (var pos : positions) {
                if (!isValid(pos)) throw new IllegalArgumentException();
            }
            Collections.addAll(walls, positions);
        }

        @Override
        public Position getCurrentHead() {
            return head;
        }

        @Override
        public boolean isValid(Position pos) {
            return pos.row() >= 0 && pos.row() < nbRows && pos.col() >= 0 && pos.col() < nbCols;
        }

        @Override
        public boolean isFree(Position pos) {
            return isValid(pos) && !walls.contains(pos) && !snake.contains(pos);
        }

        protected Stream<Position> positions() {
            return IntStream.range(0, nbRows)
                    .boxed()
                    .flatMap(row -> IntStream.range(0, nbCols)
                            .mapToObj(col -> new Position(row, col)));
        }

        @Override
        public boolean isFull() {
            return positions().noneMatch(this::isFree);
        }

        @Override
        public int freeCells(Direction dir) {
            int result = 1;
            while (isFree(head.go(dir, result)))
                result++;
            return result - 1;
        }

        @Override
        public void extendSnake(int n, Direction dir) {
            while (n > 0) {
                var newHead = head.go(dir, 1);
                if (!isFree(newHead)) throw new IllegalStateException();
                head = newHead;
                snake.add(newHead);
                n--;
            }
        }

        @Override
        public void retractSnake(int n, Direction dir) {
            while (n > 0) {
                snake.remove(head);
                head = head.go(dir.opposite(), 1);
                n--;
            }
        }

        @Override
        public Board copy() {
            var result = new BoardImpl(nbRows, nbCols, head);
            result.snake.addAll(snake);
            result.walls.addAll(walls);
            return result;
        }

        @Override
        public String toString() {
            var result = new StringBuilder();
            for (int row = 0; row < nbRows; row++) {
                for (int col = 0; col < nbCols; col++) {
                    var pos = new Position(row, col);
                    if (walls.contains(pos)) {
                        result.append("X");
                    } else if (pos.equals(head)) {
                        result.append("S");
                    } else if (snake.contains(pos)) {
                        result.append("s");
                    } else {
                        result.append(".");
                    }
                }
                result.append("\n");
            }
            return result.toString();
        }
    }

    /**
     * Positie op het bord
     */
    record Position(int row, int col) {
        /**
         * Positie op n vakken vanaf de huidige positie in de
         * gegeven richting.
         */
        public Position go(Direction dir, int n) {
            return switch (dir) {
                case RIGHT -> new Position(row, col + n);
                case LEFT -> new Position(row, col - n);
                case UP -> new Position(row - n, col);
                case DOWN -> new Position(row + n, col);
            };
        }
    }

    /**
     * Een richting (UP, DOWN, LEFT, RIGHT).
     */
    enum Direction {
        UP, DOWN, LEFT, RIGHT;
        public static final Direction[] ALL_DIRECTIONS =
                {UP, DOWN, LEFT, RIGHT};

        /**
         * Geef de omgekeerde richting terug
         * (UP <-> DOWN, LEFT <-> RIGHT)
         */
        public Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    public static void main(String[] args) {
        var board = new BoardImpl(6, 5, new Position(0, 0)) {{
            addWalls(new Position(0, 3),
                    new Position(0, 4),
                    new Position(3, 2),
                    new Position(3, 3),
                    new Position(3, 4));
        }};

        var solution = solve(board);
        if (solution != null)
            IO.println(solution);
        else
            IO.println("No solution");
    }


    public static List<Direction> solve(Board board) {
        // TODO
        return null;
    }

}
