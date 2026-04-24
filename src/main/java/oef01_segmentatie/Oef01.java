package oef01_segmentatie;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Oef01 {

    public static List<String> segmentAnySolution(String str, List<String> tokens) {
        return segmentAnySolution(str, tokens, new ArrayList<>());
    }

    private static List<String> segmentAnySolution(String str, List<String> tokens, List<String> tokensSoFar) {
        if (str.isEmpty()) return tokensSoFar;

        for (var token : tokens) {
            if (str.startsWith(token)) {
                tokensSoFar.add(token);
                var newTokens = new ArrayList<>(tokens);
                newTokens.remove(token);
                var restOfString = str.substring(token.length());
                var solution = segmentAnySolution(restOfString, newTokens, tokensSoFar);
                if (solution != null) {
                    return solution;
                }
                tokensSoFar.removeLast(); // backtracking
            }
        }

        return null;
    }


    public static Set<List<String>> segmentAllSolutions(String str, List<String> tokens) {
        // TODO
        return null;
    }


    public static List<String> segmentOptimalSolution(String str, List<String> tokens) {
        // TODO
        return null;
    }

}
