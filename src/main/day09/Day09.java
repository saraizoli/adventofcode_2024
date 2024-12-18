package main.day09;

import main.utils.Day;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.stream.LongStream;

//todo refactor
public class Day09 extends Day<Long> {
    private final int[] origCodes;
    private final int fileCnt;

    public Day09() {
//        String raw = "12345";
//        String raw = "2333133121414131402";
        String raw = getReader().readAsStringList(9).getFirst();
        origCodes = raw.chars().map(i -> i - '0').toArray();
        fileCnt = (origCodes.length + 1) / 2;
    }

    @Override
    public Long getSolution1() {
        //effectively two-pointer solution moving from front and back, just not using extra space to explode the original codes
        int[] codes = new int[origCodes.length];
        System.arraycopy(origCodes, 0, codes, 0, origCodes.length);
        long sum = 0;
        long i = 0;
        int fileInd = 0;
        int movedFileInd = fileCnt - 1;
        while (fileInd <= movedFileInd) {
            for (int j = 0; j < codes[fileInd * 2]; j++) { //checksum of file in original position
                sum += fileInd * i++;
            }
            int blankSize = codes[fileInd * 2 + 1];
            while (blankSize > 0 && fileInd < movedFileInd) { // dealing with the blank after file fileInd
                int movedFileSize = codes[movedFileInd * 2];
                if (blankSize >= movedFileSize) {
                    for (int j = 0; j < movedFileSize; j++) {
                        sum += movedFileInd * i++;
                    }
                    blankSize -= movedFileSize;
                    movedFileInd--;
                } else {
                    for (int j = 0; j < blankSize; j++) {
                        sum += movedFileInd * i++;
                    }
                    codes[movedFileInd * 2] -= blankSize;
                    blankSize = 0;
                }
            }
            fileInd++;
        }
        return sum;
    }


    @Override
    public Long getSolution2() {
        TreeSet<Integer>[] gaps = new TreeSet[10];
        LinkedList<File> files = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            gaps[i] = new TreeSet<>();
        }
        int sum = 0;
        for (int i = 0; i < origCodes.length; i++) {
            if (i % 2 == 0) {
                files.addFirst(new File(i / 2, sum, origCodes[i]));
            } else {
                gaps[origCodes[i]].add(sum);
            }
            sum += origCodes[i];
        }
        File f = files.removeFirst();
        while (f.id() != 0) {
            int mini = -1;
            int min = f.startInd();
            for (int i = f.len(); i < 10; i++) {
                if (!gaps[i].isEmpty() && gaps[i].first() < min) {
                    mini = i;
                    min = gaps[i].first();
                }
            }
            if (mini > 0) {
                int newStart = gaps[mini].pollFirst();
                f = new File(f.id(), newStart, f.len());
                gaps[mini - f.len()].add(newStart + f.len());
            }
            files.addLast(f);
            f = files.removeFirst();
        }
//        return files.stream().mapToLong(e -> e.id() * (long) (e.len() * (e.len() - 1) / 2 + e.len() * e.startInd())).sum();
        return files.stream().mapToLong(e -> LongStream.range(e.startInd(), e.startInd() + e.len()).map(i -> i * e.id()).sum()).sum();
    }
}

record File(int id, int startInd, int len) {
}