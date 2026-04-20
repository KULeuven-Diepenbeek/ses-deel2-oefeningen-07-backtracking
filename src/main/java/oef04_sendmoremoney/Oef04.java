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
            // TODO
            return null;
        }

        public Set<PuzzleSolution> solveAll() {
            // TODO
            return null;
        }

        private long replace(String str, Map<Character, Integer> mapping) {
            for (var entry : mapping.entrySet()) {
                str = replaceCharWithNumber(str, entry.getKey(), entry.getValue());
            }
            return Long.parseLong(str);
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
