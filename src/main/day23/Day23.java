package main.day23;

import main.utils.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Day<Integer> {
    private final Map<String, Set<String>> edges;
    private final Set<Set<String>> base;

    public Day23() {
//        List<String> raw = getReader().readAsStringList("day23_sample.txt");
        List<String> raw = getReader().readAsStringList(23);
        edges = new HashMap<>();
        base = new HashSet<>();
        for (String s : raw) {
            String[] t = s.split("-");
            base.add(Set.of(t[0], t[1]));
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

    private Set<Set<String>> nextSizeCliq(Set<Set<String>> prev) {
        Set<Set<String>> next = new HashSet<>();
        prev.parallelStream().forEach(existingCliq -> {
            Set<String> commonEdges = null;
            for (String cliqMember : existingCliq) {
                if (commonEdges == null) {
                    commonEdges = new HashSet<>(edges.get(cliqMember));
                } else {
                    commonEdges.retainAll(edges.get(cliqMember));
                }
            }
            commonEdges.forEach(e -> {
                Set<String> newCliq = new HashSet<>(existingCliq);
                newCliq.add(e);
                next.add(newCliq);
            });
        });
        return next;
    }


    @Override
    public Integer getSolution2() {
        Set<Set<String>> curr = base;
        while (curr.size() > 1) {
            curr = nextSizeCliq(curr);
        }
        String res = curr.stream().findFirst().orElseThrow().stream().sorted().collect(Collectors.joining(","));
        System.out.println(res);
        return 0;
    }
}
