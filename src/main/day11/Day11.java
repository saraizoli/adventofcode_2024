package main.day11;

import main.utils.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day<Long> {
    private final List<Integer> codes;
    private final Map<State, Long> memo;

    public Day11() {
//        codes = List.of(125, 17);
        codes = List.of(3028, 78, 973951, 5146801, 5, 0, 23533, 857);
        memo = new HashMap<>();
    }

    @Override
    public Long getSolution1() {
        return solve(25);
    }

    private long solve(int step) {
        memo.clear();
        long sum = 0;
        for (int i : codes) {
            sum += countStones(i, step);
        }
        return sum;
    }

    private long countStones(long num, int step) {
        if (step == 0) {
            return 1;
        }
        State s = new State(num, step);
        Long res = memo.get(s);
        if (res != null) {
            return res;
        }
        res = countStonesInner(num, step);
        memo.put(s, res);
        return res;
    }

    private long countStonesInner(long num, int step) {
        if (num == 0) {
            return countStones(1, step - 1);
        }
        String s = String.valueOf(num);
        if (s.length() % 2 == 0) {
            return countStones(Long.parseLong(s.substring(0, s.length() / 2)), step - 1) + countStones(Long.parseLong(s.substring(s.length() / 2)), step - 1);
        }
        return countStones(2024 * num, step - 1);
    }


    @Override
    public Long getSolution2() {
        return solve(75);
    }
}

record State(long num, int step) {
}