package oef05_uurrooster;

import java.util.*;

public class Oef05 {

    public record PlanningParameters(int maxSlots, int maxCoursesPerSlot) {}

    public interface IPlanning {
        int nbUsedSlots();
        Set<Course> coursesInSlot(int slot);
        PlanningParameters parameters();
    }

    public record Course(String code, Set<String> people) {}


    public static IPlanning createPlanning(List<Course> vakken, PlanningParameters parameters) {
        // TODO
        return null;
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
