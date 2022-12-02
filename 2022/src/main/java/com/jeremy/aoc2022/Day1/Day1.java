package com.jeremy.aoc2022.Day1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Day1 {
    String TEST_INPUT = "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000";
    String INPUT;

    public void run() {
        run(TEST_INPUT);
    }

    public void run(String input) {
        INPUT = input;
        runPart1();
        runPart2();
    }

    public void runPart1() {
        Stream<String> elves = getElves(INPUT);
        Stream<Integer> calories = elves.map(elf -> getCalories(elf));
        Integer highest = calories.max((i, j) -> i.compareTo(j)).get();

        System.out.println("Part 1: " + highest);
    }

    public void runPart2() {
        Stream<String> elves = getElves(INPUT);
        Stream<Integer> calories = elves.map(elf -> getCalories(elf));
        Integer highest = calories.sorted(Comparator.reverseOrder()).limit(3).mapToInt(i -> i.intValue()).sum();

        System.out.println("Part 2: " + highest);
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
