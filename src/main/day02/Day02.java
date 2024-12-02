package main.day02;

import main.utils.Day;
import main.utils.MyMath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//todo refactor, messy
public class Day02 extends Day<Long> {
    private final List<String> codes;

    public Day02() {
        codes = getReader().readAsStringList(2);
    }

    @Override
    public Long getSolution1() {
        return codes.stream().filter(this::valid1).count();
    }

    public boolean valid1(String code) {
        List<Integer> list = Stream.of(code.split(" ")).map(Integer::parseInt).toList();
        return valid1(list);
    }

    private static boolean valid1(List<Integer> list) {
        List<Integer> diffs = MyMath.diffs(list);
        boolean inc = diffs.getFirst() > 0;
        return diffs.stream().allMatch(d -> d != 0 && d >= -3 && d <= 3 && d > 0 == inc);
    }

    @Override
    public Long getSolution2() {
        return codes.stream().filter(this::valid2).count();
    }

    public boolean valid2(String code) {
        List<Integer> list = Stream.of(code.split(" ")).map(Integer::parseInt).toList();
        if (valid1(list)) {
            return true;
        }

        //could only retry around the first offending index
        return IntStream.range(0, list.size()).anyMatch(i -> {
            List<Integer> l = new ArrayList<>(list);
            l.remove(i);
            return valid1(l);
        });
    }

// not a lot of perf improvement
//    public boolean validRaw(String code) {
//        String[] t = code.split(" ");
//        int last = Integer.parseInt(t[0]);
//        int diff = Integer.parseInt(t[1]) - last;
//        boolean inc = diff > 0;
//        for (int i = 1; i < t.length; i++) {
//            int curr = Integer.parseInt(t[i]);
//            diff = curr - last;
//            last = curr;
//            if (inc != diff > 0 || diff == 0 || diff < -3 || diff > 3) {
//                return false;
//            }
//        }
//        return true;
//    }
}
