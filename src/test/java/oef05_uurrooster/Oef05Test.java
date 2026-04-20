package oef05_uurrooster;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static oef05_uurrooster.PlanningAssert.assertThat;


public class Oef05Test {

    @Test
    public void test_empty_planning() {
        var courses = List.<Oef05.Course>of();
        Oef05.IPlanning solution = Oef05.createPlanning(courses, new Oef05.PlanningParameters(10, 3));

        assertThat(solution)
                .containsAllCourses(courses)
                .satisfiesAllConstraints()
                .usesNSlots(0);
    }

    @Test
    public void test_all_in_one_slot() {
        var courses = List.of(new Oef05.Course("A", Set.of("p1")),
                new Oef05.Course("B", Set.of("p2")),
                new Oef05.Course("C", Set.of("p3")));
        var result = Oef05.createPlanning(courses, new Oef05.PlanningParameters(10, 3));
        assertThat(result)
                .containsAllCourses(courses)
                .satisfiesAllConstraints()
                .usesNSlots(1);
    }

    @Test
    public void test_respects_max_courses_per_slot() {
        var courses = List.of(new Oef05.Course("A", Set.of("p1")),
                new Oef05.Course("B", Set.of("p2")),
                new Oef05.Course("C", Set.of("p3")),
                new Oef05.Course("D", Set.of("p4")));
        var result = Oef05.createPlanning(courses, new Oef05.PlanningParameters(10, 3));
        assertThat(result)
                .containsAllCourses(courses)
                .satisfiesAllConstraints()
                .usesNSlots(2);
    }

    @Test
    public void test_no_conflicts() {
        var courses = List.of(new Oef05.Course("A", Set.of("p1", "p2")),
                new Oef05.Course("B", Set.of("p1", "p3")),
                new Oef05.Course("C", Set.of("p1", "p4")),
                new Oef05.Course("D", Set.of("p1", "p5")));
        var result = Oef05.createPlanning(courses, new Oef05.PlanningParameters(10, 3));
        assertThat(result)
                .containsAllCourses(courses)
                .satisfiesAllConstraints()
                .usesNSlots(4);
    }

    @Test
    public void test_optimal() {
        var courses = List.of(new Oef05.Course("A", Set.of("p1")),
                new Oef05.Course("B", Set.of("p1", "p2")),
                new Oef05.Course("C", Set.of("p2", "p3")),
                new Oef05.Course("D", Set.of("p3")));
        var result = Oef05.createPlanning(courses, new Oef05.PlanningParameters(10, 3));
        assertThat(result)
                .containsAllCourses(courses)
                .satisfiesAllConstraints()
                .usesNSlots(2);
    }

    @Test
    public void test_no_solution() {
        var courses = List.of(new Oef05.Course("A", Set.of("p1")),
                new Oef05.Course("B", Set.of("p1")),
                new Oef05.Course("C", Set.of("p1")),
                new Oef05.Course("D", Set.of("p1")));
        var result = Oef05.createPlanning(courses, new Oef05.PlanningParameters(3, 2));
        assertThat(result)
                .isNull();
    }

}

class PlanningAssert extends AbstractAssert<PlanningAssert, Oef05.IPlanning> {

    public PlanningAssert(Oef05.IPlanning actual) {
        super(actual, PlanningAssert.class);
    }

    public static PlanningAssert assertThat(Oef05.IPlanning actual) {
        return new PlanningAssert(actual);
    }

    public PlanningAssert satisfiesAllConstraints() {
        isNotNull();

        // valid slot numbers
        if (!allSlots(actual)
                .allMatch(slot -> slot >= 1 && slot <= actual.parameters().maxSlots()))
            failWithMessage("Expected planning to be valid, but it used slot numbers outside the valid range (1-%d)", actual.parameters().maxSlots());

        // not schedule same person twice in one slot
        allSlots(actual)
                .filter(this::somePersonScheduledMoreThanOnce)
                .forEach(slot -> failWithMessage("Expected planning to be valid, but it scheduled the same person more than once in slot %d", slot));

        // respect maximum courses per slot (room allocation)
        allSlots(actual)
                .filter(slot -> actual.coursesInSlot(slot).size() > actual.parameters().maxCoursesPerSlot())
                .forEach(slot -> failWithMessage("Expected planning to be valid, but it scheduled more than %d courses in slot %d", actual.parameters().maxCoursesPerSlot(), slot));

        // respect maximum number of slots
        if (actual.nbUsedSlots() > actual.parameters().maxSlots())
            failWithMessage("Expected planning to be valid, but it used %d slots while the maximum was %d", actual.nbUsedSlots(), actual.parameters().maxSlots());

        return this;
    }

    private boolean somePersonScheduledMoreThanOnce(Integer slot) {
        return !allPairs(actual.coursesInSlot(slot).stream().toList(), (c1, c2) -> Collections.disjoint(c1.people(), c2.people()));
    }

    public PlanningAssert usesNSlots(int n) {
        isNotNull();

        if (n != actual.nbUsedSlots()) {
            failWithMessage("Expected planning to use %d slots, but it used %d", n, actual.nbUsedSlots());
        }

        return this;
    }

    public PlanningAssert containsAllCourses(Collection<Oef05.Course> courses) {
        isNotNull();

        org.assertj.core.api.Assertions.assertThat(allPlannedCourses(actual))
                .as("planned courses")
                .containsExactlyInAnyOrderElementsOf(courses);

        return this;
    }


    private static Set<Oef05.Course> allPlannedCourses(Oef05.IPlanning planning) {
        return allSlots(planning).flatMap(s -> planning.coursesInSlot(s).stream()).collect(Collectors.toSet());
    }

    private static Stream<Integer> allSlots(Oef05.IPlanning planning) {
        return IntStream.rangeClosed(1, planning.nbUsedSlots()).boxed();
    }

    private static <T> boolean allPairs(List<T> list, BiPredicate<T, T> pred) {
        return IntStream.range(0, list.size())
                .allMatch(i ->
                        IntStream.range(i + 1, list.size())
                                .allMatch(j -> pred.test(list.get(i), list.get(j)))
                );
    }
}
