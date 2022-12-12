package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeremy.aoc2022.Days.*;

public class Day04Test {

    static String TEST_INPUT = new Loader().loadTest(4);
    static Day04 day = new Day04(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(Integer.parseInt(day.runPart1()), 2);
    }

    @Test
    public void part2() {
        assertEquals(Integer.parseInt(day.runPart2()), 4);
    }
}
