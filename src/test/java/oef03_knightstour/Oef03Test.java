package oef03_knightstour;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static oef03_knightstour.KnightsTourAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class Oef03Test {

    @Nested
    public class KnightsTour_Any {
        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 4})
        public void no_solution(int n) {
            var sol = Oef03.knightstour(n);
            assertThat(sol)
                    .isNull();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 6, 7})
        public void valid_solution(int n) {
            var sol = Oef03.knightstour(n);
            assertThat(sol)
                    .hasSize(n)
                    .isValid();
            IO.println("Solution for n=" + n);
            IO.println(sol);
        }
    }

    @Nested
    public class KnightsTour_All {
        @Test
        public void valid_solutions_0() {
            var n = 0;
            var solutions = Oef03.all_knightstours(n);
            assertThat(solutions)
                    .isEmpty();
        }


        @Test
        public void valid_solutions_1() {
            var n = 1;
            var solutions = Oef03.all_knightstours(n);
            assertThat(solutions)
                    .hasSize(1)
                    .allSatisfy(s -> assertThat(s).hasSize(n).isValid());
        }


        @Test
        public void valid_solutions_5() {
            var n = 5;
            var solutions = Oef03.all_knightstours(n);
            assertThat(solutions)
                    .hasSize(304)
                    .allSatisfy(s -> assertThat(s).hasSize(n).isValid());
        }

    }
}

class KnightsTourAssert extends AbstractAssert<KnightsTourAssert, Oef03.KnightsTour> {

    public KnightsTourAssert(Oef03.KnightsTour actual) {
        super(actual, KnightsTourAssert.class);
    }

    public static KnightsTourAssert assertThat(Oef03.KnightsTour actual) {
        return new KnightsTourAssert(actual);
    }

    public KnightsTourAssert isValid() {
        isNotNull();

        if (Set.copyOf(actual.positions()).size() != actual.positions().size())
            failWithMessage("Duplicate positions in tour");

        if (actual.positions().size() != actual.n() * actual.n())
            failWithMessage("Tour is not complete (expected %d positions, but only got %d", actual.n() * actual.n(), actual.positions().size());

        actual.positions().stream()
                .filter(q -> q.row() < 0 || q.row() >= actual.n() || q.col() < 0 || q.col() >= actual.n())
                .forEach(q -> failWithMessage("Knight position %s is out of bounds", q));

        for (int i = 0; i < actual.positions().size() - 1; i++) {
            var first = actual.positions().get(i);
            var second = actual.positions().get(i + 1);
            if (!isKnightMove(first, second))
                failWithMessage("Not a valid knight move between position %s (at index %d) and %s (at index %d)", first, i, second, i + 1);
        }

        return this;
    }

    public KnightsTourAssert hasSize(int n) {
        isNotNull();

        if (actual.n() != n)
            failWithMessage("Expected tour of size %d, but got %d", n, actual.n());

        return this;
    }

    private boolean isKnightMove(Oef03.KnightPosition from, Oef03.KnightPosition to) {
        return Math.abs(from.row() - to.row()) == 2 && Math.abs(from.col() - to.col()) == 1 ||
                Math.abs(from.row() - to.row()) == 1 && Math.abs(from.col() - to.col()) == 2;
    }

}