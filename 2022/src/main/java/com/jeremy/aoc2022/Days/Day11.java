package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

class Monkey {
    List<Integer> items = new ArrayList<>();
    HashMap<Boolean, Integer> targets = new HashMap<>();
    Long inspections = 0L;
    Integer test;
    Function<Integer, Integer> operation;
    Integer operand = 0;

    Monkey(String input) {
        String[] monkey = input.split("\n");
        items = Stream.of(monkey[1].substring(18).split(",")).map(c -> Integer.parseInt(c.trim()))
                .collect(Collectors.toList());
        if (monkey[2].charAt(25) == 'o') {
            operation = (old) -> old * old;
        } else if (monkey[2].charAt(23) == '*') {
            operand = Integer.parseInt(monkey[2].substring(25));
            operation = (old) -> old * operand;
        } else {
            operand = Integer.parseInt(monkey[2].substring(25));
            operation = (old) -> old + operand;
        }
        test = Integer.parseInt(monkey[3].substring(21));
        targets.put(true, Integer.parseInt(monkey[4].substring(29)));
        targets.put(false, Integer.parseInt(monkey[5].substring(30)));
    }
}

public class Day11 extends Day {

    List<Monkey> monkeys;

    public Day11() {
        super();
    }

    public Day11(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    @Override
    public String runPart1() {
        monkeys = Stream.of(INPUT.split("\n\n")).map(input -> new Monkey(input)).collect(Collectors.toList());
        return "" + play(20);
    }

    @Override
    public String runPart2() {
        monkeys = Stream.of(INPUT.split("\n\n")).map(input -> new Monkey(input)).collect(Collectors.toList());
        return "" + play(10000, true);
    }

    String play(int rounds) {
        return play(rounds, false);
    }

    String play(int rounds, boolean useMod) {
        Integer modulo = monkeys.stream().map(a -> a.test).reduce(1, (a, b) -> a * b);
        for (int i = 0; i < rounds; i++) {
            for (Monkey monkey : monkeys) {
                for (Integer item : monkey.items) {
                    monkey.inspections++;
                    Integer value = monkey.operation.apply(item);
                    if (useMod) {
                        value = value % modulo;
                    } else {
                        value = value / 3;
                    }
                    monkeys.get(monkey.targets.get(value % monkey.test == 0)).items.add(value);
                }
                monkey.items.clear();
            }
        }

        long[] maxArray = monkeys.stream().map(c -> c.inspections).sorted(Collections.reverseOrder()).limit(2)
                .mapToLong(c -> c).toArray();

        return "" + (maxArray[0] * maxArray[1]);
    }

}