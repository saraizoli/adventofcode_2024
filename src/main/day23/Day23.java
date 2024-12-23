package main.day23;

import main.utils.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Day<Integer> {
    private final Map<String, Set<String>> edges;

    public Day23() {
//        List<String> raw = getReader().readAsStringList("day23_sample.txt");
        List<String> raw = getReader().readAsStringList(23);
        edges = new HashMap<>();
        for (String s : raw) {
            String[] t = s.split("-");
            edges.computeIfAbsent(t[0], k -> new HashSet<>()).add(t[1]);
            edges.computeIfAbsent(t[1], k -> new HashSet<>()).add(t[0]);
        }
    }

    @Override
    public Integer getSolution1() {
        Set<Set<String>> cliqs = new HashSet<>();
        edges.forEach((bp, neighbs) -> {
            if (bp.charAt(0) != 't') {
                return;
            }
            for (String n : neighbs) {
                HashSet<String> base = new HashSet<>(neighbs);

                base.retainAll(edges.get(n));
                base.forEach(p -> cliqs.add(Set.of(bp, n, p)));
            }
        });
        return cliqs.size();
    }

    //https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm#With_pivoting around random vertex
    //seems like input is a lot of 12 clicks with 1 13 click - this pivot makes all recursive calls run on 1 element...
    private Set<String> getBiggestCliq(Set<String> unused, Set<String> cliq) {
        if (unused.isEmpty()) {
            return cliq;
        }
//        String pivot = unused.stream().max(Comparator.comparingInt(v -> edges.get(v).size())).orElseThrow();
        String pivot = unused.stream().findFirst().orElseThrow();
        return (unused.size() > 20 ? unused.parallelStream() : unused.stream())
                .filter(v -> !edges.get(pivot).contains(v))
                .map(v -> {
                    Set<String> nextUnused = new HashSet<>(unused);
                    Set<String> nextCliq = new HashSet<>(cliq);
                    nextUnused.retainAll(edges.get(v));
                    nextCliq.add(v);
                    return getBiggestCliq(nextUnused, nextCliq);
                }).max(Comparator.comparingInt(Set::size)).orElseThrow();
    }


    @Override
    public Integer getSolution2() {
        String res = getBiggestCliq(edges.keySet(), Collections.emptySet()).stream().sorted().collect(Collectors.joining(","));
        System.out.println(res);
        return 0;
    }
}
