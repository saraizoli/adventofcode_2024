package main.day19;

import main.utils.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 extends Day<Long> {
    private final List<String> patterns;
    private final List<String> designs;
    private final Map<String, Long> memo;

    public Day19() {
//        List<String> raw = getReader().readAsStringList("day19_sample.txt");
        List<String> raw = getReader().readAsStringList(19);
        patterns = Arrays.asList(raw.getFirst().split(", "));
        designs = raw.subList(2, raw.size());
        memo = new HashMap<>();
    }

    @Override
    public Long getSolution1() {
        memo.clear();
        return designs.stream().filter(d -> possible(d) > 0).count();
    }

    long possible(String design) {
        if (design.isEmpty()) {
            return 1L;
        }
        if (memo.containsKey(design)) {
            return memo.get(design);
        }
        long poss = patterns.stream().filter(design::startsWith).mapToLong(p -> possible(design.substring(p.length()))).sum();
        memo.put(design, poss);
        return poss;
    }

    @Override
    public Long getSolution2() {
        memo.clear();
        return designs.stream().mapToLong(this::possible).sum();
    }
}
