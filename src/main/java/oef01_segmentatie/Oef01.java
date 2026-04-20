package oef01_segmentatie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Oef01 {

    public static List<String> segmentAnySolution(String str, List<String> tokens) {
        return segmentAnySolution_worker(str, new ArrayList<>(tokens), new ArrayList<>());
    }

    private static List<String> segmentAnySolution_worker(String str, List<String> tokens, List<String> tokensSoFar) {
        if (str.isEmpty()) return tokensSoFar;

        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                tokens.remove(i);
                var solution = segmentAnySolution_worker(str.substring(token.length()), tokens, tokensSoFar);
                tokens.add(i, token);
                if (solution != null) return solution;
            }
        }

        return null;
    }

    public static Set<List<String>> segmentAllSolutions(String str, List<String> tokens) {
        return segmentAllSolutions_worker(str, new ArrayList<>(tokens), new ArrayList<>(), new HashSet<>());
    }

    private static Set<List<String>> segmentAllSolutions_worker(String str,
                                                                 List<String> tokens,
                                                                 List<String> tokensSoFar,
                                                                 Set<List<String>> solutions) {
        if (str.isEmpty()) {
            solutions.add(List.copyOf(tokensSoFar));
            return solutions;
        }

        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                tokens.remove(i);
                segmentAllSolutions_worker(str.substring(token.length()), tokens, tokensSoFar, solutions);
                tokens.add(i, token);
                tokensSoFar.removeLast();
            }
        }

        return solutions;
    }

    public static List<String> segmentOptimalSolution(String str, List<String> tokens) {
        return segmentOptimalSolution_worker(str, new ArrayList<>(tokens), new ArrayList<>(), null);
    }

    public static List<String> segmentOptimalSolution_worker(String str, List<String> tokens, List<String> tokensSoFar, List<String> bestTokenization) {
        if (str.isEmpty()) {
            if (bestTokenization == null || tokensSoFar.size() > bestTokenization.size()) {
                return List.copyOf(tokensSoFar);
            } else {
                return bestTokenization;
            }
        }

        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                tokens.remove(i);
                bestTokenization = segmentOptimalSolution_worker(str.substring(token.length()), tokens, tokensSoFar, bestTokenization);
                tokens.add(i, token);
                tokensSoFar.removeLast();
            }
        }

        return bestTokenization;
    }
}
