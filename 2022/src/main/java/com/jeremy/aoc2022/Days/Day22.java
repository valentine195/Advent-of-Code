package com.jeremy.aoc2022.Days;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.jeremy.aoc2022.Day;
import com.jeremy.aoc2022.utils.Coord;

public class Day22 extends Day {

    enum Direction {
        Right,
        Down,
        Left,
        Up;

        private static final Direction[] vals = values();

        public Direction counterclockwise() {
            return vals[Math.floorMod(this.ordinal() - 1, vals.length)];
        }

        public Direction clockwise() {
            return vals[Math.floorMod(this.ordinal() + 1, vals.length)];
        }

        public Coord step() {
            switch (this.ordinal()) {
                case 0:
                    return new Coord(1, 0);
                case 1:
                    return new Coord(0, 1);
                case 2:
                    return new Coord(-1, 0);
                case 3:
                    return new Coord(0, -1);
            }
            throw new IllegalArgumentException();
        }
    }

    class Me extends Coord {
        Direction heading;

        Me(int x, int y, Direction heading) {
            super(x, y);
            this.heading = heading;
        }

        void move() {
            switch (heading) {
                case Right: {
                    this.x += 1;
                }
                case Left: {
                    this.x -= 1;
                }
                case Down: {
                    this.y += 1;
                }
                case Up: {
                    this.y -= 1;
                }
            }
        }

        void turn(boolean clockwise) {
            if (clockwise) {
                this.heading = this.heading.clockwise();
            } else {
                this.heading = this.heading.counterclockwise();
            }
        }
    }

    public Day22(String input) {
        super(input);
    }

    public Day22() {
        super();
    }

    HashMap<Integer, Integer> rowMin = new HashMap<>();
    HashMap<Integer, Integer> rowMax = new HashMap<>();
    HashMap<Integer, Integer> colMin = new HashMap<>();
    HashMap<Integer, Integer> colMax = new HashMap<>();
    HashMap<Integer, HashMap<Integer, Boolean>> map = new HashMap<>();

    HashMap<Integer, HashMap<Integer, Boolean>> generate() {
        String[] split = INPUT.split("\n\n");
        String mapString = split[0];
        String[] rows = mapString.split("\n");
        for (int row = 0; row < rows.length; row++) {
            HashMap<Integer, Boolean> rowMap = new HashMap<>();
            map.put(row, rowMap);
            rowMin.put(row, Integer.MAX_VALUE);
            rowMax.put(row, Integer.MIN_VALUE);
            List<Integer> cols = rows[row].chars().boxed().toList();
            for (int col = 0; col < cols.size(); col++) {
                Integer c = cols.get(col);
                if (c == ' ')
                    continue;
                if (c == '#')
                    rowMap.put(col, false);
                if (c == '.')
                    rowMap.put(col, true);
                rowMin.put(row, Math.min(rowMin.get(row), col));
                rowMax.put(row, Math.max(rowMax.get(row), col));
                colMax.put(col, Math.max(colMax.getOrDefault(col, Integer.MIN_VALUE), row));
                colMin.put(col, Math.min(colMin.getOrDefault(col, Integer.MAX_VALUE), row));
            }
        }
        return map;
    }

    List<String> steps() {

        String[] split = INPUT.split("\n\n");
        String walkString = split[1].trim();
        List<String> steps = List.of(walkString.split("((?=L|R)|(?<=L|R))"));

        return steps;
    }

    @Override
    public String runPart1() {
        generate();
        var steps = steps();
        Me me = new Me(Collections.min(map.get(0).keySet()), 0, Direction.Right);
        for (String step : steps) {
            switch (step) {
                case "R": {
                    me.turn(true);
                    break;
                }
                case "L": {
                    me.turn(false);
                    break;
                }
                default: {
                    int amount = Integer.parseInt(step);
                    for (int i = 0; i < amount; i++) {
                        Coord next = me.sum(me.heading.step());
                        switch (me.heading) {
                            case Right: {
                                if (next.x > rowMax.get((int) next.y)) {
                                    next.x = rowMin.get((int) next.y);
                                }
                                break;
                            }
                            case Left: {
                                if (next.x < rowMin.get((int) next.y)) {
                                    next.x = rowMax.get((int) next.y);
                                }
                                break;
                            }
                            case Down: {
                                if (next.y > colMax.get((int) next.x)) {
                                    next.y = colMin.get((int) next.x);
                                }
                                break;
                            }
                            case Up: {
                                if (next.y < colMin.get((int) next.x)) {
                                    next.y = colMax.get((int) next.x);
                                }
                                break;
                            }
                        }

                        if (checkLocation(next)) {
                            me.x = next.x;
                            me.y = next.y;
                        }
                    }
                }
            }

        }
        return "" + (1000 * (me.y + 1) + 4 * (me.x + 1) + me.heading.ordinal());
    }

    Coord getMovement(Coord current, Direction heading) {

        return current.copy();
    }

    boolean checkLocation(Coord coord) {
        return checkLocation(coord.x, coord.y);
    }

    boolean checkLocation(long x, long y) {
        return map.get((int) y).get((int) x);
    }

    @Override
    public String runPart2() {
        
        return null;
    }
}
