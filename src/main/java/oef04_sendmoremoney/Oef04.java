package oef04_sendmoremoney;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Oef04 {

    public record PuzzleSolution(Map<Character, Integer> mapping) {};

    public record Puzzle(List<String> terms, String total) {

        public Puzzle {
            if (terms.size() < 2) throw new IllegalArgumentException("At least two terms are required");
        }

        public Puzzle(String term1, String term2, String total) {
            this(List.of(term1, term2), total);
        }

        public String term1() { return terms.getFirst(); }

        public String term2() { return terms.get(1); }

        private Set<Character> allCharacters() {
            return IntStream.concat(terms.stream().flatMapToInt(String::chars), total.chars())
                    .mapToObj(i -> (char)i)
                    .collect(Collectors.toSet());
        }

        private static String replaceCharWithNumber(String str, char charToReplace, int digit) {
            if (digit < 0 || digit > 9) throw new IllegalArgumentException("Digit must be between 0 and 9");
            return str.replace(charToReplace, (char) ('0' + digit));
        }

        private static boolean containsLetter(String str) {
            return str.chars().anyMatch(Character::isLetter);
        }

        public PuzzleSolution solve() {
            return solve_worker(allCharacters(), new HashMap<>());
        }

        public Set<PuzzleSolution> solveAll() {
            return solveAll_worker(allCharacters(), new HashMap<>(), new HashSet<>());
        }

        private long replace(String str, Map<Character, Integer> mapping) {
            for (var entry : mapping.entrySet()) {
                str = replaceCharWithNumber(str, entry.getKey(), entry.getValue());
            }
            return Long.parseLong(str);
        }

        private PuzzleSolution solve_worker(Set<Character> unmappedCharacters, Map<Character, Integer> partialSolution) {
            if (terms.stream().anyMatch(t -> startsWithZero(t, partialSolution))) return null;
            if (startsWithZero(total, partialSolution)) return null;

            if (unmappedCharacters.isEmpty()) {
                if (terms.stream().mapToLong(t -> replace(t, partialSolution)).sum() == replace(total, partialSolution))
                    return new PuzzleSolution(partialSolution);
                else
                    return null;
            }

            var nextChar = unmappedCharacters.iterator().next();
            var rest = new HashSet<>(unmappedCharacters);
            rest.remove(nextChar);

            for (int digit = 0; digit < 10; digit++) {
                if (!partialSolution.containsValue(digit)) {
                    partialSolution.put(nextChar, digit);
                    var solution = solve_worker(rest, partialSolution);
                    if (solution != null) return solution;
                    partialSolution.remove(nextChar);
                }
            }

            return null;
        }

        private Set<PuzzleSolution> solveAll_worker(Set<Character> unmappedCharacters, Map<Character, Integer> partialSolution, Set<PuzzleSolution> solutions) {
            if (terms.stream().anyMatch(t -> startsWithZero(t, partialSolution))) return solutions;
            if (startsWithZero(total, partialSolution)) return solutions;

            if (unmappedCharacters.isEmpty()) {
                if (terms.stream().mapToLong(t -> replace(t, partialSolution)).sum() == replace(total, partialSolution)) {
                    solutions.add(new PuzzleSolution(Map.copyOf(partialSolution)));
                }
                return solutions;
            }

            var nextChar = unmappedCharacters.iterator().next();
            var rest = new HashSet<>(unmappedCharacters);
            rest.remove(nextChar);

            for (int digit = 0; digit < 10; digit++) {
                if (!partialSolution.containsValue(digit)) {
                    partialSolution.put(nextChar, digit);
                    solveAll_worker(rest, partialSolution, solutions);
                    partialSolution.remove(nextChar);
                }
            }

            return solutions;
        }

        private boolean startsWithZero(String term, Map<Character, Integer> partialSolution) {
            if (term.isEmpty()) return false;
            var firstChar = term.charAt(0);
            return partialSolution.containsKey(firstChar) && partialSolution.get(firstChar) == 0;
        }
    }

    static void main() {
        var puzzle = new Puzzle("SEND", "MORE", "MONEY");
        IO.println(puzzle.solve());
    }
}
