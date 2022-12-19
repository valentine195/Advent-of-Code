package com.jeremy.aoc2022.Days;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.jeremy.aoc2022.Day;
import com.jeremy.aoc2022.utils.Coord;

enum Tile {
    LAVA,
    AIR,
    AIR2
}

class ThreeDCoord extends Coord {
    int z;

    ThreeDCoord(int[] coords) {
        super(coords[0], coords[1]);
        this.z = coords[2];
    }

    ThreeDCoord(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public Set<ThreeDCoord> neighbors() {
        Set<ThreeDCoord> list = new HashSet<>();

        list.add(new ThreeDCoord((int) x + 1, (int) y, (int) z));
        list.add(new ThreeDCoord((int) x - 1, (int) y, (int) z));
        list.add(new ThreeDCoord((int) x, (int) y + 1, (int) z));
        list.add(new ThreeDCoord((int) x, (int) y - 1, (int) z));
        list.add(new ThreeDCoord((int) x, (int) y, (int) z + 1));
        list.add(new ThreeDCoord((int) x, (int) y, (int) z - 1));

        return list;
    }

    boolean touches(ThreeDCoord coord) {
        int dx = (int) x - (int) coord.x;
        int dy = (int) y - (int) coord.y;
        int dz = z - coord.z;

        return Math.abs(dx) + Math.abs(dy) + Math.abs(dz) == 1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x, z);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && z == ((ThreeDCoord) obj).z;
    }
}

public class Day18 extends Day {
    public Day18(String input) {
        super(input);
    }

    public Day18() {
        super();
    }

    @Override
    public String runPart1() {

        Set<ThreeDCoord> coords = new HashSet<>();
        int surfaceArea = 0;
        for (String line : MATCHES) {
            int[] coordinates = Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray();
            coords.add(new ThreeDCoord(coordinates));
        }

        for (ThreeDCoord coord : coords) {
            for (ThreeDCoord neighbor : coord.neighbors()) {
                if (!coords.contains(neighbor))
                    surfaceArea++;
            }
        }
        return "" + surfaceArea;
    }

    @Override
    public String runPart2() {

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        Set<ThreeDCoord> coords = new HashSet<>();
        int surfaceArea = 0;
        for (String line : MATCHES) {
            int[] coordinates = Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray();
            coords.add(new ThreeDCoord(coordinates));
            minX = Math.min(minX, coordinates[0]);
            maxX = Math.max(maxX, coordinates[0]);
            minY = Math.min(minY, coordinates[1]);
            maxY = Math.max(maxY, coordinates[1]);
            minZ = Math.min(minZ, coordinates[2]);
            maxZ = Math.max(maxZ, coordinates[2]);
        }

        ArrayDeque<ThreeDCoord> queue = new ArrayDeque<>();
        queue.add(new ThreeDCoord(minX - 1, minY - 1, minZ - 1));

        HashSet<ThreeDCoord> exterior = new HashSet<>();
        while (queue.size() > 0) {
            ThreeDCoord coord = queue.poll();
            for (ThreeDCoord neighbor : coord.neighbors()) {
                if (neighbor.x >= minX - 1 && neighbor.x <= maxX + 1 && neighbor.y >= minY - 1 && neighbor.y <= maxY + 1
                        && neighbor.z >= minZ - 1 && neighbor.z <= maxZ + 1 && !coords.contains(neighbor)
                        && !exterior.contains(neighbor)) {
                    exterior.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        for (ThreeDCoord coord : coords) {
            for (ThreeDCoord neighbor : coord.neighbors()) {
                if (exterior.contains(neighbor))
                    surfaceArea++;
            }
        }
        return "" + surfaceArea;
    }
}
