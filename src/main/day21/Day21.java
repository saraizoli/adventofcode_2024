package main.day21;

import main.utils.Day;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day21 extends Day<Long> {
    private final Map<String, String> codes;
    private final Map<State, Long> memo;

    //changing dirs twice in a single click is suboptimal
    //left then up/down is always not worse than the reverse
    //up/down then right is always not worse than the reverse
    //the above ofc only if possible to do so
    //with this its possible to record all the transitions and all the original paths on the keypad
    private static final Map<Integer, String> NUM_TRANS = new HashMap<>();

    static {
        //from char bitshifted by 8 + to char -> best string path
        NUM_TRANS.put(('A' << 8) + 'A', "A");
        NUM_TRANS.put(('A' << 8) + '^', "<A");
        NUM_TRANS.put(('A' << 8) + 'v', "<vA");
        NUM_TRANS.put(('A' << 8) + '<', "v<<A");
        NUM_TRANS.put(('A' << 8) + '>', "vA");

        NUM_TRANS.put(('^' << 8) + 'A', ">A");
        NUM_TRANS.put(('^' << 8) + '^', "A");
        NUM_TRANS.put(('^' << 8) + 'v', "vA");
        NUM_TRANS.put(('^' << 8) + '<', "v<A");
        NUM_TRANS.put(('^' << 8) + '>', "v>A");

        NUM_TRANS.put(('v' << 8) + 'A', "^>A");
        NUM_TRANS.put(('v' << 8) + '^', "^A");
        NUM_TRANS.put(('v' << 8) + 'v', "A");
        NUM_TRANS.put(('v' << 8) + '<', "<A");
        NUM_TRANS.put(('v' << 8) + '>', ">A");

        NUM_TRANS.put(('<' << 8) + 'A', ">>^A");
        NUM_TRANS.put(('<' << 8) + '^', ">^A");
        NUM_TRANS.put(('<' << 8) + 'v', ">A");
        NUM_TRANS.put(('<' << 8) + '<', "A");
        NUM_TRANS.put(('<' << 8) + '>', ">>A");

        NUM_TRANS.put(('>' << 8) + 'A', "^A");
        NUM_TRANS.put(('>' << 8) + '^', "<^A");
        NUM_TRANS.put(('>' << 8) + 'v', "<A");
        NUM_TRANS.put(('>' << 8) + '<', "<<A");
        NUM_TRANS.put(('>' << 8) + '>', "A");
    }

    public Day21() {
//        codes = Map.of(
//                "029A","<A^A^^>AvvvA",
//                "980A","^^^A<AvvvA>A",
//                "179A","^<<A^^A>>AvvvA",
//                "456A","^^<<A>A>AvvA",
//                "379A","^A<<^^A>>AvvvA"
//        );
        codes = Map.of(
                "671A", "^^A<<^AvvA>>vA",
                "826A", "<^^^AvvA^>AvvA",
                "670A", "^^A<<^A>vvvA>A",
                "085A", "<A^^^AvAvv>A",
                "283A", "<^A^^Avv>AvA"
        );
        memo = new HashMap<>();
    }

    @Override
    public Long getSolution1() {
        memo.clear();
        return codes.entrySet().stream().mapToLong(e -> Long.parseLong(e.getKey().substring(0, 3)) * resolve(e.getValue(), 2)).sum();
    }

    private long resolve(String s, int robotLayers) {
        if (robotLayers == 0) {
            return s.length();
        }
        State state = new State(s, robotLayers);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }
        char p = 'A';
        List<String> tokens = new LinkedList<>();
        for (char c : s.toCharArray()) {
            tokens.add(NUM_TRANS.get((p << 8) + c));
            p = c;
        }
//        System.out.println(sb);
        long res = tokens.stream().mapToLong(t -> resolve(t, robotLayers - 1)).sum();
        memo.put(state, res);
        return res;
    }


    @Override
    public Long getSolution2() {
        memo.clear();
        return codes.entrySet().stream().mapToLong(e -> Long.parseLong(e.getKey().substring(0, 3)) * resolve(e.getValue(), 25)).sum();
    }
}

record State(String s, int rl) {
}