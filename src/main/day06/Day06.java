package main.day06;

import main.utils.Day;
import main.utils.Grid;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.HashSet;
import java.util.Set;

public class Day06 extends Day<Integer> {
    private final Grid codes;
    private final Point s;
    private Set<Point> visited;

    public Day06() {
//        codes = new Grid(getReader().readAsStringList("day06_sample.txt"));
        codes = new Grid(getReader().readAsStringList(6));
        s = codes.findOne('^');
        visited = new HashSet<>();
    }

    @Override
    public Integer getSolution1() {
        Point dir = Point.D;
        Point loc = s;
        visited = new HashSet<>();
        while (codes.lookup(loc) != ' ') {
            visited.add(loc);
            Point next = loc.add(dir);
            if (codes.lookup(next) == '#') {
                dir = dir.rotate(-1);
            } else {
                loc = next;
            }
        }
        return visited.size();
    }

    public boolean isLooping(Point obs) {
        PointAndDir curr = new PointAndDir(s, Point.D);
        Set<PointAndDir> vis = new HashSet<>();
        while (codes.lookup(curr.p()) != ' ') {
            if (!vis.add(curr)) {
                return true;
            }
            PointAndDir next = curr.step();
            if (obs.equals(next.p()) || codes.lookup(next.p()) == '#') {
                curr = new PointAndDir(curr.p(), curr.d().rotate(-1));
            } else {
                curr = next;
            }
        }
        return false;
    }

    @Override
    public Integer getSolution2() {
        //todo anything smarter?
        return (int) visited.parallelStream().filter(this::isLooping).count();
    }
}
