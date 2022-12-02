package com.jeremy.aoc2022.Day1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeremy.aoc2022.Loader;

public class Day1Test {

    static String TEST_INPUT = new Loader().load(1);
    static Day1 day1 = new Day1(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day1.runPart1(), 24000);
    }

    @Test
    public void part2() {
        assertEquals(day1.runPart2(), 45000);
    }
}
