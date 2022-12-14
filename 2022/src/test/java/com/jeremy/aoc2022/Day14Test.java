package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day14Test {

    static String TEST_INPUT = new Loader().loadTest(14);
    static Day14 day = new Day14(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "24");
    }
    
    /*     @Test
    public void part2() {
        day.buildGrid();
        assertEquals(day.runPart2(),
                "2713310158");
    } */
}
