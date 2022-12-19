package com.jeremy.aoc2022.Days;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeremy.aoc2022.Day;

class Valve {
    private String name;

    public String getName() {
        return name;
    }

    private Set<String> targets;

    public Set<String> getTargets() {
        return targets;
    }

    private int flow;

    public int getFlow() {
        return flow;
    }

    private boolean opened = false;

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    Valve(String name, int flow, Set<String> targets) {
        this.name = name;
        this.flow = flow;
        this.targets = targets;
    }
}

class ValveGrid {
    HashMap<String, Valve> valves = new HashMap<>();

    String START = "AA";
    int minutes = 30;
    int openCost = 1;
    int moveCost = 1;

    Pattern VALVE = Pattern
            .compile(
                    "Valve (?<name>[A-Z]+) has flow rate=(?<flow>\\d+); tunnel(s)? lead(s)? to valve(s)? (?<targets>[\\s\\S]+)");

    void parseLine(String line) {
        // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB;
        Matcher matcher = VALVE.matcher(line);
        if (matcher.find()) {
            String name = matcher.group("name");
            String flow = matcher.group("flow");
            String targets = matcher.group("targets");
            Valve valve = new Valve(name, Integer.parseInt(flow), Set.of(targets.split(",")));
            valves.put(name, valve);
        }
    }

    void traverse(Valve node) {

    }

    public static ValveGrid build(List<String> lines) {
        ValveGrid grid = new ValveGrid();

        lines.stream().forEach(line -> grid.parseLine(line));

        return grid;
    }
}

public class Day16 extends Day {

    public Day16() {
        super();
    }

    public Day16(String input) {
        super(input);
    }

    public void run() {
        Instant start = Instant.now();
        System.out.println(runPart1() + " time: " + Duration.between(start, Instant.now()));
        start = Instant.now();
        System.out.println(runPart2() + " time: " + Duration.between(start, Instant.now()));
    }

    @Override
    public String runPart1() {
        /* ValveGrid grid = ValveGrid.build(MATCHES); */
        return "";
    }

    @Override
    public String runPart2() {
        return "";
    }

}
