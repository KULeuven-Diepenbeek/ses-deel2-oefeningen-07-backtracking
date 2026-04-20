package oef01_segmentatie;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Oef01Test {

    @Nested
    class SegmentAnySolution {
        @Test
        public void test_empty() {
            var solution = Oef01.segmentAnySolution("", List.of("a", "b", "c"));

            assertThat(solution).isEmpty();
        }

        @Test
        public void test_one_token() {
            var solution = Oef01.segmentAnySolution("a", List.of("a"));

            assertThat(solution).containsExactly("a");
        }

        @Test
        public void test_multiple_tokens() {
            var solution = Oef01.segmentAnySolution("abc", List.of("a", "b", "c"));

            assertThat(solution).containsExactly("a", "b", "c");
        }

        @Test
        public void test_impossible() {
            var solution = Oef01.segmentAnySolution("abc", List.of("d", "e", "f"));

            assertThat(solution).isNull();
        }

        @Test
        public void test_impossible_without_reusing() {
            var solution = Oef01.segmentAnySolution("aba", List.of("a", "b"));

            assertThat(solution).isNull();
        }
    }

    @Nested
    class SegmentAllSolutions {
        @Test
        public void test_empty() {
            var solutions = Oef01.segmentAllSolutions("", List.of("a", "b", "c"));
            assertThat(solutions).containsExactly(List.of());
        }

        @Test
        public void test_one_solution() {
            var solutions = Oef01.segmentAllSolutions("a", List.of("a", "b", "c"));
            assertThat(solutions).containsExactly(List.of("a"));
        }

        @Test
        public void test_multiple_solutions() {
            var solutions = Oef01.segmentAllSolutions("abc", List.of("a", "ab", "bc", "c"));
            assertThat(solutions).containsExactly(List.of("a", "bc"), List.of("ab", "c"));
        }

        @Test
        public void test_impossible() {
            var solutions = Oef01.segmentAllSolutions("abc", List.of("d", "e", "f"));

            assertThat(solutions).isEmpty();
        }

        @Test
        public void test_impossible_without_reusing() {
            var solutions = Oef01.segmentAllSolutions("aba", List.of("a", "b"));

            assertThat(solutions).isEmpty();
        }
    }

    @Nested
    class SegmentOptimalSolution {
        @Test
        public void test_empty() {
            var solution = Oef01.segmentOptimalSolution("", List.of("a", "b", "c"));
            assertThat(solution).isNotNull();
            assertThat(solution).isEmpty();
        }

        @Test
        public void test_one_solution() {
            var solution = Oef01.segmentOptimalSolution("a", List.of("a", "b", "c"));
            assertThat(solution).isNotNull();
            assertThat(solution).containsExactly("a");
        }

        @Test
        public void test_longest_solutions() {
            var solution = Oef01.segmentOptimalSolution("abc", List.of("abc", "a", "ab", "bc", "c", "b"));
            assertThat(solution).isNotNull();
            assertThat(solution).containsExactly("a", "b", "c");
        }


        @Test
        public void test_impossible() {
            var solution = Oef01.segmentOptimalSolution("abc", List.of("d", "e", "f"));

            assertThat(solution).isNull();
        }

        @Test
        public void test_impossible_without_reusing() {
            var solution = Oef01.segmentOptimalSolution("aba", List.of("a", "b"));

            assertThat(solution).isNull();
        }

    }
}
