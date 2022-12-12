package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jeremy.aoc2022.Day;

public class Day09 extends Day {
    public Day09() {
        super();
    }

    public Day09(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    @Override
    public String runPart1() {
        Set<String> visited = new HashSet<>();
        // starting
        // col, row
        int[] tail = new int[] { 0, 0 };
        int[] head = new int[] { 0, 0 };
        for (String line : MATCHES) {
            String[] split = line.split(" ");
            String direction = split[0];
            int moves = Integer.parseInt(split[1]);

            int[] transform = getTransform(direction);

            for (int i = 0; i < moves; i++) {
                head[0] += transform[0];
                head[1] += transform[1];

                if (tooFar(head, tail)) {
                    tail[0] += transform[0];
                    tail[1] += transform[1];
                    // sync row/col
                    switch (direction) {
                        case "U":
                        case "D": {
                            tail[1] = head[1];
                            break;
                        }
                        case "L":
                        case "R": {
                            tail[0] = head[0];
                            break;
                        }
                    }
                }

                if (visited.contains(tail[0] + "," + tail[1]))
                    continue;

                visited.add(tail[0] + "," + tail[1]);

            }

        }
        return "" + visited.size();
    }

    boolean tooFar(int[] head, int[] tail) {
        return tooFar(head, tail, 1);
    }

    boolean tooFar(int[] head, int[] tail, int dist) {
        return Math.abs(head[0] - tail[0]) > dist || Math.abs(head[1] - tail[1]) > dist;
    }

    @Override
    public String runPart2() {
        Set<String> visited = new HashSet<>();
        // starting
        // col, row
        List<int[]> knots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            knots.add(new int[] { 0, 0 });
        }
        int[] head = knots.get(0);
        for (String line : MATCHES) {
            String[] split = line.split(" ");
            String direction = split[0];
            int moves = Integer.parseInt(split[1]);

            int[] transform = getTransform(direction);

            for (int i = 0; i < moves; i++) {
                head[0] += transform[0];
                head[1] += transform[1];
                for (int knot = 1; knot < knots.size(); knot++) {
                    int[] prev = knots.get(knot - 1);
                    int[] tail = knots.get(knot);

                    if (!tooFar(prev, tail))
                        continue;
                    move(prev, tail);
                }
                int[] tail = knots.get(knots.size() - 1);
                if (visited.contains(tail[0] + "," + tail[1]))
                    continue;

                visited.add(tail[0] + "," + tail[1]);
            }

        }
        return "" + visited.size();
    }

    int[] move(int[] knot, int[] tail) {
        int dx = knot[0] - tail[0];
        int dy = knot[1] - tail[1];

        if (dx == 0 && Math.abs(dy) == 2) {
            tail[1] = tail[1] + (int) Math.signum(dy);
            return tail;
        }

        if (dy == 0 && Math.abs(dx) == 2) {
            tail[0] = tail[0] + (int) Math.signum(dx);
            return tail;
        }
        if (2 == Math.abs(dy)) {
            tail[1] += (int) Math.signum(dy);
            tail[0] += (int) Math.signum(dx);
        } else if (2 == Math.abs(dx)) {
            tail[0] += (int) Math.signum(dx);
            tail[1] += (int) Math.signum(dy);

        }

        return tail;

    }

    int sign(int num) {
        if (num < 0)
            return -1;
        return 1;
    }

    int[] getTransform(String direction) {
        switch (direction) {
            case "R": {
                return new int[] { 0, 1 };
            }
            case "L": {
                return new int[] { 0, -1 };
            }
            case "U": {
                return new int[] { 1, 0 };
            }
            case "D": {
                return new int[] { -1, 0 };
            }

        }
        throw new Error("Invalid direction!");
    }
}