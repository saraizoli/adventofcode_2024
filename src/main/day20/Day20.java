package main.day20;

import main.utils.Day;
import main.utils.Grid;
import main.utils.Point;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 extends Day<Long> {
    private final Grid grid;
    private final Map<Point, Integer> diffS;
    private final Map<Point, Integer> diffE;
    private final Point s;
    private final Point e;

    public Day20() {
//        grid = new Grid(getReader().readAsStringList("day20_sample.txt"));
        grid = new Grid(getReader().readAsStringList(20));
        s = grid.findOne('S');
        e = grid.findOne('E');
        diffS = new HashMap<>();
        diffE = new HashMap<>();
    }

    private void setup() {
        diffS.clear();
        diffE.clear();
        diffS.putAll(fullBfs(s));
        diffE.putAll(fullBfs(e));
    }

    @Override
    public Long getSolution1() {
        setup();
        return cheatCount(2, 100);
    }

    private long cheatCount(int jumpSize, int timeThreshold) {
        int base = diffS.get(e);
        Set<Point> jumps = IntStream.rangeClosed(-1 * jumpSize, jumpSize).mapToObj(x -> IntStream.rangeClosed(-1 * jumpSize, jumpSize) //[-jumpSize, jumpSize] x [-jumpSize, jumpSize] square
                .mapToObj(y -> new Point(x, y))).flatMap(p -> p).filter(p -> Point.O.dist1(p) <= jumpSize).collect(Collectors.toSet()); //middle rhombus of it
//        Map<Integer, Long> cheats = diffS.keySet().stream()
//                .flatMap(b -> jumps.stream().map(b::add).filter(diffE.keySet()::contains).map(j -> new CheatTime(new Cheat(b, j), base - (diffS.get(b) + b.dist1(j) + diffE.get(j)))))
//                .filter(ct -> ct.t() >= timeThreshold)
//                .collect(Collectors.groupingBy(CheatTime::t, Collectors.counting()));
//        return cheats.values().stream().mapToLong(l->l).sum();
        return diffS.keySet().stream()
                .flatMap(b -> jumps.stream().map(b::add).filter(diffE.keySet()::contains).map(j -> base - (diffS.get(b) + b.dist1(j) + diffE.get(j))))
                .filter(t -> t >= timeThreshold)
                .count();
    }

    private Map<Point, Integer> fullBfs(Point head) {
        Map<Point, Integer> diff = new HashMap<>();
        Queue<Point> q = new LinkedList<>();
        q.add(head);
        diff.put(head, 0);
        while (!q.isEmpty()) {
            Point curr = q.poll();
            int d = diff.get(curr);
            curr.neighbours()
                    .filter(n -> grid.lookupWithoutBoundsCheck(n) != '#')
                    .filter(n -> !diff.containsKey(n))
                    .forEach(n -> {
                        diff.put(n, d + 1);
                        q.add(n);
                    });
        }
        return diff;
    }

    @Override
    public Long getSolution2() {
        return cheatCount(20, 100);
    }
}

record Cheat(Point f, Point t) {
}

record CheatTime(Cheat c, int t) {
}