package main.day14;

import main.utils.Day;
import main.utils.MyMath;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day14 extends Day<Long> {
    private final List<PointAndDir> robots;
    private final int h;
    private final int w;

    public Day14() {
//        List<String> raw = getReader().readAsStringList("day14_sample.txt");
//        h = 7;
//        w = 11;
        List<String> raw = getReader().readAsStringList(14);
        h = 103;
        w = 101;
        robots = raw.stream().map(this::parse).toList();

    }

    private PointAndDir parse(String s) {
        String[] tokens = s.split("([pv, ])=?");
        return new PointAndDir(new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])), new Point(Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])));
    }

    @Override
    public Long getSolution1() {
        Map<Integer, Long> quadrants = move(100)
                .filter(p -> !(p.x() == w / 2 || p.y() == h / 2))
                .collect(Collectors.groupingBy(this::getQuadrant, Collectors.counting()));
        return quadrants.values().stream().reduce(1L, (i, j) -> i * j);
    }

    private Stream<Point> move(int steps) {
        return robots.stream()
                .map(r -> r.p().add(r.d().mult(steps)))
                .map(r -> new Point(MyMath.posMod(r.x(), w), MyMath.posMod(r.y(), h)));
    }

    private Integer getQuadrant(Point p) {
        return (p.x() < w / 2 ? 0 : 1) + (p.y() < h / 2 ? 0 : 2);
    }


    @Override
    public Long getSolution2() {
        List<Spread> list = IntStream.range(0, 103).mapToObj(this::calcSpread).toList();
        int minXSpread = list.stream().min(Comparator.comparingDouble(Spread::x)).map(list::indexOf).orElseThrow();
        int minYSpread = list.stream().min(Comparator.comparingDouble(Spread::y)).map(list::indexOf).orElseThrow();
        int i = 0;

        //finding the index that is both == minXSpread (mod h) and ==minYSpread (mod w), that will simultaneously have minimum spread in both coords
        while (true) {
            if ((minXSpread - minYSpread + i * w) % h == 0) {
                int res = minXSpread + i * w;
                printAfterNSec(res);
                return (long) res;
            }
            i++;
        }
    }

    private void printAfterNSec(int res) {
        Set<Point> resSet = move(res).collect(Collectors.toSet());
        for (int j = 0; j < h; j++) {
            for (int k = 0; k < w; k++) {
                System.out.print(resSet.contains(new Point(k, j)) ? '█' : ' ');
            }
            System.out.println();
        }

    }

    private Spread calcSpread(int i) {
        List<Point> movedRobots = move(i).toList();
        Point sum = movedRobots.stream().reduce(Point.O, Point::add);
        double avgx = sum.x() / (double) movedRobots.size();
        double avgy = sum.y() / (double) movedRobots.size();
        //avg distance from avg in x and y coord separately
        return movedRobots.stream().map(r -> new Spread(Math.abs(r.x() - avgx), Math.abs(r.y() - avgy))).reduce(new Spread(0, 0), (s1, s2) -> new Spread(s1.x() + s2.x(), s1.y() + s2.y()));
    }
}

record Spread(double x, double y) {
}