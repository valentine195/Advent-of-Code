package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeremy.aoc2022.Days.Day01;

public class Day01Test {

    static String TEST_INPUT = new Loader().loadTest(1);
    static Day01 day1 = new Day01(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(Integer.parseInt(day1.runPart1()), 24000);
    }

    @Test
    public void part2() {
        assertEquals(Integer.parseInt(day1.runPart2()), 45000);
    }
}
