package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day09Test {

    static String TEST_INPUT = new Loader().loadTest(9);
    static Day09 day = new Day09(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "13");
    }

    @Test
    public void part2() {
        Day09 day = new Day09("R 5\nU 8\nL 8\nD 3\nR 17\nD 10\nL 25\nU 20");
        assertEquals(day.runPart2(), "36");

    }
}
