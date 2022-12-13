package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import com.jeremy.aoc2022.Day;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
class Point {
    final int x;
    final int y;

    @Getter
    @Setter
    int weight;

    @Getter
    @Setter
    private Integer distance = Integer.MAX_VALUE;

    @Getter
    @Setter
    private boolean visited = false;

    boolean equals(Point point) {
        return point.x == x && point.y == y;
    }

}

class Grid {
    HashMap<Integer, HashMap<Integer, Point>> weights = new HashMap<>();

    Point start;
    Point end;

    Grid(String input) {
        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            if (!weights.containsKey(y)) {
                weights.put(y, new HashMap<>());
            }
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                Point point = new Point(x, y);
                int weight = line.charAt(x);
                if (line.charAt(x) == 'S') {
                    start = point;
                    weight = 'a' - 1;
                } else if (line.charAt(x) == 'E') {
                    end = point;
                    weight = 'z' + 1;
                }
                point.setWeight(weight);
                weights.get(point.y).put(point.x, point);

            }
        }
    }

    List<Point> getNeighbors(Point point) {
        ArrayList<Point> list = new ArrayList<>();
        List<Integer[]> offsets = new ArrayList<>(Arrays.asList(new Integer[] { -1, 0 }, new Integer[] { 1, 0 },
                new Integer[] { 0, -1 }, new Integer[] { 0, 1 }));
        for (Integer[] offset : offsets) {
            int x = point.x + offset[0];
            int y = point.y + offset[1];
            if (weights.containsKey(y) && weights.get(y).containsKey(x)) {
                list.add(weights.get(y).get(x));
            }
        }
        return list;
    }

    Integer getWeight(Point point) {
        return weights.get(point.y).get(point.x).getWeight();
    }

    void draw(ArrayList<Point> path) {
        HashMap<Integer, HashMap<Integer, String>> pathStr = new HashMap<>();
        for (int y = 0; y < weights.size(); y++) {
            if (!pathStr.containsKey(y)) {
                pathStr.put(y, new HashMap<>());
            }
            for (int x = 0; x < weights.get(y).size(); x++) {
                pathStr.get(y).put(x, ".");
            }
        }
        for (int i = path.size() - 1; i >= 0; i--) {
            Point current = path.get(i);
            if (i == 0) {
                pathStr.get(current.y).put(current.x, "E");
                break;
            }
            Point target = path.get(i - 1);
            if (target.x > current.x) {
                pathStr.get(current.y).put(current.x, ">");
            } else if (target.x < current.x) {
                pathStr.get(current.y).put(current.x, "<");

            } else if (target.y > current.y) {
                pathStr.get(current.y).put(current.x, "v");

            } else if (target.y < current.y) {
                pathStr.get(current.y).put(current.x, "^");
            }
        }
        for (HashMap<Integer, String> line : pathStr.values()) {
            System.out.println(String.join("", line.values()));
        }
    }

    public int traverse(Point point) {
        return traverse(point, false);
    }

    void reset() {
        for (HashMap<Integer, Point> rows : weights.values()) {
            for (Point point : rows.values()) {
                point.setDistance(Integer.MAX_VALUE);
                point.setVisited(false);
            }
        }
    }

    public int traverse(Point point, boolean part2) {

        PriorityQueue<Point> queue = new PriorityQueue<>(1, (Point p, Point n) -> p.getDistance() - n.getDistance());
        point.setDistance(0);
        queue.add(point);

        while (queue.size() > 0) {
            Point node = queue.poll();
            for (Point edge : getNeighbors(node)) {
                if (!edge.isVisited() && (node.getWeight() - edge.getWeight() < 2)) {
                    if (edge.equals(start) || (part2 && edge.getWeight() == 'a')) {
                        // remove end;
                        return node.getDistance() - 1;
                    } else {
                        edge.setVisited(true);
                        edge.setDistance(Math.min(node.getDistance() + 1, edge.getDistance()));
                        queue.add(edge);
                    }
                }
            }
        }
        throw new Error("No path found.");
    }
}

public class Day12 extends Day {

    public Day12() {
        super();
    }

    public Day12(String input) {
        super(input);
    }

    Grid grid;

    public void buildGrid() {
        grid = new Grid(INPUT);
    }

    public void run() {
        buildGrid();
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    @Override
    public String runPart1() {
        return "" + grid.traverse(grid.end);
    }

    @Override
    public String runPart2() {
        grid.reset();
        return "" + grid.traverse(grid.end, true);
    }

}