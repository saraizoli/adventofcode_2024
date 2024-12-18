package main.utils;

import java.util.List;

public class Grid {

    private final List<String> data;
    private final int height;
    private final int width;
    private final Point corner;

    public Grid(List<String> data) {
        this.data = data;
        height = data.size();
        width = data.getFirst().length();
        corner = new Point(width, height);
    }

    public char lookup(Point p) {
        if (p.x() < 0 || p.x() >= width || p.y() < 0 || p.y() >= height) {
            return ' ';
        }
        return data.get(p.y()).charAt(p.x());
    }

    public char lookupWithoutBoundsCheck(Point p) {
        return data.get(p.y()).charAt(p.x());
    }

    public void print() {
        //todo?
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
        int j;
        for (int i = 0; i < height; i++) {
            if (0 <= (j = data.get(i).indexOf(c))) {
                return new Point(j, i);
            }
        }
        return null;
    }
}
