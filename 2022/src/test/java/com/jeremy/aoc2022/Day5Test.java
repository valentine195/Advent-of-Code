package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeremy.aoc2022.Days.*;

public class Day5Test {

    static String TEST_INPUT = new Loader().loadTest(5);
    static Day5 day = new Day5(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "CMZ");
    }

    @Test
    public void part2() {
        assertEquals(day.runPart2(), "MCD");
    }
}
