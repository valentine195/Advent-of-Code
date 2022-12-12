package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jeremy.aoc2022.Day;

public class Day03 extends Day {
    public Day03() {
        super();
    }

    public Day03(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {
        Integer priorities = 0;
        for (String rucksack : MATCHES) {
            List<List<Integer>> compartments = getCompartments(rucksack);
            Set<Integer> intersection = getIntersection(compartments);
            priorities += intersection.stream().mapToInt(Integer::intValue).sum();
        }
        return priorities.toString();
    }

    public String runPart2() {
        Integer priorities = 0;

        for (List<List<Integer>> group : partition(MATCHES)) {
            Set<Integer> intersection = getIntersection(group);
            priorities += intersection.stream().mapToInt(Integer::intValue).sum();
        }

        return priorities.toString();
    }

    public List<List<List<Integer>>> partition(List<String> input) {
        List<List<List<Integer>>> lists = new ArrayList<List<List<Integer>>>();
        for (int i = 0; i < input.size(); i += 3) {
            List<List<Integer>> list = new ArrayList<List<Integer>>();

            for (int j = 0; j < 3; j++) {
                List<Integer> priorities = convertToPriorities(input.get(i + j));
                list.add(priorities);
            }

            lists.add(list);

        }
        return lists;
    }

    public Set<Integer> getIntersection(List<List<Integer>> compartments) {
        Set<Integer> first = new HashSet<Integer>(compartments.get(0));

        for (List<Integer> compartment : compartments) {
            first.retainAll(new HashSet<Integer>(compartment));
        }

        return first;
    }

    public List<List<Integer>> getCompartments(String rucksack) {
        int middle = rucksack.length() / 2; // get the middle of the String
        List<Integer> first = convertToPriorities(rucksack.substring(0, middle));
        List<Integer> second = convertToPriorities(rucksack.substring(middle));
        return List.of(first, second);
    }

    public int getPriority(Integer item) {
        if (item >= 65 && item <= 90) {
            return item - 65 + 27;
        }
        return item - 97 + 1;
    }

    public List<Integer> convertToPriorities(String rucksack) {
        return rucksack.chars().boxed().map(c -> getPriority(c)).toList();
    }
}
