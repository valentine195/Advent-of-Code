package com.jeremy.aoc2022.Days;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

public class Day01 extends Day {

    public Day01() {

    }

    public Day01(String input) {
        INPUT = input;
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {
        Stream<String> elves = getElves(INPUT);
        Stream<Integer> calories = elves.map(elf -> getCalories(elf));
        Integer highest = calories.max((i, j) -> i.compareTo(j)).get();

        return highest.toString();
    }

    public String runPart2() {
        Stream<String> elves = getElves(INPUT);
        Stream<Integer> calories = elves.map(elf -> getCalories(elf));
        Integer highest = calories.sorted(Comparator.reverseOrder()).limit(3).mapToInt(i -> i.intValue()).sum();

        return highest.toString();
    }

    private Stream<String> getElves(String input) {
        return Arrays.stream(input.split("\n\n"));
    }

    public int getCalories(String elf) {
        String[] split = elf.split("\n");
        int calories = 0;
        for (String calorie : split) {
            calories += Integer.parseInt(calorie);
        }
        return calories;
    }
}
