package com.jeremy.aoc2022.Days;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jeremy.aoc2022.Day;
import com.jeremy.aoc2022.utils.Coord;

enum Jets {
    Left("<"),
    Right(">");

    public final String label;

    public String getLabel() {
        return label;
    }

    public Coord getCoord() {
        if (this == Jets.Right) {
            return Coord.RIGHT;
        }
        return Coord.LEFT;
    }

    private Jets(String label) {
        this.label = label;
    }

    static Jets from(String jet) {
        if (Jets.Right.getLabel().equals(jet)) {
            return Jets.Right;
        }
        return Jets.Left;
    }
}

class Shape {
    List<Coord> coords;

    Shape(List<Coord> coords) {
        this.coords = coords;
    }

    List<Coord> sumSelf(Coord coord) {
        for (Coord c : coords) {
            c.sumSelf(coord);
        }
        return coords;
    }

    static Shape getShape(int index) {
        List<Coord> coords = new ArrayList<>();
        if (index + 5 % 5 == 0) {
            coords.add(new Coord(0, 0));
            coords.add(new Coord(1, 0));
            coords.add(new Coord(2, 0));
            coords.add(new Coord(3, 0));
        }
        if (index + 6 % 5 == 0) {
            coords.add(new Coord(1, 0));
            coords.add(new Coord(0, 1));
            coords.add(new Coord(2, 1));
            coords.add(new Coord(1, 2));
        }
        if (index + 7 % 5 == 0) {
            coords.add(new Coord(0, 0));
            coords.add(new Coord(1, 0));
            coords.add(new Coord(2, 0));
            coords.add(new Coord(2, 1));
            coords.add(new Coord(2, 2));
        }
        if (index + 8 % 5 == 0) {
            coords.add(new Coord(0, 0));
            coords.add(new Coord(1, 0));
            coords.add(new Coord(0, 1));
            coords.add(new Coord(1, 1));

        }
        if (index + 9 % 5 == 0) {
            coords.add(new Coord(0, 0));
            coords.add(new Coord(0, 1));
            coords.add(new Coord(0, 2));
            coords.add(new Coord(0, 3));
        }
        return new Shape(coords);
    }
}

class AirJets {

    private final List<String> jets;
    private int index = 0;

    AirJets(String input) {
        jets = List.of(input.split(""));
    }

    public Jets next() {
        if (index >= jets.size()) {
            index = 0;
        }
        String jet = this.jets.get(index);
        index++;
        return Jets.from(jet);
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

    Set<Coord> tower = new HashSet<>();
    HashMap<Integer, Shape> cache = new HashMap<>();

    boolean empty(Coord pos) {
        return pos.x >= 0 && pos.x <= 6 && pos.y > 0 && !tower.contains((Coord) pos);
    }

    boolean check(Coord pos, Coord dir, Shape rock) { // all(empty(pos+dir+r) for r in rock)
        return rock.coords.stream().allMatch(coord -> empty(coord.sum(pos, dir)));
    }

    @Override
    public String runPart1() {
        // last four rows
        AirJets jets = new AirJets(INPUT);
        Coord down = new Coord(0, -1);
        int index = 0;
        int top = 0;
        for (int step = 0; step < 3; step++) {
            Coord start = new Coord(2, top + 4);
            Shape shape = Shape.getShape(index);
            index++;

            while (true) {
                Coord jet = jets.next().getCoord();
                if (check(start, jet, shape)) {
                    start.sumSelf(jet);
                }
                if (check(start, down, shape)) {
                    start.sumSelf(down);
                } else {
                    break;
                }
            }
            Set<Coord> union = new HashSet<>(shape.sumSelf(start));
            tower.addAll(union);
            top = Math.max(top, start.y + new int[] { 1, 0, 2, 2, 3 }[index]);
        }
        return "";
    }

    void addRow(HashMap<Integer, HashMap<Integer, Boolean>> grid, int index) {
        addRow(grid, index, false);
    }

    void addRow(HashMap<Integer, HashMap<Integer, Boolean>> grid, int index, boolean value) {
        grid.put(index, new HashMap<>());
        for (int j = 0; j < 7; j++) {
            grid.get(index).put(j, value);
        }
    }

    @Override
    public String runPart2() {
        return "";
    }

}
