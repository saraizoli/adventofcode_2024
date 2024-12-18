package main.day07;

import main.utils.Day;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Day07 extends Day<Long> {
    //first element is the aim to compute, rest the operands
    private final List<List<Long>> codes;
    private final BiFunction<Long, Long, Long> mult = (i, j) -> i * j;
    private final BiFunction<Long, Long, Long> conc = (i, j) -> Long.parseLong(("" + i) + j);

    private final List<BiFunction<Long, Long, Long>> p1 = List.of(Long::sum, mult);
    private final List<BiFunction<Long, Long, Long>> p2 = List.of(Long::sum, mult, conc);

    public Day07() {
//        List<String> raw = getReader().readAsStringList("day07_sample.txt");
        List<String> raw = getReader().readAsStringList(7);
        codes = raw.stream().map(s -> s.split(":? ")).map(st -> Stream.of(st).map(Long::parseLong).toList()).toList();
    }

    @Override
    public Long getSolution1() {
        return codes.parallelStream().filter(l -> check(l, p1)).mapToLong(List::getFirst).sum();
    }

    public boolean check(List<Long> nums, List<BiFunction<Long, Long, Long>> ops) {
        return resolve(nums, nums.size() - 1, ops).stream().anyMatch(i -> i.equals(nums.getFirst()));
    }

    public List<Long> resolve(List<Long> nums, int indexTill, List<BiFunction<Long, Long, Long>> ops) {
        Long curr = nums.get(indexTill);
        if (indexTill == 1) {
            return Collections.singletonList(curr);
        } else {
            return resolve(nums, indexTill - 1, ops).stream().flatMap(i -> ops.stream().map(op -> op.apply(i, curr))).toList();
        }
    }

    @Override
    public Long getSolution2() {
        return codes.parallelStream().filter(l -> check(l, p2)).mapToLong(List::getFirst).sum();
    }

//  nongeneric operations solution is 3* faster than the above
//    public boolean check(List<Long> nums) {
//        return resolve(nums, nums.size() - 1).stream().anyMatch(i -> i.equals(nums.getFirst()));
//    }
//
//    public List<Long> resolve(List<Long> nums, int indexTill) {
//        Long curr = nums.get(indexTill);
//        if (indexTill == 1) {
//            return Collections.singletonList(curr);
//        } else {
//            List<Long> till = resolve(nums, indexTill - 1);
//            return Stream.concat(till.stream().map(i -> i + curr), till.stream().map(i -> i * curr)).filter(i -> i <= nums.getFirst()).toList();
//        }
//    }
}
