package main.application;

import main.day06.Day06;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day06();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day25::new, 100);
        day.printSolution1WithTime(100);
        day.printSolution2WithTime(100);
    }
}