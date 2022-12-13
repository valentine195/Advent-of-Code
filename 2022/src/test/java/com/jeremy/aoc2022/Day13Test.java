package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day13Test {

    static String TEST_INPUT = new Loader().loadTest(13);
    static Day13 day = new Day13(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "13");
    }
    
    /*     @Test
    public void part2() {
        day.buildGrid();
        assertEquals(day.runPart2(),
                "2713310158");
    } */
}
