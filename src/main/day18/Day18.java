package main.day18;

import main.utils.Day;
import main.utils.Point;

import java.util.*;

public class Day18 extends Day<Integer> {
    private final List<Point> bytes;
    private final Point s = Point.O;
    private final Point e;

    public Day18() {
//        List<String> raw = getReader().readAsStringList("day18_sample.txt");
//        e = new Point(6,6);
        List<String> raw = getReader().readAsStringList(18);
        e = new Point(70, 70);
        bytes = raw.stream().map(Point::from).toList();


    }

    @Override
    public Integer getSolution1() {
        return findPath(1024);
    }

    public int findPath(int secs) {
        //bfs
        Set<Point> blocked = new HashSet<>(bytes.subList(0, secs));
        Map<Point, Integer> visited = new HashMap<>();
        Queue<Point> q = new LinkedList<>();
        q.add(s);
        visited.put(s, 0);
        while (!q.isEmpty()) {
            Point curr = q.poll();
            int d = visited.get(curr);
            if (e.equals(curr)) {
                return d;
            }
            curr.neighbours()
                    .filter(n -> n.isInRectClosed(e))
                    .filter(n -> !blocked.contains(n))
                    .filter(n -> !visited.containsKey(n))
                    .forEach(n -> {
                        visited.put(n, d + 1);
                        q.add(n);
                    });
        }
        return -1;
    }

    @Override
    public Integer getSolution2() {
        //binary search between 1024 and bytes.size();
        //there is a Lifelong Planning dijkstra that could be better
        //also could color the bytes in two colors - being reachable from (left or bottom edge) vs (top or right) - if a byte has 2 colors there is a wall
        int b = 1024;
        int f = bytes.size();
        int c = (b + f) / 2;
        while (f - b > 1) {
            int v = findPath(c);
            if (v == -1) {
                f = c;
            } else {
                b = c;
            }
            c = (b + f) / 2;
        }
//        System.out.println(findPath(f - 1));
//        System.out.println(findPath(f));
        System.out.println(bytes.get(f - 1));
        return f;
    }
}
