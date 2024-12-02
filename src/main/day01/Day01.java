package main.day01;

import main.utils.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day01 extends Day<Integer> {
    private final List<Integer> left;
    private final List<Integer> right;

    public Day01() {
        List<String> codes = getReader().readAsStringList(1);
        left = new ArrayList<>();
        right = new ArrayList<>();
        codes.stream().map(s -> s.split("   ")).forEach(sa -> {
            left.add(Integer.parseInt(sa[0]));
            right.add(Integer.parseInt(sa[1]));
        });
    }

    @Override
    public Integer getSolution1() {
        Collections.sort(left);
        Collections.sort(right);
        return IntStream.range(0, left.size()).map(i -> Math.abs(left.get(i) - right.get(i))).sum();
    }

    @Override
    public Integer getSolution2() {
        Map<Integer, Long> counts = right.stream().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        return (int) left.stream().mapToLong(i -> i * counts.getOrDefault(i,0L)).sum();
    }
}
