package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jeremy.aoc2022.Days.*;

public class Day03Test {

    static String TEST_INPUT = new Loader().loadTest(3);
    static Day03 day = new Day03(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(Integer.parseInt(day.runPart1()), 157);
    }

    @Test
    public void part2() {
        assertEquals(Integer.parseInt(day.runPart2()), 70);
    }
}
