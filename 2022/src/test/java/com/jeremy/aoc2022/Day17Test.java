package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day17Test {

    static String TEST_INPUT = new Loader().loadTest(17);
    static Day17 day = new Day17(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "3068");
    }

    /* @Test
    public void part2() {
        assertEquals(day.runPart2(),
                "56000011");
    } */
}
