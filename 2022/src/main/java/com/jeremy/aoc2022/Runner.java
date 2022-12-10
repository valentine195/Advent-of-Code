package com.jeremy.aoc2022;

import java.util.List;

import com.jeremy.aoc2022.Days.*;

public class Runner {
    List<Day> days = List.of(new Day1(), new Day2(), new Day3(), new Day4(), new Day5(), new Day6(), new Day7(),
            new Day8(), new Day9(), new Day10());

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