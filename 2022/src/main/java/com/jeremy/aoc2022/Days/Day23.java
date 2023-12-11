package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jeremy.aoc2022.Day;
import com.jeremy.aoc2022.utils.Coord;

public class Day23 extends Day {

    enum Direction {
        North,
        South,
        West,
        East;

        static Direction[] valuesAtIndex(int index) {
            Direction[] values = values();
            int start = Math.floorMod(index, values.length);
            Direction[] arr = new Direction[values.length];
            for (int i = 0; i < values.length; i++) {
                arr[i] = values[Math.floorMod(i + start, values.length)];
            }
            return arr;
        }

        int[][] offsets() {
            return switch (this.ordinal()) {
                case 0 -> new int[][] { { -1, -1 }, { 0, -1 }, { 1, -1 } };
                case 1 -> new int[][] { { -1, 1 }, { 0, 1 }, { 1, 1 } };
                case 2 -> new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 } };
                case 3 -> new int[][] { { 1, -1 }, { 1, 0 }, { 1, 1 } };
                default -> throw new IllegalArgumentException();
            };
        }

        Coord coord() {
            return switch (this.ordinal()) {
                case 0 -> new Coord(0, -1);
                case 1 -> new Coord(0, 1);
                case 2 -> new Coord(-1, 0);
                case 3 -> new Coord(1, 0);
                default -> throw new IllegalArgumentException();
            };
        }
    }

    class Elf extends Coord {
        boolean hasNeighbor = false;
        Coord pending;
        int index = 0;

        Elf(int x, int y) {
            super(x, y);
        }

        // Moves and returns previous position.
        Coord move() {
            Coord clone = copy();
            super.set(pending);
            hasNeighbor = false;
            pending = null;
            return clone;
        }

        boolean decide(HashMap<Integer, HashMap<Integer, Elf>> map) {

            index++;
            if (!hasNeighbor) {
                List<Elf> close = new ArrayList<>();
                for (Coord coord : super.allNeighbors()) {
                    if (!map.containsKey((int) coord.y))
                        continue;
                    if (!map.get((int) coord.y).containsKey((int) coord.x))
                        continue;
                    if (map.get((int) coord.y).get((int) coord.x) == null)
                        continue;
                    Elf add = map.get((int) coord.y).get((int) coord.x);
                    add.hasNeighbor = true;
                    close.add(add);
                }
                hasNeighbor = close.size() > 0;
            }
            if (!hasNeighbor)
                return false;

            for (Direction direction : Direction.valuesAtIndex(index - 1)) {
                if (!mapHasNeighborInDirection(map, direction)) {
                    pending = super.sum(direction.coord());
                    return true;
                }
            }

            return false;
        }

        boolean mapHasNeighborInDirection(HashMap<Integer, HashMap<Integer, Elf>> map, Direction direction) {
            for (int[] offset : direction.offsets()) {
                int xOff = (int) x + offset[0];
                int yOff = (int) y + offset[1];
                if (map.containsKey(yOff) && map.get(yOff).containsKey(xOff) && map.get(yOff).get(xOff) != null)
                    return true;
            }
            return false;
        }
    }

    public Day23(String input) {
        super(input);
    }

    public Day23() {
        super();
    }

    HashMap<Integer, HashMap<Integer, Elf>> generate() {
        HashMap<Integer, HashMap<Integer, Elf>> map = new HashMap<>();
        elves = new ArrayList<>();
        for (int row = 0; row < MATCHES.size(); row++) {
            List<Integer> rowLine = MATCHES.get(row).chars().boxed().toList();
            HashMap<Integer, Elf> rowMap = new HashMap<>();
            map.put(row, rowMap);
            for (int col = 0; col < rowLine.size(); col++) {
                if (rowLine.get(col) == '#') {
                    Elf elf = new Elf(col, row);
                    rowMap.put(col, elf);
                    elves.add(elf);
                } else {
                    rowMap.put(col, null);
                }
            }
        }

        return map;
    }

    List<Elf> elves;

    @Override
    public String runPart1() {
        var map = generate();
        /* draw(map); */

        for (int i = 0; i < 10; i++) {
            HashMap<Coord, List<Elf>> canMove = new HashMap<>();
            for (Elf elf : elves) {
                boolean decide = elf.decide(map);
                if (decide) {
                    List<Elf> list = canMove.getOrDefault(elf.pending, new ArrayList<>());
                    list.add(elf);
                    canMove.put(elf.pending, list);
                }
            }

            List<List<Elf>> movable = canMove.values().stream().filter(list -> list.size() == 1).toList();
            for (List<Elf> list : movable) {
                Elf elf = list.get(0);
                Coord previous = elf.move();
                map.get((int) previous.y).put((int) previous.x, null);
                HashMap<Integer, Elf> row = map.getOrDefault((int) elf.y, new HashMap<>());
                row.put((int) elf.x, elf);
                map.put((int) elf.y, row);
            }
            /* draw(map); */
        }

        int[][] bounds = bounds(map);
        int ground = 0;
        for (int row = bounds[0][0]; row <= bounds[0][1]; row++) {
            for (int col = bounds[1][0]; col <= bounds[1][1]; col++) {
                if (!map.containsKey(row) || !map.get(row).containsKey(col) || map.get(row).get(col) == null)
                    ground++;
            }
        }

        return "" + ground;
    }

    int round = 0;

    void draw(HashMap<Integer, HashMap<Integer, Elf>> map) {
        int[][] bounds = bounds(map);
        System.out.println("== Round " + round + " ==");
        round++;
        for (int row = bounds[0][0]; row <= bounds[0][1]; row++) {
            for (int col = bounds[1][0]; col <= bounds[1][1]; col++) {
                if (map.containsKey(row) && map.get(row).containsKey(col) && map.get(row).get(col) != null) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    int[][] bounds(HashMap<Integer, HashMap<Integer, Elf>> map) {

        int rowMin = Integer.MAX_VALUE;
        int rowMax = Integer.MIN_VALUE;
        int colMin = Integer.MAX_VALUE;
        int colMax = Integer.MIN_VALUE;
        for (int num : map.keySet()) {
            HashMap<Integer, Elf> row = map.get(num);
            if (row.size() == 0)
                continue;
            if (row.values().stream().filter(elf -> elf != null).toList().size() == 0)
                continue;
            rowMin = Math.min(num, rowMin);
            rowMax = Math.max(rowMax, num);
            for (int col : row.keySet()) {
                if (row.get(col) == null)
                    continue;
                colMin = Math.min(colMin, col);
                colMax = Math.max(colMax, col);
            }

        }

        return new int[][] { { rowMin, rowMax }, { colMin, colMax } };
    }

    @Override
    public String runPart2() {
        var map = generate();
        /* draw(map); */
        int round = 0;
        while (true) {
            HashMap<Coord, List<Elf>> canMove = new HashMap<>();
            for (Elf elf : elves) {
                boolean decide = elf.decide(map);
                if (decide) {
                    List<Elf> list = canMove.getOrDefault(elf.pending, new ArrayList<>());
                    list.add(elf);
                    canMove.put(elf.pending, list);
                }
            }
            List<List<Elf>> movable = canMove.values().stream().filter(list -> list.size() == 1).toList();

            for (List<Elf> list : movable) {
                Elf elf = list.get(0);
                Coord previous = elf.move();
                map.get((int) previous.y).put((int) previous.x, null);
                HashMap<Integer, Elf> row = map.getOrDefault((int) elf.y, new HashMap<>());
                row.put((int) elf.x, elf);
                map.put((int) elf.y, row);
            }
            round++;
            if (movable.size() == 0) {
                break;
            }
        }
        return "" + round;
    }
}
