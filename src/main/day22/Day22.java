package main.day22;

import main.utils.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 extends Day<Long> {
    private final List<Long> codes;

    public Day22() {
//        List<String> raw = getReader().readAsStringList("day22_sample.txt");
        List<String> raw = getReader().readAsStringList(22);
        codes = raw.stream().map(Long::parseLong).toList();
    }

    private long op(long l) {
        l = ((l << 6) ^ l) & 16777215;
        l = ((l >> 5) ^ l); //& 16777215,
        return ((l << 11) ^ l) & 16777215;
    }

    private long generate(long seed, int steps) {
        if (steps == 0) {
            return seed;
        }
        return op(generate(seed, steps - 1));
    }

    @Override
    public Long getSolution1() {
        return codes.stream().mapToLong(s -> generate(s, 2000)).sum();
    }

    @Override
    public Long getSolution2() {
        Map<Integer, Integer> sums = new HashMap<>();
        for (long code : codes) {
            long c = code;
            int p = (int) c % 10;
            Map<Integer, Integer> currMap = new HashMap<>();
            int currEncode = 1;
            for (int i = 0; i < 2000; i++) {
                c = generate(c, 1);
                int np = (int) c % 10;
                currEncode = (currEncode * 100 + 10 + np - p) % 100000000;//this keeps the last 4 numbers in an always unique combination
                p = np;
                if (i > 2) {
                    currMap.putIfAbsent(currEncode, np);
                }
            }
            currMap.forEach((k, v) -> sums.merge(k, v, Integer::sum));
        }
        return sums.values().stream().max(Integer::compareTo).map(i -> (long) i).orElseThrow();
    }
}