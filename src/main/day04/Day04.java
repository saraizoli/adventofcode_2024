package main.day04;

import main.utils.Day;
import main.utils.Grid;
import main.utils.Point;

import java.util.stream.Stream;

public class Day04 extends Day<Integer> {
    private final Grid codes;

    public Day04() {
        codes = new Grid(getReader().readAsStringList(4));
    }

    @Override
    public Integer getSolution1() {
        int s = 0;
        for (int y = 0; y < codes.getHeight(); y++) {
            for (int x = 0; x < codes.getWidth(); x++) {
                s += countXMAS(x, y);
            }
        }
        return s;
    }

    private int countXMAS(int x, int y) {
        Point p = new Point(x, y);
        if (codes.lookup(p) != 'X') {
            return 0;
        }
        return (int) Stream.of(Point.DIRS_ALL).filter(d -> codes.lookup(p.add(d)) == 'M' && codes.lookup(p.add(d.mult(2))) == 'A' && codes.lookup(p.add(d.mult(3))) == 'S').count();
    }


    @Override
    public Integer getSolution2() {
        int s = 0;
        for (int y = 0; y < codes.getHeight(); y++) {
            for (int x = 0; x < codes.getWidth(); x++) {
                s += isCrossMAS(x, y) ? 1 : 0;
            }
        }
        return s;
    }


    private boolean isCrossMAS(int x, int y) {
        Point p = new Point(x, y);
        if (codes.lookup(p) != 'A') {
            return false;
        }
        char dl = codes.lookup(p.add(Point.DL));
        char dr = codes.lookup(p.add(Point.DR));
        char ul = codes.lookup(p.add(Point.UL));
        char ur = codes.lookup(p.add(Point.UR));
        return ((dl == 'M' && ur == 'S') || (dl == 'S' && ur == 'M')) && ((dr == 'M' && ul == 'S') || (dr == 'S' && ul == 'M'));
    }
}
