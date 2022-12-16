package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day16Test {

    static String TEST_INPUT = new Loader().loadTest(16);
    static Day16 day = new Day16(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "1651");
    }

    @Test
    public void part2() {
        assertEquals(day.runPart2(),
                "56000011");
    }
}
