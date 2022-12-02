package com.jeremy.aoc2022.Day1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Day1Test {

    String TEST_INPUT = "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000";
    Day1 day1 = new Day1(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day1.runPart1(), 24000);
    }

    @Test
    public void part2() {
        assertEquals(day1.runPart2(), 45000);
    }
}
