package main.day17;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class VirtualMachine {
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

    private long res(int i) {
        return switch (i) {
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> i;
        };
    }

    public VirtualMachine(int[] code) {
        a = 0;
        b = 0;
        c = 0;
        this.code = code;
    }

    List<Integer> runProgram(long aVal) {
        reset();
        a = aVal;
        while (ip < code.length) {
            ops[code[ip]].accept(code[ip + 1]);
            ip += 2;
        }
        return out;
    }

    private void reset(){
        b = 0;
        c = 0;
        a = 0;
        ip = 0;
        out = new ArrayList<>();
    }
}
