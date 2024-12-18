package main.day10;

import main.utils.Day;
import main.utils.Grid;
import main.utils.Point;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Day10 extends Day<Integer> {
    private final Grid grid;


    public Day10() {
//        grid = new Grid(getReader().readAsStringList("day10_sample.txt"));
        grid = new Grid(getReader().readAsStringList(10));
    }

    @Override
    public Integer getSolution1() {
        return grid.findAll('0').stream().mapToInt(this::scoreTrailHead).sum();
    }

    private int scoreTrailHead(Point head) {
        //bfs
        Set<Point> found = new HashSet<>();
        Queue<Point> q = new LinkedList<>();
        q.add(head);
        while (!q.isEmpty()) {
            Point curr = q.poll();
            int cv = grid.lookup(curr);
            if (cv == '9') {
                found.add(curr);
            } else {
                curr.neighbours().filter(n -> grid.lookup(n) - cv == 1).forEach(q::add);
            }
        }
        return found.size();
    }

    private int rateTrailHead(Point head) {
        //bfs
        int rating = 0;
        Queue<Point> q = new LinkedList<>();
        q.add(head);
        while (!q.isEmpty()) {
            Point curr = q.poll();
            int cv = grid.lookup(curr);
            if (cv == '9') {
                rating++;
            } else {
                curr.neighbours().filter(n -> grid.lookup(n) - cv == 1).forEach(q::add);
            }
        }
        return rating;
    }

    @Override
    public Integer getSolution2() {
        return grid.findAll('0').stream().mapToInt(this::rateTrailHead).sum();

    }
}
