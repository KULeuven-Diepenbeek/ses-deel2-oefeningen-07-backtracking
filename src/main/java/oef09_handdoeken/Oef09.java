package oef09_handdoeken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            return designs.stream().filter(this::isPossible).count();
        }

        public long countDifferentRealizations(String design) {
            return countDifferentRealizations(design, new HashMap<>());
        }

        private long countDifferentRealizations(String design, Map<String, Long> cache) {
            if (design.isEmpty()) return 1;
            if (cache.containsKey(design)) return cache.get(design);

            var total = 0L;
            for (var pattern : patterns) {
                if (design.startsWith(pattern)) {
                    total += countDifferentRealizations(design.substring(pattern.length()), cache);
                }
            }

            cache.put(design, total);
            return total;
        }

        private boolean isPossible(String design) {
            return isPossible(design, new HashMap<>());
        }

        private boolean isPossible(String design, Map<String, Boolean> cache) {
            if (design.isEmpty()) return true;
            if (cache.containsKey(design)) return cache.get(design);

            for (var pattern : patterns) {
                if (design.startsWith(pattern)) {
                    if (isPossible(design.substring(pattern.length()), cache)) {
                        cache.put(design, true);
                        return true;
                    }
                }
            }

            cache.put(design, false);
            return false;
        }
    }
}