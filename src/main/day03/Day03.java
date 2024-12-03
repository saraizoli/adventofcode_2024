package main.day03;

import main.utils.Day;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends Day<Integer> {
    private final String code;

    public Day03() {
        code = String.join("", getReader().readAsStringList(3));
    }

    @Override
    public Integer getSolution1() {
        return matchAndMult(code);
    }

    private static int matchAndMult(String text) {
        int s = 0;
        Matcher m = Pattern.compile("mul\\((\\d+),(\\d+)\\)").matcher(text);
        while (m.find()) {
            s += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
        }

        return s;
    }

    @Override
    public Integer getSolution2() {
        Matcher bm = Pattern.compile("do\\(\\)").matcher(code);
        Matcher em = Pattern.compile("don't\\(\\)").matcher(code);

        int s = 0;
        int len = code.length();

        int bi = 0;
        int ei = em.find() ? em.start() : len;

        while (bi < len) {
            s += matchAndMult(code.substring(bi, ei));
            bi = bm.find(ei) ? bm.end() : len;
            ei = em.find(bi) ? em.start() : len;
        }

        return s;
    }
}
