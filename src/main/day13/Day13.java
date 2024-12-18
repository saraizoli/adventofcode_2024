package main.day13;

import main.utils.Day;
import main.utils.Point;

import java.util.List;
import java.util.stream.IntStream;

public class Day13 extends Day<Long> {
    private final List<Machine> machines;

    //ignoring the "fewest tokens" part, as each machine only has 1 real solution as the button vectors are non colinear luckily
    public Day13() {
//        List<String> raw = getReader().readAsStringList("day13_sample.txt");
        List<String> raw = getReader().readAsStringList(13);
        machines = IntStream.rangeClosed(0, raw.size() / 4).mapToObj(i -> new Machine(parsePoint(raw.get(4 * i)), parsePoint(raw.get(4 * i + 1)), parsePoint(raw.get(4 * i + 2)))).toList();

    }

    private Point parsePoint(String s) {
        String[] tokens = s.split("([XY,])=?");
        return new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[3]));
    }

    @Override
    public Long getSolution1() {
        return machines.stream().mapToLong(this::check1).sum();
    }


    private long check1(Machine m) {
        return check(m, 0L);
    }


    private long check2(Machine m) {
        return check(m, 10000000000000L);
    }

    private long check(Machine m, long offset) {
        //solve 2 variable equation system for integer solutions

        long px = m.prize().x() + offset;
        long py = m.prize().y() + offset;
        long bNom = px * m.a().y() - py * m.a().x();
        long bDiv = m.b().x() * m.a().y() - m.a().x() * m.b().y();
        if (bNom % bDiv != 0) {
            return 0;
        }
        long B = bNom / bDiv;
        long aNom = px - B * m.b().x();
        if (aNom % m.a().x() != 0) {
            return 0;
        }
        long A = aNom / m.a().x();
        return 3 * A + B;
    }


    @Override
    public Long getSolution2() {
        return machines.stream().mapToLong(this::check2).sum();

    }
}

record Machine(Point a, Point b, Point prize) {
}