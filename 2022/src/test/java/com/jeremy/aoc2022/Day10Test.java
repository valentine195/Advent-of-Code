package com.jeremy.aoc2022;

import static org.junit.Assert.assertEquals;

import com.jeremy.aoc2022.Days.*;
import org.junit.Test;

public class Day10Test {

    static String TEST_INPUT = new Loader().loadTest(10);
    static Day10 day = new Day10(TEST_INPUT);

    @Test
    public void part1() {
        assertEquals(day.runPart1(), "13140");
    }

    @Test
    public void part2() {
        assertEquals(day.runPart2(),
                "##..##..##..##..##..##..##..##..##..##..\n###...###...###...###...###...###...###.\n####....####....####....####....####....\n#####.....#####.....#####.....#####.....\n######......######......######......####\n#######.......#######.......#######.....");
    }
}
