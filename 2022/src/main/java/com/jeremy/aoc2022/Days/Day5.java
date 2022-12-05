package com.jeremy.aoc2022.Days;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

class Instruction {
    int amount;
    int from;
    int to;

    public Instruction(String amount, String from, String to) {
        this(Integer.parseInt(amount), Integer.parseInt(from), Integer.parseInt(to));
    }

    public Instruction(int amount, int from, int to) {
        this.amount = amount;
        this.from = from;
        this.to = to;
    }
}

class Crane {
    List<ArrayDeque<String>> stacks = new ArrayList<ArrayDeque<String>>();
    List<ArrayDeque<String>> clone = new ArrayList<ArrayDeque<String>>();

    public Crane() {
        this(0);
    }

    public Crane(int amount) {
        for (int i = 0; i < amount; i++) {
            stacks.add(new ArrayDeque<String>());
            clone.add(new ArrayDeque<String>());
        }
    }

    public String toString() {
        return stacks.toString();
    }

    public void move(Instruction instruction) {
        ArrayDeque<String> from = stacks.get(instruction.from - 1);
        ArrayDeque<String> to = stacks.get(instruction.to - 1);

        for (int i = 0; i < instruction.amount; i++) {
            to.addFirst(from.removeFirst());
        }
    }

    public void moveAll(Instruction instruction) {
        ArrayDeque<String> from = stacks.get(instruction.from - 1);
        ArrayDeque<String> to = stacks.get(instruction.to - 1);

        ArrayDeque<String> toAdd = new ArrayDeque<>();
        for (int i = 0; i < instruction.amount; i++) {
            toAdd.addLast(from.removeFirst());
        }
        for (int i = 0; i < instruction.amount; i++) {
            to.addFirst(toAdd.removeLast());
        }
    }

    public String getTop() {
        String top = "";
        for (ArrayDeque<String> stack : stacks) {
            top += stack.peekFirst();
        }
        return top;
    }

    public void addLast(int stack, String box) {
        stacks.get(stack).addLast(box);
        clone.get(stack).addLast(box);
    }

    public void reset() {
        stacks = new ArrayList<>(clone);
    }

}

public class Day5 extends Day {
    public Day5() {
        super();
    }

    public Day5(String input) {
        super(input);
    }

    List<Instruction> movements;

    Crane crane;

    @Override
    public void setInput(String input) {

        super.setInput(input);

        List<String> split = List.of(input.split("\\s+(?:\\d\\s+)+\\n\\n"));

        String pattern = "^([0-9 ]+)$";
        Pattern STACKS = Pattern.compile(pattern, Pattern.MULTILINE);
        Matcher match = STACKS.matcher(input);
        match.find();
        String[] columnArray = match.group().split("\\s+");

        int columns = Stream.of(columnArray).filter(c -> c.trim().length() > 0).toList().size();

        crane = new Crane(columns);

        for (String line : List.of(split.get(0).split("\n"))) {
            for (int i = 0; i < line.length(); i += 4) {
                String substring = line.substring(i, Math.min(line.length(), i + 4));
                if (substring.trim().length() > 0) {
                    crane.addLast(i / 4, substring.trim().replaceAll("[\\[\\]]", ""));
                }
            }
        }
        Pattern PATTERN = Pattern.compile("move (?<amount>\\d+) from (?<start>\\d+) to (?<end>\\d+)$",
                Pattern.MULTILINE);
        Matcher instructionMatcher = PATTERN.matcher(split.get(1));

        movements = new ArrayList<Instruction>();
        while (instructionMatcher.find()) {
            try {
                movements.add(new Instruction(instructionMatcher.group("amount"), instructionMatcher.group("start"),
                        instructionMatcher.group("end")));
            } catch (Exception e) {
                System.out.println("ERROR" + e.getLocalizedMessage());
            }
        }

    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {

        for (Instruction instruction : movements) {
            crane.move(instruction);
        }

        return crane.getTop();
    }

    public String runPart2() {

        crane.reset();
        for (Instruction instruction : movements) {
            crane.moveAll(instruction);
        }
        return crane.getTop();
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
