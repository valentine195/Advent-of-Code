package com.jeremy.aoc2022;

import java.util.List;

import com.jeremy.aoc2022.Days.*;

public class Runner {
    List<Day> days = List.of(new Day01(), new Day02(), new Day03(), new Day04(), new Day05(), new Day06(), new Day07(),
            new Day08(), new Day09(), new Day10(), new Day11(), new Day12(), new Day13(), new Day14(), new Day15(), new Day16());

    public void runDay(Integer dayNumber, String input) {
        Day day = days.get(dayNumber - 1);
        if (!day.equals(null)) {
            day.setInput(input);
            day.run();
        } else {
            throw new Error("Invalid day specified");
        }
    }
}