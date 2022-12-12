package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day12Test {

    static String TEST_INPUT = new Loader().loadTest(12);
    static Day12 day = new Day12(TEST_INPUT);

    @Test
    public void part1() {
        day.buildGrid();
        assertEquals(day.runPart1(), "31");
    }
    
    /*     @Test
    public void part2() {
        day.buildGrid();
        assertEquals(day.runPart2(),
                "2713310158");
    } */
}
