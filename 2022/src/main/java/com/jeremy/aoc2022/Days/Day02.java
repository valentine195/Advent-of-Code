package com.jeremy.aoc2022.Days;

import java.util.List;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

class Points {
    public static int LOSS = 0;
    public static int DRAW = 3;
    public static int WIN = 6;
    public static int ROCK = 1;
    public static int PAPER = 2;
    public static int SCISSORS = 3;

    static int getBeatingShape(String shape) {
        if (shape.equals("A"))
            return Points.PAPER;
        if (shape.equals("B"))
            return Points.SCISSORS;
        return Points.ROCK;
    }

    static int getLosingShape(String shape) {
        if (shape.equals("A"))
            return Points.SCISSORS;
        if (shape.equals("B"))
            return Points.ROCK;
        return Points.PAPER;
    }

    static int getShape(String shape) {
        switch (shape) {
            case "X":
            case "A":
                return Points.ROCK;
            case "Y":
            case "B":
                return Points.PAPER;
            case "Z":
            case "C":
                return Points.SCISSORS;
            default:
                throw new Error("Invalid shape");
        }
    }
}

public class Day02 extends Day {

    public Day02() {
        super();
    }

    public Day02(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    @Override
    public String runPart1() {
        Integer outcomes = 0;

        for (String line : MATCHES) {
            outcomes = outcomes + getResultPart1(line);
        }

        return outcomes.toString();
    }

    private int getResultPart1(String line) {

        List<Integer> shapes = Stream.of(line.split(" ")).map(shape -> Points.getShape(shape.trim())).toList();
        int delta = shapes.get(1) - shapes.get(0);
        int result = shapes.get(1);
        if (delta == 1 || delta == -2) {
            result += Points.WIN;
        } else if (delta == 0) {
            result += Points.DRAW;
        } else {
            result += Points.LOSS;
        }
        return result;

    }

    @Override
    public String runPart2() {
        Integer outcomes = 0;

        for (String line : MATCHES) {
            outcomes = outcomes + getResultPart2(line);
        }

        return outcomes.toString();
    }

    private int getResultPart2(String line) {
        List<String> shapes = List.of(line.split(" "));
        int outcome = 0;
        if (shapes.get(1).equals("X")) {
            // loss
            outcome += Points.LOSS + Points.getLosingShape(shapes.get(0));
        } else if (shapes.get(1).equals("Y")) {
            // draw
            outcome += Points.DRAW + Points.getShape(shapes.get(0));
        } else {
            // win
            outcome += Points.WIN + Points.getBeatingShape(shapes.get(0));
        }
        return outcome;
    }

}
