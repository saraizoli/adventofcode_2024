package main.day17;

import main.utils.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class Day17 extends Day<Long> {
    private long a;
    private long b;
    private long c;
    private int ip;
    private List<Integer> out;

    private final int[] code;

    private final IntConsumer[] ops = new IntConsumer[]{
            /*0*/ i -> a = a >> res(i),
            /*1*/ i -> b = b ^ i,
            /*2*/ i -> b = res(i) & 7,
            /*3*/ i -> ip = (a == 0 ? ip : i - 2),
            /*4*/ i -> b = b ^ c,
            /*5*/ i -> out.add((int) res(i) & 7),
            i -> {
                throw new UnsupportedOperationException();
            },
            /*7*/ i -> c = a >> res(i)
    };

    public Day17() {
        b = 0;
        c = 0;
        a = 46323429;
        code = new int[]{
                2, 4, // b = a % 8
                1, 1, // b = b ^ 1
                7, 5, // c = a /2**b
                1, 5, // b = b ^ 5
                4, 3, // b = b ^ c
                0, 3, // a = a / 8
                5, 5, // print b
                3, 0 //jump to beginning
        };
    }

    @Override
    public Long getSolution1() {
        runProgram();
        System.out.println(out);
        return 0L;
    }

    private void runProgram() {
        ip = 0;
        out = new ArrayList<>();
        while (ip < code.length) {
            ops[code[ip]].accept(code[ip + 1]);
            ip += 2;
        }
    }


    @Override
    public Long getSolution2() {
        //checking the program, we notice it is a single loop with 1 out command in each loop
        //in each iteration we take the last 3 bits of 'a' (to set b), some earlier 3 bits (to set c), combine them, then print the combination. Lastly we drop the last 3 bits of a
        //for any given output string, reading it backwards we can build up 'a' by 3 bit chunks - we need to dfs in the tree created by this logic to find the smallest 'a'
        //to stop, a has to be 0 at the end, so we start with candidate for a=0, count of (octal) digits already set=0
        return dfs(0, 0);
    }

    private long dfs(long ac, int digs) {
        if (digs == code.length) {
            return ac;
        }
        for (int i = 0; i < 8; i++) {
            long nac = ac * 8 + i; //try appending all 3 bit combos
            a = nac;
            runProgram();
            if (!out.getFirst().equals(code[code.length - digs - 1])) { //this try for 'a' resulted in a bad output on the index digs indexes from the end
                continue;
            }
            long res = dfs(nac, digs + 1);
            if (res != -1) { //res = -1 means we ran into a dead end with dfs
                return res;
            }
        }
        return -1;
    }

    private long res(int i) {
        return switch (i) {
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> i;
        };
    }
}
