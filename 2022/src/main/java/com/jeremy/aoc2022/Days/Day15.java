package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;

abstract class SensorOrBeacon {

    abstract String getChar();

    SensorOrBeacon(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    private final int x;

    int getY() {
        return y;
    }

    private final int y;
}

class Beacon extends SensorOrBeacon {
    @Override
    String getChar() {
        return "B";
    }

    public Beacon(int x, int y) {
        super(x, y);
    }
}

class Sensor extends SensorOrBeacon {
    private final Beacon beacon;

    public Beacon getBeacon() {
        return beacon;
    }

    Sensor(int x, int y, Beacon beacon) {
        super(x, y);
        this.beacon = beacon;
    }

    int getDistance() {
        return getDistance(this.beacon);
    }

    int getDistance(Beacon point) {
        return Math.abs(point.getX() - getX()) + Math.abs(point.getY() - getY());
    }

    int getDistance(int x, int y) {
        return Math.abs(x - getX()) + Math.abs(y - getY());
    }

    boolean canSeePoint(Beacon point) {
        if (point.getX() == beacon.getX() && point.getY() == beacon.getY())
            return false;
        return getDistance() >= getDistance(point);
    }

    @Override
    String getChar() {
        return "S";
    }
}

class CaveMap {
    // Y, X
    HashMap<Integer, HashMap<Integer, Boolean>> grid = new HashMap<>();
    List<Sensor> sensors = new ArrayList<>();

    boolean part2;
    int MINIMUM = 0;
    int MAXIMUM = 4000000;

    public void setMinMax(int MINIMUM, int MAXIMUM) {
        this.MINIMUM = MINIMUM;
        this.MAXIMUM = MAXIMUM;
        MIN_X = Math.max(MINIMUM, MIN_X);
        MIN_Y = Math.max(MINIMUM, MIN_Y);
        MAX_X = Math.min(MAXIMUM, MAX_X);
        MAX_Y = Math.min(MAXIMUM, MAX_X);
    }

    int MIN_X = part2 ? 0 : Integer.MAX_VALUE;
    int MIN_Y = part2 ? 0 : Integer.MAX_VALUE;
    int MAX_X = part2 ? 4000000 : Integer.MIN_VALUE;
    int MAX_Y = part2 ? 4000000 : Integer.MIN_VALUE;

    // each scanner is four lines... two positive slope (1) and two negative slope
    // (-1)
    // each beacon lies exactly on one of these four lines
    // y = x + a;
    // y = -x + b;
    // ...a is positive slope: (s_y - s_x) +/- r (left -> top, bottom -> right)
    // ...b is negative slope: (s_y + s_x) +/- r (left -> bottom, top -> right)
    // coefficients of line just outside scanner range are: a +/- 1, b +/- 1
    // the missing beacon has to be at an intersection of multiple scanners outside
    // line??
    // intersection of lines - cramer's (ax + by + c = 0) => -1x + 1y - a, 1x +
    // 1y - b:
    // (x_o, y_o) = ((b1c2 - b2c1)/(a1b2 - a2b1), (c1a2 - c2a1)/(a1b2 - a2b1)) =>
    // (1*-b - 1-a)/(-1*1 - 1*1), (-a*1 - -b*-1)/(-1*1 - 1*1)
    // => (x_o, y_o) = -1*(b - a)/-2, -1*(a + b)/2 => (b-a)/2, (a+b)/2
    HashSet<Integer> A_COEFFICIENTS = new HashSet<>();
    HashSet<Integer> B_COEFFICIENTS = new HashSet<>();

    Sensor addSensor(int x, int y, Beacon beacon) {
        Sensor sensor = new Sensor(x, y, beacon);
        sensors.add(sensor);
        setDimensions(sensor);
        grid.putIfAbsent(y, new HashMap<>());
        grid.get(y).put(x, true);
        grid.putIfAbsent(beacon.getY(), new HashMap<>());
        grid.get(beacon.getY()).put(beacon.getX(), true);
        int radius = sensor.getDistance();
        // add coefficients for external lines...
        for (int i : new int[] { -1, 1 }) {
            A_COEFFICIENTS.add((y - x) + i * (radius + 1));
            B_COEFFICIENTS.add((y + x) + i * (radius + 1));
        }

        return sensor;
    }

    static CaveMap build(List<String> input) {
        return CaveMap.build(input, false);
    }

    static CaveMap build(List<String> input, boolean part2) {
        CaveMap grid = new CaveMap();
        grid.part2 = part2;
        for (String line : input) {
            String[] split = line.split(":");
            Beacon sensorCoords = CaveMap.getCoords(split[0]);
            Beacon beacon = CaveMap.getCoords(split[1]);
            grid.addSensor(sensorCoords.getX(), sensorCoords.getY(), beacon);
        }
        return grid;
    }

    void setDimensions(Sensor sensor) {
        Beacon beacon = sensor.getBeacon();

        int left, right, top, bottom;
        left = sensor.getX() - sensor.getDistance();
        right = sensor.getX() + sensor.getDistance();
        top = sensor.getY() - sensor.getDistance();
        bottom = sensor.getY() + sensor.getDistance();

        MIN_X = Math.min(MIN_X, Math.min(left, beacon.getX()));
        MAX_X = Math.max(MAX_X, Math.max(right, beacon.getX()));
        MIN_Y = Math.min(MIN_Y, Math.min(top, beacon.getY()));
        MAX_Y = Math.max(MAX_Y, Math.max(bottom, beacon.getY()));
    }

    static Beacon getCoords(String definition) {
        List<Integer> list = Stream.of(definition.split(",")).map(s -> s.replaceAll("[^\\d-]", ""))
                .map(c -> Integer.parseInt(c)).toList();
        return new Beacon(list.get(0), list.get(1));
    }

    int findInTargetRow(int target) {
        int count = 0;
        for (int i = MIN_X; i <= MAX_X; i++) {
            if (grid.getOrDefault(target, new HashMap<>()).getOrDefault(i, false)) {
                continue;
            }
            Beacon point = new Beacon(i, target);
            if (sensors.stream().anyMatch(sensor -> sensor.canSeePoint(point))) {
                count++;
            }
        }
        return count;
    }

    long findMissingBeacon() {
        for (int a_coefficient : A_COEFFICIENTS) {
            for (int b_coefficient : B_COEFFICIENTS) {
                // (x_o, y_o) = -1*(b - a)/-2, -1*(a + b)/2 => (b-a)/2, (a+b)/2
                // from above
                Beacon intersection = new Beacon((b_coefficient - a_coefficient) / 2,
                        (a_coefficient + b_coefficient) / 2);
                if (intersection.getX() < MIN_X || intersection.getX() > MAX_X || intersection.getY() < MIN_Y
                        || intersection.getY() > MAX_Y)
                    // can't be outside bounnds
                    continue;
                if (sensors.stream().anyMatch(sensor -> sensor.canSeePoint(intersection)))
                    // inside sensor region
                    continue;
                //no sensor can see this intersection point; its the answer
                return Long.valueOf(intersection.getX()) * 4000000 + Long.valueOf(intersection.getY());
            }
        }
        throw new Error("No beacon found");

    }

}

public class Day15 extends Day {

    public Day15() {
        super();
    }

    public Day15(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    int target = 2000000;
    private int MINIMUM = 0;
    private int MAXIMUM = 4000000;

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String runPart1() {
        CaveMap grid = CaveMap.build(MATCHES);
        return "" + grid.findInTargetRow(target);
    }

    public void setMinMax(int MINIMUM, int MAXIMUM) {
        this.MINIMUM = MINIMUM;
        this.MAXIMUM = MAXIMUM;
    }

    @Override
    public String runPart2() {
        CaveMap grid = CaveMap.build(MATCHES, true);
        grid.setMinMax(MINIMUM, MAXIMUM);
        return "" + grid.findMissingBeacon();
    }

}
