package com.jeremy.aoc2022.Days;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.jeremy.aoc2022.Day;
import com.jeremy.aoc2022.utils.Coord;

public class Day24 extends Day {
    static enum Direction {
        North,
        South,
        West,
        East;

        String character() {
            return switch (this.ordinal()) {
                case 0 -> "^";
                case 1 -> "v";
                case 2 -> "<";
                case 3 -> ">";
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

        static Direction from(char dir) {
            return switch (dir) {
                case '^' -> Direction.North;
                case '>' -> Direction.East;
                case 'v' -> Direction.South;
                case '<' -> Direction.West;
                default -> throw new IllegalArgumentException();
            };
        }
    }

    static class Obstacle extends Coord {
        Boolean isWall() {
            return wall;
        }

        Direction dir;
        int min;
        int max;
        boolean wall;

        Obstacle(int x, int y) {
            super(x, y);
            this.wall = true;
        }

        Obstacle(int x, int y, Direction dir, int min, int max) {
            super(x, y);
            this.wall = false;
            this.dir = dir;
            this.min = min;
            this.max = max;
        }

        Obstacle step() {
            if (dir == null)
                return new Obstacle((int) x, (int) y);
            Coord clone = sum(dir.coord());
            switch (dir) {
                case North: {
                    if (clone.y < min) {
                        clone.y = max;
                    }
                    break;
                }
                case South: {
                    if (clone.y > max) {
                        clone.y = min;
                    }
                    break;
                }
                case East: {
                    if (clone.x > max) {
                        clone.x = min;
                    }
                    break;
                }
                case West: {
                    if (clone.x < min) {
                        clone.x = max;
                    }
                    break;
                }

            }
            return new Obstacle((int) clone.x, (int) clone.y, dir, min, max);
        }
    }

    static class Map {
        List<Obstacle> obstacles = new ArrayList<>();
        Coord start;
        Coord end;

        String input;

        Map(String input) {
            this.input = input;
        }

        List<Obstacle> step(List<Obstacle> obstacles) {
            List<Obstacle> clone = new ArrayList<>();
            for (Obstacle obstacle : obstacles) {
                if (obstacle.isWall()) {
                    clone.add(obstacle);
                    continue;
                }
                clone.add(obstacle.step());
            }
            return clone;
        }

        record State(Coord position, int minute, boolean end, boolean start) {
        }

        HashMap<Integer, List<Obstacle>> blizzardCache = new HashMap<>();

        List<Obstacle> getBlizzardsAtMinute(int minute) {
            if (!blizzardCache.containsKey(minute)) {
                blizzardCache.put(minute, step(blizzardCache.get(minute - 1)));
            }

            return blizzardCache.get(minute);
        }

        int traverseWithQueue() {
            return traverseWithQueue(false);
        }

        int traverseWithQueue(boolean part2) {

            ArrayDeque<State> queue = new ArrayDeque<>();
            queue.add(new State(start, 0, false, false));
            final Set<State> checkedStates = new HashSet<>();
            while (!queue.isEmpty()) {
                State state = queue.removeFirst();
                if (checkedStates.contains(state)) {
                    continue;
                }

                checkedStates.add(state);

                // pre blizzard move
                /*
                 * draw(state.position(), state.minute(), getBlizzardsAtMinute(state.minute()));
                 */

                if (state.position().equals(end)) {
                    if (!part2 || (state.end() && state.start()))
                        return state.minute();
                    state = new State(state.position(), state.minute(), true, state.start());
                }
                if (state.position().equals(start) && state.end() && part2) {
                    state = new State(state.position(), state.minute(), state.end(), true);
                }
                // post blizzard move
                List<Obstacle> blizzards = getBlizzardsAtMinute(state.minute() + 1);
                /* draw(pos, state.minute(), blizzards); */
                if (!state.position().equals(start)
                        && !blizzards.contains(new Obstacle((int) state.position().x, (int) state.position().y))) {
                    queue.add(new State(state.position(), state.minute() + 1, state.end(), state.start()));
                }
                for (Coord neighbor : state.position().directNeighbors()) {
                    if (blizzards.contains(new Obstacle((int) neighbor.x, (int) neighbor.y)))
                        continue;
                    queue.add(new State(neighbor, state.minute() + 1, state.end(), state.start()));
                }
            }

            return 0;
        }

        void draw(Coord position, int minute, List<Obstacle> obstacles) {
            String[] lines = input.split("\n");
            System.out.println("Minute " + minute + ":");
            for (int row = 0; row < lines.length; row++) {
                String line = lines[row];
                String cur = "";

                for (int col = 0; col < line.length(); col++) {
                    if (position.equals(new Coord(col, row))) {
                        cur += "E";
                    } else if (obstacles.contains(new Obstacle(col, row))) {
                        Obstacle temp = new Obstacle(col, row);
                        Obstacle obstacle = obstacles.get(obstacles.indexOf(temp));
                        if (obstacle.isWall()) {
                            cur += "#";
                        } else {
                            var matches = obstacles.stream().filter(c -> c.equals(temp)).toList();
                            if (matches.size() == 1) {
                                cur += obstacle.dir.character();
                            } else {
                                cur += matches.size();
                            }
                        }
                    } else {
                        cur += ".";
                    }
                }
                System.out.println(cur);
            }
            System.out.println("");
        }

        static Map generate(String input) {
            Map map = new Map(input);
            String[] lines = input.split("\n");
            int maxX = lines[1].length() - 2;
            int maxY = lines.length - 2;
            for (int row = 0; row < lines.length; row++) {
                String line = lines[row];
                for (int col = 0; col < line.length(); col++) {
                    switch (line.charAt(col)) {
                        case '.': {
                            if (row == 0) {
                                map.start = new Coord(col, row);
                                for (int i : new int[] { -1, 0, 1 }) {
                                    map.obstacles.add(new Obstacle(col + i, row - 1));
                                }
                            }
                            if (row == lines.length - 1) {
                                map.end = new Coord(col, row);
                                for (int i : new int[] { -1, 0, 1 }) {
                                    map.obstacles.add(new Obstacle(col + i, row + 1));
                                }
                            }
                            break;
                        }
                        case '#': {
                            map.obstacles.add(new Obstacle(col, row));
                            break;
                        }
                        default: {
                            Integer max = null;
                            switch (line.charAt(col)) {
                                case '^' -> max = maxY;
                                case 'v' -> max = maxY;
                                case '<' -> max = maxX;
                                case '>' -> max = maxX;
                            }
                            if (max == null) {
                                throw new ExceptionInInitializerError();
                            }
                            map.obstacles.add(new Obstacle(col, row, Direction.from(line.charAt(col)), 1, max));
                            break;
                        }
                    }
                }
            }
            map.blizzardCache.put(0, map.obstacles);
            return map;
        }

    }

    public Day24(String input) {
        super(input);
    }

    public Day24() {
        super();
    }

    public String runPart1() {
        var map = Map.generate(INPUT);
        return "" + map.traverseWithQueue();
    }

    @Override
    public String runPart2() {
        var map = Map.generate(INPUT);
        return "" + map.traverseWithQueue(true);
    }
}
