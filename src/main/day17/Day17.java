package main.day17;

import main.utils.Day;

import java.util.List;

public class Day17 extends Day<Long> {
    private final long origA;
    private final int[] code;
    private final VirtualMachine vm;

    public Day17() {
        origA = 46323429;
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
        vm = new VirtualMachine(code);
    }

    @Override
    public Long getSolution1() {
        System.out.println(vm.runProgram(origA));
        return 0L;
    }

    @Override
    public Long getSolution2() {
        //checking the program, we notice it is a single loop with 1 out command in each loop
        //in each iteration we take the last 3 bits of 'a' (to set b), some earlier 3 bits (to set c), combine them, then print the combination. Lastly we drop the last 3 bits of 'a', converging to 0
        //for any given output string, reading it backwards we can build up 'a' by 3 bit chunks - we need to dfs in the tree created by this logic to find the smallest 'a'
        //to stop, a has to be 0 at the end, so we start with candidate for a=0, count of (octal) digits already set=0
        return dfs(0, 0);
    }

    private long dfs(long ac, int digs) {
        if (digs == code.length) {
            return ac;
        }
        for (int i = 0; i < 8; i++) {
            long nac = ac * 8 + i; //try appending all 3 bit combos and checking the output
            List<Integer> out = vm.runProgram(nac);
            if (!out.getFirst().equals(code[code.length - 1 - digs])) { //this try for 'a' resulted in a bad output on the index 'digs' indexes from the end
                continue;
            }
            long res = dfs(nac, digs + 1); //res = -1 means 'nac' is a dead end in the dfs
            if (res != -1) {
                return res;
            }
        }
        return -1;
    }
}
