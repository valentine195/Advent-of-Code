package com.jeremy.aoc2022.Days;

import java.util.HashSet;

import com.jeremy.aoc2022.Day;

public class Day6 extends Day {
    public Day6() {
        super();
    }

    public Day6(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {

        return check(INPUT, 4);

    }

    public String runPart2() {
        return check(INPUT, 14);
    }

    public String check(String input, int uniques) {
        for (int i = uniques; i < input.length(); i++) {
            if (INPUT.substring(i - uniques, i).chars().distinct().count() == uniques) {
                return "" + (i);
            }
        }
        throw new Error("No unique string of length " + uniques + " found.");
    }
}
