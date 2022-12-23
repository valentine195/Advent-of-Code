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
        Monkey sibling;
        Monkey parent;
        String operation;
        public boolean first;

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
            if (children.size() > 0) {
                return performOperation(children.get(0).getNumber(), children.get(1).getNumber());
            } else {
                return number;
            }
        }

        void setInvertedOperation(String parentOperation) {
            if (parentOperation.equals("+")) {
                operation = "-";
            }
            if (parentOperation.equals("*")) {
                operation = "/";
            }
            if (parentOperation.equals("-")) {
                if (first) {
                    operation = "+";
                } else {
                    operation = "-";
                }
            }
            if (parentOperation.equals("/")) {
                if (first) {
                    operation = "*";
                } else {
                    operation = "/";
                }
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

        var monkeys = buildMonkeys();

        return "" + (long) monkeys.get("root").getNumber();
    }

    @Override
    public String runPart2() {

        var monkeys = buildMonkeys();
        Monkey root = monkeys.get("root");
        Monkey me = monkeys.get("humn");
        Monkey check = monkeys.get("humn");
        List<String> path = new ArrayList<>();
        path.add("humn");
        while (check.parent != null) {
            path.add(check.parent.name);
            check = check.parent;
        }
        String otherName = root.childrenNames.stream().filter(c -> !path.contains(c)).findFirst().get();

        for (String name : path) {
            Monkey m = monkeys.get(name);
            m.number = 0;
            m.children.clear();

            if (m.parent != null) {
                if (m.parent.name.equals("root")) {
                    m.number = monkeys.get(otherName).getNumber();
                    continue;
                }
                if (m.first || m.parent.operation.equals("*") || m.parent.operation.equals("+")) {
                    m.children.add(m.parent);
                    m.children.add(m.sibling);
                } else {
                    m.children.add(m.sibling);
                    m.children.add(m.parent);
                }
                m.setInvertedOperation(m.parent.operation);
            }
        }
        return "" + (long) me.getNumber();
    }

    HashMap<String, Monkey> buildMonkeys() {
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
                    monkeys.get(child).parent = monkey;
                }
                monkey.children.get(0).sibling = monkey.children.get(1);
                monkey.children.get(0).first = true;
                monkey.children.get(1).sibling = monkey.children.get(0);
            }
        }
        return monkeys;
    }
}
