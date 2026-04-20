package oef06_samentrekking;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Oef06Test {

    @Nested
    class KortsteSamentrekking {

        @Test
        public void lege_invoer() {
            var result = Oef06.kortsteSamentrekking(List.of());
            assertThat(result).isEqualTo("");
        }

        @Test
        public void slechts_een_woord() {
            var result = Oef06.kortsteSamentrekking(List.of("hallo"));
            assertThat(result).isEqualTo("hallo");
        }

        @Test
        public void identieke_woorden() {
            var result = Oef06.kortsteSamentrekking(List.of("hallo", "hallo"));
            assertThat(result).isEqualTo("hallo");
        }


        @Test
        public void voorbeeld1() {
            var result = Oef06.kortsteSamentrekking(List.of("banaan", "ananas"));
            assertThat(result).isEqualTo("banaananas");
        }

        @Test
        public void voorbeeld2() {
            var result = Oef06.kortsteSamentrekking(List.of("besturend", "declaratiesysteem", "deelgemeente", "gemeentebesturen", "merendeel", "programmeren", "sturende", "urendeclaraties"));
            assertThat(result).isEqualTo("programmerendeelgemeentebesturendeclaratiesysteem");
        }

        @Test
        public void voorbeeld3() {
            var result = Oef06.kortsteSamentrekking(List.of("samentrekking", "trekkingsdata", "datavoorziening", "voorzieningsfonds", "fondsmanager", "managersfuncties", "functiesysteem", "systeemdata"));
            assertThat(result).isEqualTo("samentrekkingsdatavoorzieningsfondsmanagersfunctiesysteemdata");
        }

        @Test
        public void greedy_oplossing_fout() {
            var result = Oef06.kortsteSamentrekking(List.of("bcda", "abcd"));
            assertThat(result).hasSize(5);
        }

    }

    @Nested
    class LangsteSamentrekking {
        @Test
        public void lege_invoer() {
            var result = Oef06.langsteeSamentrekking(List.of());
            assertThat(result).isEqualTo("");
        }

        @Test
        public void slechts_een_woord() {
            var result = Oef06.langsteeSamentrekking(List.of("hallo"));
            assertThat(result).isEqualTo("hallo");
        }

        @Test
        public void identieke_woorden() {
            var result = Oef06.langsteeSamentrekking(List.of("hallo", "hallo"));
            assertThat(result).isEqualTo("hallo");
        }


        @Test
        public void voorbeeld1() {
            var result = Oef06.langsteeSamentrekking(List.of("banaan", "ananas"));
            assertThat(result).isEqualTo("banaananas");
        }

        @Test
        public void voorbeeld2() {
            var result = Oef06.langsteeSamentrekking(List.of("besturend", "declaratiesysteem", "deelgemeente", "gemeentebesturen", "merendeel", "programmeren", "sturende", "urendeclaraties"));
            assertThat(result).isEqualTo("programmerendeelgemeentebesturendeclaratiesturendeclaratiesysteem");
        }

        @Test
        public void voorbeeld3() {
            var result = Oef06.langsteeSamentrekking(List.of("samentrekking", "trekkingsdata", "datavoorziening", "voorzieningsfonds", "fondsmanager", "managersfuncties", "functiesysteem", "systeemdata"));
            assertThat(result).isEqualTo("functiesysteemdatavoorzieningsfondsmanagersfunctiesamentrekkingsdata");
        }

        @Test
        public void greedy_oplossing_fout() {
            var result = Oef06.langsteeSamentrekking(List.of("abcd", "bcda"));
            assertThat(result).hasSize(7);
        }

    }
}
