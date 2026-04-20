package oef02_nqueens;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static oef02_nqueens.NQueensAssert.assertThat;

public class Oef02Test {

    @Nested
    public class EightQueens {
        @Test
        public void valid_solution() {
            var sol = Oef02.eightqueens();
            assertThat(sol)
                    .hasSize(8)
                    .isValid();
        }
    }

    @Nested
    public class NQueens_Any {
        @ParameterizedTest
        @ValueSource(ints = {2, 3})
        public void no_solution(int n) {
            var sol = Oef02.nqueens_any(n);
            assertThat(sol)
                    .isNull();
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 4, 5, 6, 7, 8})
        public void valid_solution(int n) {
            var sol = Oef02.nqueens_any(n);
            assertThat(sol)
                    .hasSize(n)
                    .isValid();
        }
    }

    @Nested
    public class NQueens_All {
        @ParameterizedTest
        @ValueSource(ints = {2, 3})
        public void no_solution(int n) {
            var sols = Oef02.nqueens_all(n);
            assertThat(sols)
                    .isEmpty();
        }

        @Test
        public void valid_solutions_1() {
            var n = 1;
            var solutions = Oef02.nqueens_all(n);
            assertThat(solutions)
                    .hasSize(1)
                    .allSatisfy(s -> assertThat(s).hasSize(n).isValid());
        }

        @Test
        public void valid_solutions_4() {
            var n = 4;
            var solutions = Oef02.nqueens_all(n);
            assertThat(solutions)
                    .hasSize(2)
                    .allSatisfy(s -> assertThat(s).hasSize(n).isValid());
        }

        @Test
        public void valid_solutions_5() {
            var n = 5;
            var solutions = Oef02.nqueens_all(n);
            assertThat(solutions)
                    .hasSize(10)
                    .allSatisfy(s -> assertThat(s).hasSize(n).isValid());
        }

    }
}

class NQueensAssert extends AbstractAssert<NQueensAssert, Oef02.NQueensSolution> {

    public NQueensAssert(Oef02.NQueensSolution actual) {
        super(actual, NQueensAssert.class);
    }

    public static NQueensAssert assertThat(Oef02.NQueensSolution actual) {
        return new NQueensAssert(actual);
    }

    public NQueensAssert isValid() {
        isNotNull();

        if (actual.queenPositions().size() != actual.n())
            failWithMessage("Expected %d queens, but only got %d", actual.n(), actual.queenPositions().size());

        actual.queenPositions().stream()
                .filter(q -> q.row() < 0 || q.row() >= actual.n() || q.col() < 0 || q.col() >= actual.n())
                .forEach(q -> failWithMessage("Queen position %s is out of bounds", q));

        if (!allPairs(actual.queenPositions().stream().toList(), (q1, q2) -> !attacks(q1, q2)))
            failWithMessage("Some queens attack each other.");

        return this;
    }

    public NQueensAssert hasSize(int n) {
        isNotNull();

        if (actual.n() != n)
            failWithMessage("Expected solution for size %d, but got %d", n, actual.n());

        return this;
    }

    private static boolean attacks(Oef02.QueenPosition pos1, Oef02.QueenPosition pos2) {
        return pos1.row() == pos2.row() || pos1.col() == pos2.col() || Math.abs(pos1.row() - pos2.row()) == Math.abs(pos1.col() - pos2.col());
    }

    private static <T> boolean allPairs(List<T> list, BiPredicate<T, T> pred) {
        return IntStream.range(0, list.size())
                .allMatch(i ->
                        IntStream.range(i + 1, list.size())
                                .allMatch(j -> pred.test(list.get(i), list.get(j)))
                );
    }
}