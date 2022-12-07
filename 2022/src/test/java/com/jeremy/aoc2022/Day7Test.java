package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day7Test {

    static String TEST_INPUT = new Loader().loadTest(7);
    static Day7 day = new Day7(TEST_INPUT);

    @Test
    public void part1() {

        day.buildTree();
        assertEquals(day.runPart1(), "95437");

    }

    @Test
    public void part2() {
        assertEquals(day.runPart2(), "24933642");

    }
}
