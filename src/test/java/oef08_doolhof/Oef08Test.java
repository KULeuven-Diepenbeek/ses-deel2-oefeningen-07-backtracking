package oef08_doolhof;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static oef08_doolhof.DoolhofAssert.assertThat;

public class Oef08Test {

    @Test
    public void test_simple() {
        var doolhof = Oef08.Doolhof.fromString("""
                @.
                X$""");
        var solution = doolhof.shortestPath();
        assertThat(solution)
                .isNotNull()
                .solves(doolhof)
                .hasSize(3);
    }

    @Test
    public void test_example() {
        var doolhof = Oef08.Doolhof.fromString("""
                @..X.....X
                X.XXX.X.X.
                ..X.X.X...
                .XX...XX.X
                ....XX$..$""");
        var solution = doolhof.shortestPath();
        assertThat(solution)
                .isNotNull()
                .solves(doolhof)
                .hasSize(24);
    }

    @Test
    public void test_unsolvable() {
        var doolhof = Oef08.Doolhof.fromString("""
                $.X.@
                .X...
                X....""");
        var solution = doolhof.shortestPath();
        assertThat(solution)
                .isNull();
    }

    @Test
    public void test_larger() {
        var doolhof = Oef08.Doolhof.fromString("""
                $.X.X.X.X.@
                X......X..X
                XXX..XXX.X.
                X..X.X.....
                X..X.X.XXX.
                .X.....X...""");
        var solution = doolhof.shortestPath();
        assertThat(solution)
                .isNotNull()
                .solves(doolhof)
                .hasSize(21);
    }

    @Test
    public void test_multiple_paths() {
        var doolhof = Oef08.Doolhof.fromString("""
                $.X...X.X.@
                X......X..X
                ..X..X.X.X.
                ...X.X.....
                X..X...X.X.
                .X.....X...""");
        var solution = doolhof.shortestPath();
        assertThat(solution)
                .isNotNull()
                .solves(doolhof)
                .hasSize(17);
    }

    @Test
    public void test_multiple_finishes() {
        var doolhof = Oef08.Doolhof.fromString("""
                @..$.
                ..$..
                .$..$""");
        var solution = doolhof.shortestPath();
        assertThat(solution)
                .isNotNull()
                .solves(doolhof)
                .hasSize(4);
    }
}

class DoolhofAssert extends AbstractAssert<DoolhofAssert, List<Oef08.Doolhof.Position>> {
    public DoolhofAssert(List<Oef08.Doolhof.Position> actual) {
        super(actual, DoolhofAssert.class);
    }
    public static DoolhofAssert assertThat(List<Oef08.Doolhof.Position> actual) {
        return new DoolhofAssert(actual);
    }

    public DoolhofAssert hasSize(int size) {
        isNotNull();
        if (actual().size() != size) {
            failWithMessage("Expected solution to have size %d, but got %d", size, actual().size());
        }
        return this;
    }

    public DoolhofAssert solves(Oef08.Doolhof doolhof) {
        isNotNull();

        if (!actual().getFirst().equals(doolhof.getStart())) {
            failWithMessage("Expected solution to start at %s, but got %s", doolhof.getStart(), actual().getFirst());
        }
        if (!doolhof.isFinish(actual().getLast())) {
            failWithMessage("Expected solution to end at one of %s, but got %s", doolhof.getFinishPositions(), actual().getLast());
        }

        for (int i = 0; i < actual().size() - 1; i++) {
            var first = actual().get(i);
            if (doolhof.hasWallAt(first)) {
                failWithMessage("Expected solution to not contain walls, but hit a wall at %s", first);
            }
            var second = actual().get(i + 1);
            if (doolhof.hasWallAt(second)) {
                failWithMessage("Expected solution to not contain walls, but hit a wall at %s", second);
            }
            if (!second.isNeighborOf(first)) {
                failWithMessage("Expected solution to be a valid path, but got %s -> %s", first, second);
            }
        }

        return this;
    }
}