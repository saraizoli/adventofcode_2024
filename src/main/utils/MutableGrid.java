package main.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MutableGrid {

    private final List<char[]> data;
    private final int height;
    private final int width;
    private final Point corner;

    public MutableGrid(List<String> data) {
        this.data = data.stream().map(String::toCharArray).toList();
        height = data.size();
        width = data.getFirst().length();
        corner = new Point(width, height);
    }

    public char lookup(Point p) {
        if (p.x() < 0 || p.x() >= width || p.y() < 0 || p.y() >= height) {
            return ' ';
        }
        return data.get(p.y())[p.x()];
    }

    public void set(Point p, char c) {
        data.get(p.y())[p.x()] = c;
    }

    public char lookupWithoutBoundsCheck(Point p) {
        return data.get(p.y())[p.x()];
    }

    public void print() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(data.get(y)[x]);
            }
            System.out.println();
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Point getCorner() {
        return corner;
    }

    public Point findOne(char c) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data.get(y)[x] == c) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    public Set<Point> findAll(char c) {
        Set<Point> found = new HashSet<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data.get(y)[x] == c) {
                    found.add(new Point(x, y));
                }
            }
        }
        return found;
    }
}
