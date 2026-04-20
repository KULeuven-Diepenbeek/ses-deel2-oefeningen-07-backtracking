package oef06_samentrekking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class Oef06 {
    /**
     * Deze oplossing demonstreert hoe get gebruik van een (bi)predicaat code-duplicatie kan voorkomen.
     * Zowel de kortste als de langste samentrekking zijn gebaseerd op hetzelfde algoritme;
     * enkel het selecteren van de beste samentrekking is afhankelijk van een gegeven criterium.
    */

    public static String kortsteSamentrekking(List<String> woorden) {
        return samentrekking(new ArrayList<>(woorden), "", null, (s1, s2) -> s1.length() < s2.length());
    }

    public static String langsteSamentrekking(List<String> woorden) {
        return samentrekking(new ArrayList<>(woorden), "", null, (s1, s2) -> s1.length() > s2.length());
    }

    private static String samentrekking(List<String> teGebruikenWoorden, String samentrekking, String beste, BiPredicate<String, String> eersteBeterDanTweede) {
        if (teGebruikenWoorden.isEmpty()) {
            if (beste == null || eersteBeterDanTweede.test(samentrekking, beste))
                return samentrekking;
            return beste;
        }

        for (int i = 0; i < teGebruikenWoorden.size(); i++) {
            var volgendWoord = teGebruikenWoorden.get(i);
            var overlap = langsteOverlap(samentrekking, volgendWoord);
            if (samentrekking.isEmpty() || !overlap.isEmpty()) {
                var volgendZonderOverlap = volgendWoord.substring(overlap.length());
                teGebruikenWoorden.remove(i);
                beste = samentrekking(teGebruikenWoorden, samentrekking + volgendZonderOverlap, beste, eersteBeterDanTweede);
                teGebruikenWoorden.add(i, volgendWoord);
            }
        }

        return beste;
    }

    private static String langsteOverlap(String first, String second) {
        for (int len = second.length(); len > 0 && len <= first.length(); len--) {
            var overlap = second.substring(0, len);
            if (first.endsWith(overlap)) {
                return overlap;
            }
        }
        return "";
    }

}
