package com.jeremy.aoc2022;

import java.util.List;

import com.jeremy.aoc2022.Day1.Day1;

public class Runner {
    List<Day> days = List.of(new Day1());

    public void runDay(Integer dayNumber, String input) {
        Day day = days.get(dayNumber - 1);
        if (!day.equals(null)) {
            day.run(input);
        } else {
            throw new Error("Invalid day specified");
        }
    }
}