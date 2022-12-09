package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day8Test {

    static String TEST_INPUT = new Loader().loadTest(8);
    static Day8 day = new Day8(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "21");
    }

    @Test
    public void part2() {
        assertEquals(day.runPart2(), "8");

    }
}
