package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jeremy.aoc2022.Days.*;

public class Day06Test {

    static String TEST_INPUT = new Loader().loadTest(6);
    static Day06 day = new Day06(TEST_INPUT);

    @Test
    public void part1() {
        for (String line : List.of(TEST_INPUT.split("\n"))) {
            String[] split = line.split(":");
            Day06 day = new Day06(split[0]);
            assertEquals(day.runPart1(), split[1]);
        }
    }

    @Test
    public void part2() {
        for (String line : List.of(TEST_INPUT.split("\n"))) {
            String[] split = line.split(":");
            Day06 day = new Day06(split[0]);
            assertEquals(day.runPart2(), split[2]);
        }
    }
}
