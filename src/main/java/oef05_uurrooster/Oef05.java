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

    public record Course(String code, Set<String> people) {}


    public static IPlanning createPlanning(List<Course> vakken, PlanningParameters parameters) {
        return createPlanning(new ArrayList<>(vakken), new Planning(parameters), null);
    }

    private static Planning createPlanning(List<Course> coursesToPlan, Planning planningSoFar, Planning bestSoFar) {
        if (coursesToPlan.isEmpty()) {
            if (bestSoFar == null || planningSoFar.nbUsedSlots() < bestSoFar.nbUsedSlots())
                return planningSoFar.clone();
            return bestSoFar;
        }

        // if we don't have enough room, abort (impossible)
        if (planningSoFar.nbPlannableCourses() < coursesToPlan.size()) return bestSoFar;
        // if we're already using more slots than the best one, abort
        if (bestSoFar != null && planningSoFar.nbUsedSlots() >= bestSoFar.nbUsedSlots()) return bestSoFar;

        for (var course : coursesToPlan) {
            for (var slot = 1; slot <= planningSoFar.lastSlot() + 1; slot++) {
                if (planningSoFar.canBePlannedInSlot(course, slot)) {
                    var geplandVak = new PlannedCourse(course, slot);
                    planningSoFar.add(geplandVak);
                    bestSoFar = createPlanning(coursesToPlan.subList(1, coursesToPlan.size()), planningSoFar, bestSoFar);
                    planningSoFar.remove(geplandVak);
                }
            }
        }

        return bestSoFar;
    }



    record PlannedCourse(Course course, int slot) {
        @Override
        public String toString() {
            return slot + ": " + course;
        }
    }

    static class Planning implements Cloneable, IPlanning {
        private final Set<PlannedCourse> plannedCourses = new HashSet<>();
        private final PlanningParameters parameters;

        public Planning(PlanningParameters parameters) {
            this.parameters = parameters;
        }

        protected Planning(PlanningParameters parameters, Set<PlannedCourse> plannedCourses) {
            this.parameters = parameters;
            this.plannedCourses.addAll(plannedCourses);
        }

        @Override
        public Set<Course> coursesInSlot(int slot) {
            return plannedCourses.stream()
                    .filter(pc -> pc.slot() == slot)
                    .map(PlannedCourse::course)
                    .collect(Collectors.toSet());
        }

        @Override
        public int nbUsedSlots() {
            return (int) plannedCourses.stream().mapToInt(PlannedCourse::slot).distinct().count();
        }

        @Override
        public PlanningParameters parameters() {
            return parameters;
        }

        public int nbPlannedCourses() {
            return plannedCourses.size();
        }

        public int nbPlannableCourses() {
            return parameters.maxSlots() * parameters.maxCoursesPerSlot() - nbPlannedCourses();
        }

        private boolean isRoomAvailable(int slot) {
            return coursesInSlot(slot).size() < parameters.maxCoursesPerSlot();
        }

        private boolean allPeopleAvailable(Set<String> people, int slot) {
            return coursesInSlot(slot).stream().noneMatch(p -> p.people().stream().anyMatch(people::contains));
        }

        @Override
        public Planning clone() {
            return new Planning(parameters, plannedCourses);
        }

        boolean canBePlannedInSlot(Course course, int slot) {
            return 1 <= slot && slot <= parameters.maxSlots() && isRoomAvailable(slot) && allPeopleAvailable(course.people(), slot);
        }

        @Override
        public String toString() {
            return plannedCourses.stream()
                    .sorted(Comparator.comparing(PlannedCourse::slot))
                    .map(PlannedCourse::toString)
                    .collect(Collectors.joining("\n"));
        }

        public int lastSlot() {
            return plannedCourses.stream().mapToInt(PlannedCourse::slot).max().orElse(0);
        }

        public void add(PlannedCourse plannedCourse) {
            plannedCourses.add(plannedCourse);
        }

        public void remove(PlannedCourse plannedCourse) {
            plannedCourses.remove(plannedCourse);
        }

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
