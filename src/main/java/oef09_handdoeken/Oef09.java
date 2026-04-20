package oef09_handdoeken;

import java.util.List;
import java.util.regex.Pattern;

public class Oef09 {

    public record Puzzle(List<String> patterns, List<String> designs) {
        public Puzzle(List<String> patterns, List<String> designs) {
            // ensure immutable
            this.patterns = List.copyOf(patterns);
            this.designs = List.copyOf(designs);
        }

        public static Puzzle parse(String input) {
            var lines = input.lines().toList();
            var patterns = List.of(lines.getFirst().split(Pattern.quote(", ")));
            if (!lines.get(1).isBlank()) throw new IllegalArgumentException("Expected empty line after patterns");
            var designs = lines.subList(2, lines.size());
            return new Puzzle(patterns, designs);
        }

        public long nbPossibleDesigns() {
            // TODO
            return -1;
        }

        public long countDifferentRealizations(String design) {
            // TODO
            return -1;
        }

    }
}