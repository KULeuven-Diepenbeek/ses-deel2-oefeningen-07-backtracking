package oef05_uurrooster;

import java.util.*;
import java.util.stream.Collectors;

public class Oef05 {

    public record PlanningParameters(int maxSlots, int maxCoursesPerSlot) {}

    public interface IPlanning {
        int nbUsedSlots();
        Set<Course> coursesInSlot(int slot);
        PlanningParameters parameters();
    }

    public record Course(String code, Set<String> people) {
        public boolean overlappingPersonWith(Course other) {
            return !Collections.disjoint(this.people(), other.people());
            // return this.people().stream().anyMatch(p -> other.people().contains(p));
        }
    }

    static class Planning implements IPlanning {

        private final PlanningParameters parameters;
        private final Map<Course, Integer> geplandeVakken = new HashMap<>();

        public Planning(PlanningParameters parameters) {
            this.parameters = parameters;
        }

        @Override
        public int nbUsedSlots() {
            return (int)geplandeVakken.values().stream().distinct().count();
        }

        @Override
        public Set<Course> coursesInSlot(int slot) {
//            var result = new HashSet<Course>();
//            for (var course : geplandeVakken.keySet()) {
//                if (geplandeVakken.get(course) == slot) {
//                    result.add(course);
//                }
//            }
//            return result;
            return geplandeVakken.entrySet().stream()
                    .filter(e -> e.getValue() == slot)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
        }

        @Override
        public PlanningParameters parameters() {
            return parameters;
        }

        public void plan(Course vak, int slot) {
            geplandeVakken.put(vak, slot);
        }

        public void unplan(Course vak, int slot) {
            geplandeVakken.remove(vak);
        }

        public boolean canBePlannedInSlot(Course vak, int slot) {
            // is er nog plaats?
            if (coursesInSlot(slot).size() >= parameters.maxCoursesPerSlot()) return false;
            // geen dubbele boekingen van personen
            if (coursesInSlot(slot).stream().anyMatch(c -> c.overlappingPersonWith(vak))) return false;

            return true;
        }

        public Planning copy() {
            var copy = new Planning(parameters);
            copy.geplandeVakken.putAll(this.geplandeVakken);
            return copy;
        }

        public boolean isBetterThan(Planning other) {
            if (this.nbUsedSlots() < other.nbUsedSlots()) return true;
            if (this.nbUsedSlots() > other.nbUsedSlots()) return false;
            // even veel slots
            if (this.lastSlot() < other.lastSlot()) return true;

            return false;
        }

        private int lastSlot() {
            return geplandeVakken.values().stream().mapToInt(i -> i).max().orElse(0);
        }
    }

    public static IPlanning createPlanning(List<Course> vakken, PlanningParameters parameters) {
        return createPlanning(vakken, parameters, new Planning(parameters), null);
    }

    private static Planning createPlanning(List<Course> tePlannenVakken, PlanningParameters parameters, Planning planningSoFar, Planning bestSoFar) {
        if (tePlannenVakken.isEmpty()) {
            if (bestSoFar == null || planningSoFar.isBetterThan(bestSoFar)) {
                return planningSoFar.copy();
            }
            return bestSoFar;
        }

        if (bestSoFar != null && bestSoFar.isBetterThan(planningSoFar)) return bestSoFar;

        var vak = tePlannenVakken.getFirst();
        var rest = tePlannenVakken.subList(1, tePlannenVakken.size());

        for (var slot = 1; slot <= parameters.maxSlots(); slot++) {
            if (planningSoFar.canBePlannedInSlot(vak, slot)) {
                planningSoFar.plan(vak, slot);
                bestSoFar = createPlanning(rest, parameters, planningSoFar, bestSoFar);
                planningSoFar.unplan(vak, slot);
            }
        }

        return bestSoFar;
    }

    static void main() {
        var vakken = List.of(new Course("A", Set.of("p1")),
                new Course("B", Set.of("p1", "p2")),
                new Course("C", Set.of("p2", "p3")),
                new Course("D", Set.of("p3")));
        var result = createPlanning(vakken, new PlanningParameters(10, 3));
        IO.println(result);
    }
}
