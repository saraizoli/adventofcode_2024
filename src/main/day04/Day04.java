package main.day04;

import main.utils.Day;
import main.utils.Point;

import java.util.List;
import java.util.stream.Stream;

public class Day04 extends Day<Integer> {
    private final List<String> codes;
    private final int h;
    private final int w;

    public Day04() {
        codes = getReader().readAsStringList(4);
        h = codes.size();
        w = codes.getFirst().length();
    }

    @Override
    public Integer getSolution1() {
        int s = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                s += countXMAS(x, y);
            }
        }
        return s;
    }

    private int countXMAS(int x, int y) {
        Point p = new Point(x, y);
        if (lookup(p) != 'X') {
            return 0;
        }
        return (int) Stream.of(Point.DIRS_ALL).filter(d -> lookup(p.add(d)) == 'M' && lookup(p.add(d.mult(2))) == 'A' && lookup(p.add(d.mult(3))) == 'S').count();
    }


    @Override
    public Integer getSolution2() {
        int s = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                s += isCrossMAS(x, y) ? 1 : 0;
            }
        }
        return s;
    }


    private boolean isCrossMAS(int x, int y) {
        Point p = new Point(x, y);
        if (lookup(p) != 'A') {
            return false;
        }
        char dl = lookup(p.add(Point.DL));
        char dr = lookup(p.add(Point.DR));
        char ul = lookup(p.add(Point.UL));
        char ur = lookup(p.add(Point.UR));
        return ((dl == 'M' && ur == 'S') || (dl == 'S' && ur == 'M')) && ((dr == 'M' && ul == 'S') || (dr == 'S' && ul == 'M'));
    }

    private char lookup(Point p) {
        if (p.x() < 0 || p.x() >= w || p.y() < 0 || p.y() >= h) {
            return ' ';
        }
        return codes.get(p.y()).charAt(p.x());
    }
}
