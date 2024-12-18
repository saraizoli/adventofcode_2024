package main.day12;

import main.utils.Day;
import main.utils.Grid;
import main.utils.Point;

import java.util.*;

public class Day12 extends Day<Integer> {
    private final Grid grid;
    private final Set<Point> visited;

    public Day12() {
//        grid = new Grid(getReader().readAsStringList("day12_sample.txt"));
        grid = new Grid(getReader().readAsStringList(12));
        visited = new HashSet<>();
    }

    @Override
    public Integer getSolution1() {
        visited.clear();
        int sum = 0;
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                Point p = new Point(i, j);
                if (!visited.contains(p)) {
                    sum += price(p);
                }
            }
        }
        return sum;
    }

    private int price(Point head) {
        //bfs
        int area = 0;
        int perim = 0;
        char cv = grid.lookup(head);
        Queue<Point> q = new LinkedList<>();
        q.add(head);
        while (!q.isEmpty()) {
            Point curr = q.poll();
            if (!visited.add(curr)) {
                continue;
            }
            area++;
            List<Point> neighboursInSamePlot = curr.neighbours().filter(n -> grid.lookup(n) == cv).toList();
            perim += 4 - neighboursInSamePlot.size();
            q.addAll(neighboursInSamePlot);
        }
        return area * perim;
    }

    @Override
    public Integer getSolution2() {
        visited.clear();
        int sum = 0;
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                Point p = new Point(i, j);
                if (!visited.contains(p)) {
                    sum += priceBulk(p);
                }
            }
        }
        return sum;
    }

    private int priceBulk(Point head) {
        //bfs
        int area = 0;
        int perim = 0;
        char cv = grid.lookup(head);
        Queue<Point> q = new LinkedList<>();
        q.add(head);
        while (!q.isEmpty()) {
            Point curr = q.poll();
            if (!visited.add(curr)) {
                continue;
            }
            area++;
            perim += countCorners(cv, curr); //number of sides == number of corners
            curr.neighbours().filter(n -> grid.lookup(n) == cv).forEach(q::add);
        }
        return area * perim;
    }

    private int countCorners(char cv, Point curr) {
        /* corners are all rotations of the following setup
        AX   AX
        BA   AA
         */
        return (int) Arrays.stream(Point.DIRS).filter(d -> {
            Point r = d.rotate(1);
            char dv = grid.lookup(curr.add(d));
            char rv = grid.lookup(curr.add(r));
            char drv = grid.lookup(curr.add(d.add(r)));
            return (drv != cv && dv == cv && rv == cv) || (dv != cv && rv != cv);
        }).count();
    }
}