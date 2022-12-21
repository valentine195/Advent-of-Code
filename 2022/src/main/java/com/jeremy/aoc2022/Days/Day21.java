package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeremy.aoc2022.Day;

public class Day21 extends Day {

    class Monkey {
        String name;
        double number;
        List<String> childrenNames = new ArrayList<>();;
        List<Monkey> children = new ArrayList<>();
        String operation;

        Monkey(String name, double number) {
            this.name = name;
            this.number = number;
        }

        Monkey(String name, String operation, List<String> children) {
            this.name = name;
            this.operation = operation;
            children.stream().forEach(c -> childrenNames.add(c));
            ;
        }

        void addChild(Monkey monkey) {
            children.add(monkey);
        }

        private double performOperation(double first, double second) {
            switch (operation) {
                case "+": {
                    return first + second;
                }
                case "-": {
                    return first - second;
                }
                case "*": {
                    return first * second;
                }
                case "/": {
                    return first / second;
                }
                default: {
                    throw new Error("Invalid operation");
                }
            }
        }

        double getNumber() {
            if (operation != null) {
                return performOperation(children.get(0).getNumber(), children.get(1).getNumber());
            } else {
                return number;
            }
        }
    }

    public Day21(String input) {
        super(input);
    }

    public Day21() {
        super();
    }

    Pattern MONKEY_TARGETS = Pattern
            .compile("(?<name>\\w{4}): (?<first>\\w{4}) (?<operation>[*/+-]) (?<second>\\w{4})");
    Pattern MONKEY_NUMBER = Pattern.compile("(?<name>\\w{4}): (?<number>\\d+)");

    @Override
    public String runPart1() {
        HashMap<String, Monkey> monkeys = new HashMap<>();
        for (String line : MATCHES) {
            Matcher targetMatch = MONKEY_TARGETS.matcher(line);
            if (targetMatch.matches()) {
                List<String> targets = new ArrayList<>();
                String name = targetMatch.group("name");
                String operation = targetMatch.group("operation");
                String first = targetMatch.group("first");
                String second = targetMatch.group("second");
                targets.add(first);
                targets.add(second);
                monkeys.put(name, new Monkey(name, operation, targets));
            }
            Matcher numberMatcher = MONKEY_NUMBER.matcher(line);
            if (numberMatcher.matches()) {
                String name = numberMatcher.group("name");
                Double number = Double.parseDouble(numberMatcher.group("number"));
                monkeys.put(name, new Monkey(name, number));
            }
        }
        for (String name : monkeys.keySet()) {
            Monkey monkey = monkeys.get(name);
            if (monkey.childrenNames.size() > 0) {
                for (String child : monkey.childrenNames) {
                    monkey.addChild(monkeys.get(child));
                }
            }
        }

        System.out.println((long) monkeys.get("root").getNumber());

        return null;
    }

    @Override
    public String runPart2() {
        // TODO Auto-generated method stub
        return null;
    }
}
