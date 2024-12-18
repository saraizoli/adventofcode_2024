package main.day08;

import main.utils.Day;
import main.utils.Point;

import java.util.*;

public class Day08 extends Day<Long> {
    private final Map<Character, Set<Point>> nodes;
    private final Point corner;

    public Day08() {
//        List<String> raw = getReader().readAsStringList("day08_sample.txt");
        List<String> raw = getReader().readAsStringList(8);
        nodes = new HashMap<>();
        int h = raw.size();
        int w = raw.getFirst().length();
        corner = new Point(w, h);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                char c = raw.get(i).charAt(j);
                if (c != '.') {
                    nodes.computeIfAbsent(c, HashSet::new).add(new Point(j, i));
                }
            }
        }
    }

    @Override
    public Long getSolution1() {
        return nodes.values().stream().map(this::getAntinodes1).flatMap(Set::stream).distinct().count();
    }

    Set<Point> getAntinodes1(Set<Point> points) {
        Set<Point> antinodes = new HashSet<>();
        for (Point p : points) {
            for (Point q : points) {
                if (p.equals(q)) {
                    continue;
                }
                Point antinode = p.add(p.add(q.neg()));
                if (antinode.isInRect(corner)) {
                    antinodes.add(antinode);
                }
            }
        }
        return antinodes;
    }

    @Override
    public Long getSolution2() {
        return nodes.values().stream().map(this::getAntinodes2).flatMap(Set::stream).distinct().count();
    }

    Set<Point> getAntinodes2(Set<Point> points) {
        //by the task description this could be more complicated if any diff vector had gcd(dx, dy) > 1, but this is never the case
        //if it was we'd need to do diff = diff / gcd
        Set<Point> antinodes = new HashSet<>();
        for (Point p : points) {
            for (Point q : points) {
                if (p.equals(q)) {
                    continue;
                }
                Point diff = p.add(q.neg());
                Point r = p;
                do {
                    antinodes.add(r);
                    r = r.add(diff);
                } while (r.isInRect(corner));
            }
        }
        return antinodes;
    }
}
