package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeremy.aoc2022.Days.*;

public class Day02Test {

    static String TEST_INPUT = new Loader().loadTest(2);
    static Day02 day = new Day02(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(Integer.parseInt(day.runPart1()), 15);
    }

    @Test
    public void part2() {
        assertEquals(Integer.parseInt(day.runPart2()), 12);
    }
}
