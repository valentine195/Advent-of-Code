package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jeremy.aoc2022.Day;

public class Day10 extends Day {
    public Day10() {
        super();
    }

    public Day10(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    @Override
    public String runPart1() {
        int X = 1;
        int cycle = 0;
        int total = 0;
        for (String line : MATCHES) {
            int increment = 0, add = 0;
            if (line.equals("noop")) {
                increment = 1;
            } else {
                add = Integer.parseInt(line.split(" ")[1]);
                increment = 2;
            }
            for (int i = 0; i < increment; i++) {
                cycle++;
                if ((cycle - 20) % 40 == 0) {
                    total += (X * cycle);
                }
            }
            X += add;

        }
        return "" + total;
    }

    @Override
    public String runPart2() {
        String screen = "";
        int X = 1;
        int position = 0;
        for (String line : MATCHES) {
            int increment = 0, add = 0;
            if (line.equals("noop")) {
                increment = 1;
            } else {
                add = Integer.parseInt(line.split(" ")[1]);
                increment = 2;
            }
            for (int i = 0; i < increment; i++) {

                if (position >= X - 1 && position <= X + 1) {
                    screen += "#";
                } else {
                    screen += ".";
                }
                position++;
                if ((position) == 40) {
                    position = 0;
                    screen += "\n";
                }
            }
            X += add;
        }
        return screen;
    }

}