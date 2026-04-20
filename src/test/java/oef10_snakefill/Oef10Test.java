package oef10_snakefill;

import org.junit.jupiter.api.Test;

import static oef10_snakefill.Oef10.Direction.*;
import static oef10_snakefill.Oef10.Direction.DOWN;
import static org.assertj.core.api.Assertions.assertThat;

public class Oef10Test {

    @Test
    public void test_one_by_one() {
        var board = new Oef10.BoardImpl(1, 1, new Oef10.Position(0, 0));
        var solution = Oef10.solve(board);
        assertThat(solution)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void test_example() {
        var board = new Oef10.BoardImpl(6, 5, new Oef10.Position(0, 0)) {{
            addWalls(new Oef10.Position(0, 3),
                    new Oef10.Position(0, 4),
                    new Oef10.Position(3, 2),
                    new Oef10.Position(3, 3),
                    new Oef10.Position(3, 4));
        }};
        var testBoard = board.copy();

        var solution = Oef10.solve(board);
        assertThat(solution)
                .isNotNull()
                .hasSize(10)
                .containsExactly(DOWN, RIGHT, UP, LEFT, UP, RIGHT, DOWN, RIGHT, UP, LEFT);

        for (var dir : solution) {
            testBoard.extendSnake(testBoard.freeCells(dir), dir);
        }
        assertThat(testBoard.isFull()).isTrue();
    }

    @Test
    public void test_example_2() {
        var board = new Oef10.BoardImpl(6, 5, new Oef10.Position(0, 0));
        var testBoard = board.copy();

        var solution = Oef10.solve(board);
        assertThat(solution)
                .isNotNull()
                .hasSize(9);

        for (var dir : solution) {
            testBoard.extendSnake(testBoard.freeCells(dir), dir);
        }
        assertThat(testBoard.isFull()).isTrue();
    }

    @Test
    public void test_no_solution() {
        var board = new Oef10.BoardImpl(6, 5, new Oef10.Position(0, 0)) {{
            addWalls(new Oef10.Position(0, 3),
                    new Oef10.Position(0, 4),
                    new Oef10.Position(2, 2),
                    new Oef10.Position(2, 3),
                    new Oef10.Position(2, 4));
        }};

        var solution = Oef10.solve(board);
        assertThat(solution)
                .isNull();
    }


}
