package main.day16;

import main.utils.Day;
import main.utils.Grid;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.*;
import java.util.stream.Stream;

public class Day16 extends Day<Integer> {
    private final Grid grid;
    private final Map<PointAndDir, Integer> dist;
    private final Map<PointAndDir, List<PointAndDir>> prevs;
    private final Set<PointAndDir> done;
    private final Set<Point> onBestPath;
    private final PointAndDir s;
    private final Point e;


    public Day16() {
//        grid = new Grid(getReader().readAsStringList("day16_sample2.txt"));
        grid = new Grid(getReader().readAsStringList(16));
        dist = new HashMap<>();
        prevs = new HashMap<>();
        done = new HashSet<>();
        onBestPath = new HashSet<>();
        s = new PointAndDir(grid.findOne('S'), Point.R);
        e = grid.findOne('E');
    }

    @Override
    public Integer getSolution1() {
        //Dijkstra with remembering all optimal prev points
        reset();
        PriorityQueue<PointAndDir> q = new PriorityQueue<>(Comparator.comparingInt(p -> dist.getOrDefault(p, Integer.MAX_VALUE)));
        q.add(s);
        while (!q.isEmpty()) {
            PointAndDir curr = q.poll();
            if (e.equals(curr.p())) {
                return dist.get(curr);
            }
            done.add(curr);
            Stream.of(curr.d(), curr.d().rotate(1), curr.d().rotate(-1))
                    .map(d -> new PointAndDir(curr.p().add(d), d))
                    .filter(pd -> grid.lookupWithoutBoundsCheck(pd.p()) != '#' && !done.contains(pd))
                    .forEach(pd -> {
                                int cost = dist.getOrDefault(pd, Integer.MAX_VALUE);
                                int newCost = dist.get(curr) + (curr.d().equals(pd.d()) ? 1 : 1001);
                                if (newCost > cost) {
                                    return;
                                }
                                q.remove(pd);
                                dist.put(pd, newCost);
                                q.add(pd);

                                if (cost == newCost) {
                                    prevs.get(pd).add(curr);
                                } else {
                                    prevs.put(pd, new ArrayList<>(List.of(curr)));
                                }
                            }
                    );
        }
        return -1;
    }


    @Override
    public Integer getSolution2() {
        //needs solution1 to be ran first
        onBestPath.clear();
        Stream.of(Point.DIRS).map(d -> new PointAndDir(e, d)).forEach(this::resolvePath);
        return onBestPath.size();
    }

    private void resolvePath(PointAndDir p) {
        onBestPath.add(p.p());
        List<PointAndDir> prev = prevs.get(p);
        if (prev != null) {
            prev.forEach(this::resolvePath);
        }
    }

    private void reset() {
        dist.clear();
        prevs.clear();
        done.clear();
        dist.put(s, 0);
    }
}