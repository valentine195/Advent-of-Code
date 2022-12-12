package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day11Test {

    static String TEST_INPUT = new Loader().loadTest(11);
    static Day11 day = new Day11(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "10605");
    }

    @Test
    public void part2() {
        assertEquals(day.runPart2(),
                "2713310158");
    }
}
