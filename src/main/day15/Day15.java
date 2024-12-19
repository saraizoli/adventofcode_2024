package main.day15;

import main.utils.Day;
import main.utils.MutableGrid;
import main.utils.Point;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15 extends Day<Long> {
    private final String moves;
    private final List<String> gridBase;

    private MutableGrid grid;

    public static final char ROBOT = '@';
    public static final char EMPTY = '.';
    public static final char BOX = 'O';
    public static final char WALL = '#';
    public static final char BOXO = '[';
    public static final char BOXC = ']';

    public Day15() {
//        List<String> raw = getReader().readAsStringList("day15_sample.txt");
        List<String> raw = getReader().readAsStringList(15);
        int blankLine = IntStream.iterate(0, i -> i + 1).dropWhile(i -> !raw.get(i).isBlank()).findFirst().orElseThrow();
        gridBase = raw.subList(0, blankLine);
        moves = raw.stream().skip(blankLine).collect(Collectors.joining());
    }

    @Override
    public Long getSolution1() {
        grid = new MutableGrid(gridBase);
        Point robot = grid.findOne(ROBOT);

        for (char c : moves.toCharArray()) {
            Point d = Point.DIRS_CHAR_MAP.get(c);
            if (attemptMove(robot, d)) {
                robot = robot.add(d);
            }
        }
//        grid.print();
        return grid.findAll(BOX).stream().mapToLong(p -> p.x() + 100L * p.y()).sum();
    }

    private boolean attemptMove(Point from, Point dir) {
        //not the cleanest, redraws the grid but does not move the robot
        Point next = from.add(dir);
        char cv = grid.lookupWithoutBoundsCheck(from);
        char nv = grid.lookupWithoutBoundsCheck(next);
        if (nv == EMPTY || ((nv == BOX || nv == BOXO || nv == BOXC) && attemptMove(next, dir))) {
            grid.set(from, EMPTY);
            grid.set(next, cv);
            return true;
        }
        return false;
    }


    @Override
    public Long getSolution2() {
        grid = transformGridFor2();
        Point robot = grid.findOne(ROBOT);

        for (char c : moves.toCharArray()) {
            Point d = Point.DIRS_CHAR_MAP.get(c);
            if (d == Point.L || d == Point.R) { //horizontal moves don't change
                if (attemptMove(robot, d)) {
                    robot = robot.add(d);
                }
            } else { //vertically I couldn't be as greedy, first need to check if the whole stack can move, and only attempt after
                boolean can = checkCanMove(robot, d);
                if (can) {
                    move(robot, d);
                    robot = robot.add(d);
                }
            }
        }
//        grid.print();
        return grid.findAll(BOXO).stream().mapToLong(p -> p.x() + 100L * p.y()).sum();
    }

    private boolean checkCanMove(Point from, Point dir) {
        //not the cleanest, redraws the grid but does not move the robot
        Point next = from.add(dir);
        char cv = grid.lookupWithoutBoundsCheck(from);
        char nv = grid.lookupWithoutBoundsCheck(next);
        return switch (nv) {
            case EMPTY -> true;
            case BOXO -> checkCanMove(next, dir) && checkCanMove(next.add(Point.R), dir);
            case BOXC -> checkCanMove(next, dir) && checkCanMove(next.add(Point.L), dir);
            default -> false;
        };
    }

    private void move(Point from, Point dir) {
        Point next = from.add(dir);
        char cv = grid.lookupWithoutBoundsCheck(from);
        char nv = grid.lookupWithoutBoundsCheck(next);
        if (nv == BOXO) {
            move(next, dir);
            move(next.add(Point.R), dir);
        }
        if (nv == BOXC) {
            move(next, dir);
            move(next.add(Point.L), dir);
        }
        grid.set(from, EMPTY);
        grid.set(next, cv);
    }

    private MutableGrid transformGridFor2() {
        List<String> mappedList = gridBase.stream().map(s -> {
            StringBuilder sb = new StringBuilder();
            s.chars().forEachOrdered(i -> sb.append( //ugly but wanted to try out the new switch
                    switch (i) {
                        case ROBOT -> "@.";
                        case EMPTY -> "..";
                        case BOX -> "[]";
                        default -> "##";
                    }
            ));
            return sb.toString();
        }).toList();
        return new MutableGrid(mappedList);
    }
}
