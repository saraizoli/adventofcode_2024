package main.day06;

import main.utils.Day;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 extends Day<Integer> {
    private final List<String> codes;
    private final int h;
    private final int w;
    private final Point s;
    private Set<Point> visited;

    public Day06() {
//        codes = getReader().readAsStringList("day06_sample.txt");
        codes = getReader().readAsStringList(6);
        h = codes.size();
        w = codes.getFirst().length();
        Point start = null;
        for (int i = 0; i < h; i++) {
            if (codes.get(i).contains("^")) {
                start = new Point(codes.get(i).indexOf('^'), i);
                break;
            }
        }
        s = start;
        visited = new HashSet<>();
    }

    @Override
    public Integer getSolution1() {
        Point dir = Point.D;
        Point loc = s;
        visited = new HashSet<>();
        while (lookup(loc) != ' ') {
            visited.add(loc);
            Point next = loc.add(dir);
            if (lookup(next) == '#') {
                dir = dir.rotate(-1);
            } else {
                loc = next;
            }
        }
        return visited.size();
    }

    public boolean isLooping(Point obs) {
        PointAndDir curr = new PointAndDir(s,Point.D);
        Set<PointAndDir> vis = new HashSet<>();
        while (lookup(curr.p()) != ' ') {
            if(!vis.add(curr)){
                return true;
            }
            PointAndDir next = curr.step();
            if (obs.equals(next.p()) || lookup(next.p()) == '#') {
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


    private char lookup(Point p) {
        if (p.x() < 0 || p.x() >= w || p.y() < 0 || p.y() >= h) {
            return ' ';
        }
        return codes.get(p.y()).charAt(p.x());
    }
}
