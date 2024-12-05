package main.day05;

import main.utils.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

//using assumptions that are not true in general - every pair of numbers has a defined order (and it is transitive)
public class Day05 extends Day<Integer> {
    private final Set<String> order;
    private final List<List<String>> codes;


    public Day05() {
        List<String> lines = getReader().readAsStringList(5);
        int i = lines.indexOf("");
        order = new HashSet<>(lines.subList(0, i));
        codes = lines.stream().skip(i + 1).map(l -> Arrays.asList(l.split(","))).toList();
    }

    @Override
    public Integer getSolution1() {
        return codes.stream().filter(this::isOrdered).mapToInt(l -> Integer.parseInt(l.get(l.size() / 2))).sum();
    }

    private boolean isOrdered(List<String> l) {
        for (int i = 0; i < l.size() - 1; i++) {
            if (order.contains(l.get(i + 1) + "|" + l.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer getSolution2() {
        return codes.stream().filter(Predicate.not(this::isOrdered)).map(this::order).mapToInt(l -> Integer.parseInt(l.get(l.size() / 2))).sum();

    }

    private List<String> order(List<String> list) {
        return list.stream().sorted((a, b) -> order.contains(a + "|" + b) ? 1 : order.contains(b + "|" + a) ? -1 : 0).toList();
    }
}
