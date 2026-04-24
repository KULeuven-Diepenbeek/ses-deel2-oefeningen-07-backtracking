package demo;

import java.util.ArrayList;
import java.util.List;

public class Segmentatie {

    static void main() {
        String str = "catsanddogs";
        var tokens = List.of("s", "an", "ca", "cat", "dog", "and", "sand", "dogs");

        var someSegmentation = findSegmentation(str, tokens);
        IO.println(someSegmentation);

        var allSegmentations = findAllSegmentations(str, tokens);
        IO.println(allSegmentations);

        var shortest = findShortestSegmentation(str, tokens);
        IO.println(shortest);
    }

    public static List<String> findSegmentation(String str, List<String> tokens) {
        return findSegmentation(str, tokens, new ArrayList<>());
    }

    public static List<List<String>> findAllSegmentations(String str, List<String> tokens) {
        return findAllSegmentations(str, tokens, new ArrayList<>(), new ArrayList<>());
    }

    public static List<String> findShortestSegmentation(String str, List<String> tokens) {
        return findShortestSegmentation(str, tokens, new ArrayList<>(), null);
    }

    private static List<String> findShortestSegmentation(String str,
                                                         List<String> tokens,
                                                         List<String> tokensSoFar,
                                                         List<String> bestSoFar) {
        if (str.isEmpty()) {
            if (bestSoFar == null || tokensSoFar.size() < bestSoFar.size()) {
                return List.copyOf(tokensSoFar);
            } else {
                return bestSoFar;
            }
        }

        if (bestSoFar != null && tokensSoFar.size() >= bestSoFar.size()) {
            return bestSoFar;
        }

        for (var token : tokens) {
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                var restOfString = str.substring(token.length());
                bestSoFar = findShortestSegmentation(restOfString, tokens, tokensSoFar, bestSoFar);
                tokensSoFar.removeLast(); // backtracking
            }
        }

        return bestSoFar;
    }

    private static List<List<String>> findAllSegmentations(String str,
                                                     List<String> tokens,
                                                     List<String> tokensSoFar,
                                                     List<List<String>> solutionsSoFar) {
        if (str.isEmpty()) {
            solutionsSoFar.add(List.copyOf(tokensSoFar)); // !
            return solutionsSoFar;
        }

        for (var token : tokens) {
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                var restOfString = str.substring(token.length());
                findAllSegmentations(restOfString, tokens, tokensSoFar, solutionsSoFar);
                tokensSoFar.removeLast(); // backtracking
            }
        }

        return solutionsSoFar;
    }

    private static List<String> findSegmentation(String str, List<String> tokens, List<String> tokensSoFar) {
        if (str.isEmpty()) return tokensSoFar;

        for (var token : tokens) {
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                var restOfString = str.substring(token.length());
                var solution = findSegmentation(restOfString, tokens, tokensSoFar);
                if (solution != null) {
                    return solution;
                }
                tokensSoFar.removeLast(); // backtracking
            }
        }

        return null;
    }
}
