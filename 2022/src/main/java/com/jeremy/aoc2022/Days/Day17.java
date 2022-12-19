package com.jeremy.aoc2022.Days;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;
import com.jeremy.aoc2022.utils.Coord;

class Shape {
    List<Coord> coords;

    Shape() {
        this.coords = new ArrayList<>();
    }

    Shape(List<Coord> coords) {
        this.coords = coords;
    }

    long getTop() {
        return Collections.max(coords.stream().map(c -> c.y).collect(Collectors.toList()));
    }

    List<Coord> sumSelf(Coord coord) {
        for (Coord c : coords) {
            c.sumSelf(coord);
        }
        return coords;
    }

    static Shape getShape(int index) {
        List<Coord> coords = new ArrayList<>();
        switch (index) {
            case 0: {
                coords.add(new Coord(0, 0));
                coords.add(new Coord(1, 0));
                coords.add(new Coord(2, 0));
                coords.add(new Coord(3, 0));
                break;
            }
            case 1: {
                coords.add(new Coord(1, 0));
                coords.add(new Coord(0, 1));
                coords.add(new Coord(1, 1));
                coords.add(new Coord(2, 1));
                coords.add(new Coord(1, 2));
                break;
            }
            case 2: {
                coords.add(new Coord(0, 0));
                coords.add(new Coord(1, 0));
                coords.add(new Coord(2, 0));
                coords.add(new Coord(2, 1));
                coords.add(new Coord(2, 2));
                break;
            }
            case 3: {
                coords.add(new Coord(0, 0));
                coords.add(new Coord(0, 1));
                coords.add(new Coord(0, 2));
                coords.add(new Coord(0, 3));
                break;
            }
            case 4: {
                coords.add(new Coord(0, 0));
                coords.add(new Coord(1, 0));
                coords.add(new Coord(0, 1));
                coords.add(new Coord(1, 1));
                break;
            }
        }
        return new Shape(coords);
    }
}

public class Day17 extends Day {

    public Day17() {
        super();
    }

    public Day17(String input) {
        super(input);
    }

    public void run() {
        Instant start = Instant.now();
        System.out.println(runPart1() + " time: " + Duration.between(start, Instant.now()));
        start = Instant.now();
        System.out.println(runPart2() + " time: " + Duration.between(start, Instant.now()));
    }

    boolean isEmpty(Coord pos) {
        if (pos.x < 0 || pos.x > 6)
            return false;
        if (pos.y <= 0)
            return false;
        return !tower.contains(pos);
    }

    boolean checkCanMove(Shape rock, Coord dir) {
        return rock.coords.stream().allMatch(coord -> isEmpty(coord.sum(dir)));
    }

    Set<Coord> tower;
    HashMap<Long, HashMap<Integer, Coord>> cache;

    long execute(long iterations) {
        tower = new HashSet<>();
        cache = new HashMap<>();
        List<Integer> jets = INPUT.trim().chars().boxed().map(c -> (c - 61)).collect(Collectors.toList());
        int jet = 0;
        Coord down = new Coord(0, -1);
        long index = 0;
        long top = 0;
        // can't exit on first cycle because the floor changes things
        for (int i = 0; i < iterations; i++) {
            Shape shape = Shape.getShape((int) index);
            shape.sumSelf(new Coord(2, top + 4));
            cache.putIfAbsent(index, new HashMap<>());
            // because the inputs repeat, it should be cyclical...
            // detect a cycle and exit early
            // cache the shape index and jet index
            // Shape -> Jet -> (Iteration, Top at that iteration)
            if (cache.get(index).containsKey(jet)) {
                Coord cached = cache.get(index).get(jet);
                // cached.x is the iteration we last saw this combo at
                // cached.y is the top level at that iteration
                // remaining iterations % iterations between now and cached iteration should be
                // zero for a full cycle
                long completed = (iterations - i) % (i - cached.x);
                // check if we've completed a cycle
                if (completed == 0) {
                    // this is the remaining cycles.
                    long remainingCycles = (iterations - i) / (i - cached.x);
                    //top - cached.y is the height per cycle
                    return (top + (top - cached.y) * remainingCycles);
                }
            } else {
                cache.get(index).put(jet, new Coord(i, top));
            }
            while (true) {
                Coord dir = new Coord(jets.get(jet), 0);
                jet = (jet + 1) % jets.size();
                if (checkCanMove(shape, dir)) {
                    shape.sumSelf(dir);
                }
                if (checkCanMove(shape, down)) {
                    shape.sumSelf(down);
                } else {
                    break;
                }
            }
            tower.addAll(shape.coords);
            top = Math.max(top, shape.getTop());
            index = (index + 1) % 5;
        }
        return top;
    }

    @Override
    public String runPart1() {
        return "" + execute(2022);
    }

    @Override
    public String runPart2() {
        return "" + execute((long) 1e12);
    }

    void drawShape(Shape shape) {
        long y = Collections.max(shape.coords.stream().map(c -> c.y).collect(Collectors.toList()));
        long x = Collections.max(shape.coords.stream().map(c -> c.x).collect(Collectors.toList()));
        Set<Coord> coords = new HashSet<>(shape.coords);
        for (long row = y; row >= 0; row--) {
            String rowString = "";
            for (int col = 0; col <= x; col++) {
                if (coords.contains(new Coord(col, row))) {
                    rowString += "#";
                } else {
                    rowString += ".";
                }
            }
            System.out.println(rowString);
        }
        System.out.println("");
    }

    void drawTower(int top) {
        drawTower(top, new Shape());
    }

    void clear() {
        /*
         * System.out.print("\033[H\033[2J");
         * System.out.flush();
         */
        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }
    }

    void drawTower(int top, Shape shape) {
        clear();
        for (int i = top; i > 0; i--) {
            String row = "|";
            for (int col = 0; col < 7; col++) {
                Coord coord = new Coord(col, i);
                if (tower.contains(coord)) {
                    row += "#";
                } else if (shape.coords.contains(coord)) {
                    row += "@";
                } else {
                    row += ".";
                }
            }
            row += "|";
            System.out.println(row);
        }
        System.out.println("+-------+\n");
    }

}
