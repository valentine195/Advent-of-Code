package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day15Test {

    static String TEST_INPUT = new Loader().loadTest(15);
    static Day15 day = new Day15(TEST_INPUT);

    @Test
    public void part1() {
        day.setTarget(10);
        assertEquals(day.runPart1(), "26");
    }

    @Test
    public void part2() {
        day.setMinMax(0, 20);
        assertEquals(day.runPart2(),
                "56000011");
    }
}
