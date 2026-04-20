package oef04_sendmoremoney;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static oef04_sendmoremoney.PuzzleAssert.assertThat;

public class Oef04Test {

    @Test
    public void test_no_solution() {
        var puzzle = new Oef04.Puzzle("A", "A", "A");
        var solution = puzzle.solve();
        assertThat(solution).isNull();
    }

    @Test
    public void test_example() {
        var puzzle = new Oef04.Puzzle("SEND", "MORE", "MONEY");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Test
    public void test_example_2() {
        var puzzle = new Oef04.Puzzle("ONE", "TWO", "SIX");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Test
    public void test_example_3() {
        var puzzle = new Oef04.Puzzle("SUN", "FUN", "SWIM");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Test
    public void test_example_4() {
        var puzzle = new Oef04.Puzzle("CRACK", "HACK", "ERROR");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Test
    public void test_example_5() {
        var puzzle = new Oef04.Puzzle("MATH", "MYTH", "HARD");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Test
    public void test_example_6() {
        var puzzle = new Oef04.Puzzle("BASE", "BALL", "GAMES");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Test
    public void test_example_7() {
        var puzzle = new Oef04.Puzzle(List.of("ONE", "TWO", "SIX"), "NINE");
        var solution = puzzle.solve();
        assertThat(puzzle).isSolvedBy(solution);
        IO.println(puzzle);
        IO.println(solution);
    }

    @Nested
    class AllSolutions {
        @Test
        public void test_no_solutions() {
            var puzzle = new Oef04.Puzzle("A", "A", "A");
            var solutions = puzzle.solveAll();
            assertThat(solutions)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        public void test_single_solution() {
            var puzzle = new Oef04.Puzzle("SEND", "MORE", "MONEY");
            var solutions = puzzle.solveAll();
            assertThat(solutions)
                    .isNotNull()
                    .hasSize(1)
                    .allSatisfy(s -> assertThat(puzzle).isSolvedBy(s));
        }

        @Test
        public void test_many_solutions() {
            var puzzle = new Oef04.Puzzle("ONE", "TWO", "SIX");
            var solutions = puzzle.solveAll();
            assertThat(solutions)
                    .isNotNull()
                    .hasSize(620)
                    .allSatisfy(s -> assertThat(puzzle).isSolvedBy(s));
        }
    }
}

class PuzzleAssert extends AbstractAssert<PuzzleAssert, Oef04.Puzzle> {

    protected PuzzleAssert(Oef04.Puzzle puzzle) {
        super(puzzle, PuzzleAssert.class);
    }

    public static PuzzleAssert assertThat(Oef04.Puzzle puzzle) {
        return new PuzzleAssert(puzzle);
    }

    public PuzzleAssert isSolvedBy(Oef04.PuzzleSolution solution) {
        isNotNull();

        if (!solution.mapping().keySet().equals(allCharacters(actual())))
            failWithMessage("Expected mapping to contain a digit for each letter");

        if (!solution.mapping().values().stream().allMatch(digit -> digit >= 0 && digit <= 9))
            failWithMessage("Expected mapping to contain digits between 0 and 9");

        for (var term : actual.terms()) {
            if (solution.mapping().get(term.charAt(0)) == 0)
                failWithMessage("Term should not start with zero");
        }
        if (solution.mapping().get(actual.total().charAt(0)) == 0)
            failWithMessage("Total should not start with zero");

        List<Long> terms = actual().terms().stream().map(t -> replace(t, solution.mapping())).toList();
        long sum = terms.stream().reduce(0L, Long::sum);
        long expectedTotal = replace(actual().total(), solution.mapping());

        if (sum != expectedTotal) {
            String termSum = terms.stream().map(t -> "" + t).collect(Collectors.joining(" + "));
            failWithMessage("Expected %s = %s, but got %s = %s", termSum, expectedTotal, termSum, sum);
        }

        return this;
    }

    private static String replaceCharWithNumber(String str, char charToReplace, int digit) {
        if (digit < 0 || digit > 9) throw new IllegalArgumentException("Digit must be between 0 and 9");
        return str.replace(charToReplace, (char) ('0' + digit));
    }

    private long replace(String str, Map<Character, Integer> mapping) {
        for (var entry : mapping.entrySet()) {
            str = replaceCharWithNumber(str, entry.getKey(), entry.getValue());
        }
        return Long.parseLong(str);
    }

    private static Set<Character> allCharacters(Oef04.Puzzle puzzle) {
        return IntStream.concat(puzzle.terms().stream().flatMapToInt(String::chars), puzzle.total().chars())
                .mapToObj(i -> (char)i)
                .collect(Collectors.toSet());
    }
}