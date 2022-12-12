package com.jeremy.aoc2022.Days;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

public class Day04 extends Day {
    public Day04() {
        super();
    }

    public Day04(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {
        Integer priorities = 0;

        for (String group : MATCHES) {
            List<List<Integer>> elves = Stream.of(group.split(",")).map(g -> convert(g)).toList();
            if (elves.get(0).containsAll(elves.get(1)) || elves.get(1).containsAll(elves.get(0))) {
                priorities += 1;
            }
        }

        return priorities.toString();
    }

    public String runPart2() {
        Integer priorities = 0;
        for (String group : MATCHES) {
            List<List<Integer>> elves = Stream.of(group.split(",")).map(g -> convert(g)).toList();
            Set<Integer> intersection = getIntersection(elves);
            if (intersection.size() > 0)
                priorities += 1;
        }
        return priorities.toString();
    }

    public Boolean contains(List<Integer> first, List<Integer> second) {
        return first.containsAll(second);
    }

    public Set<Integer> getIntersection(List<List<Integer>> group) {
        Set<Integer> first = new HashSet<Integer>(group.get(0));

        for (List<Integer> compartment : group) {
            first.retainAll(new HashSet<Integer>(compartment));
        }

        return first;
    }

    public List<Integer> convert(String rucksack) {
        String[] range = rucksack.split("-");
        return IntStream.range(Integer.parseInt(range[0]), Integer.parseInt(range[1]) + 1).boxed().toList();
    }
}
