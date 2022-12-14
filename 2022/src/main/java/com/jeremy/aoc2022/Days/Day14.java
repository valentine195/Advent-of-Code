package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
class Node {

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int x;
    private int y;

    private boolean sand;

    private boolean wall;

    private boolean source;

    private boolean tried;

    String getChar() {
        String c;
        if (wall) {
            c = "#";
        } else if (sand) {
            c = "o";
        } else if (source) {
            c = "+";
        } else {
            c = ".";
        }
        return c;
    }
}

class Cave {
    // Y, X
    HashMap<Integer, HashMap<Integer, Node>> grid = new HashMap<>();
    Node sand = new Node(Integer.MIN_VALUE, Integer.MIN_VALUE);

    Node source;
    int MAX_X = Integer.MIN_VALUE;
    int MIN_X = Integer.MAX_VALUE;
    int MAX_Y = Integer.MIN_VALUE;
    int MIN_Y = Integer.MAX_VALUE;
    int FLOOR;
    boolean part2 = false;

    int execute() {
        return execute(false);
    }

    int execute(boolean draw) {

        int moves = 0;
        sand = new Node(source.getX(), source.getY());
        sand.setSand(true);

        while (check()) {
            boolean moved = move(sand);
            if (!moved) {
                moves++;
                if (sand.getX() == source.getX() && sand.getY() == source.getY()) {
                    break;
                }
                addNode(sand.getX(), sand.getY(), sand);
                sand = new Node(source.getX(), source.getY());
                sand.setSand(true);
            }
            if (draw) {
                try {
                    Thread.sleep(50);
                } catch (Exception e) {

                }
                draw();
            }
        }
        return moves;
    }

    boolean check() {
        return part2 || sand.getY() <= MAX_Y;
    }

    boolean move(Node sand) {
        int y = sand.getY();
        int x = sand.getX();
        sand.setTried(true);
        if (part2 && y + 1 >= FLOOR) {
            return false;
        }
        if (!grid.containsKey(y + 1) || !grid.get(y + 1).containsKey(x)) {
            sand.setY(y + 1);
            return true;
        }
        if (!grid.get(y + 1).containsKey(x - 1)) {
            sand.setX(x - 1);
            sand.setY(y + 1);
            return true;
        }
        if (!grid.get(y + 1).containsKey(x + 1)) {
            sand.setX(x + 1);
            sand.setY(y + 1);
            return true;
        }
        return false;
    }

    Node addNode(int x, int y) {
        return addNode(x, y, false);
    }

    Node addNode(int x, int y, boolean wall) {
        Node node = new Node(x, y);

        node.setWall(wall);

        return addNode(x, y, node);
    }

    Node addNode(int x, int y, Node node) {
        if (!grid.containsKey(y)) {
            grid.put(y, new HashMap<>());
        }
        if (x > MAX_X) {
            MAX_X = x;
        }
        if (x < MIN_X) {
            MIN_X = x;
        }
        if (y > MAX_Y) {
            MAX_Y = y;
        }
        if (y < MIN_Y) {
            MIN_Y = y;
        }
        grid.get(y).put(x, node);
        return node;
    }

    Node getNode(int x, int y) {
        return grid.get(y).get(x);
    }

    void addWall(String path) {
        List<List<Integer>> walls = Stream.of(path.split(" -> ")).map(p -> p.split(","))
                .map(p -> Stream.of(p).map(i -> Integer.parseInt(i)).toList()).toList();
        if (walls.size() == 1) {
            List<Integer> wall = walls.get(0);
            addNode(wall.get(0), -1 * wall.get(1), true);
            return;
        }
        for (int i = 0; i < walls.size() - 1; i++) {
            List<Integer> wall = walls.get(i);
            List<Integer> next = walls.get(i + 1);

            int deltaX = next.get(0) - wall.get(0);
            int signX = (int) Math.signum(deltaX);
            int deltaY = next.get(1) - wall.get(1);
            int signY = (int) Math.signum(deltaY);

            for (int x = 0; x <= Math.abs(deltaX); x++) {
                addNode(wall.get(0) + (signX * x), wall.get(1), true);
            }
            for (int y = 0; y <= Math.abs(deltaY); y++) {
                addNode(wall.get(0), wall.get(1) + (y * signY), true);
            }

        }
    }

    HashMap<Integer, String> ANIMATION_STRINGS = new HashMap<>();

    void generateImage() {
        int rows = MAX_Y - MIN_Y + 1;
        if (part2) {
            rows += 2;
        }
        for (int i = 0; i < rows; i++) {
            String row = updateRow(i);
            ANIMATION_STRINGS.put(i, row);
        }
    }

    String updateRow(int index) {
        String row = "";
        if (grid.containsKey(index) || sand.getY() == index) {
            for (int j = -1; j < MAX_X - MIN_X + 2; j++) {
                if (sand.getY() == index && sand.getX() == j + MIN_X) {
                    row += "o";
                } else if (grid.containsKey(index) && grid.get(index).containsKey(j + MIN_X)) {
                    Node node = grid.get(index).get(j + MIN_X);
                    row += node.getChar();
                } else {
                    row += ".";
                }
            }
        } else {
            if (part2 && index == FLOOR) {
                row += "#".repeat(MAX_X - MIN_X + 3);
            } else {
                row += ".".repeat(MAX_X - MIN_X + 3);
            }
        }
        return row;
    }

    void draw() {
        clear();
        generateImage();
        for (String line : ANIMATION_STRINGS.values()) {
            System.out.println(line);
        }
    }

    void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static Cave build(String input) {
        return Cave.build(input, false);
    }

    static Cave build(String input, boolean part2) {
        Cave cave = new Cave();
        for (String path : input.split("\n")) {
            cave.addWall(path);
        }
        cave.source = cave.addNode(500, 0);
        cave.source.setSource(true);
        cave.part2 = part2;
        cave.FLOOR = cave.MAX_Y - cave.MIN_Y + 2;
        return cave;
    }
}

public class Day14 extends Day {

    public Day14() {
        super();
    }

    public Day14(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    @Override
    public String runPart1() {
        Cave cave = Cave.build(INPUT);
        return "" + cave.execute();
    }

    @Override
    public String runPart2() {
        Cave cave = Cave.build(INPUT, true);
        return "" + cave.execute();
    }

}