package com.jeremy.aoc2022.utils;

import java.util.ArrayList;
import java.util.Objects;

public class Coord implements Comparable<Coord> {
    public long x = 0;
    public long y = 0;

    public static final Coord UP = new Coord(0, -1);
    public static final Coord DOWN = new Coord(0, 1);
    public static final Coord LEFT = new Coord(-1, 0);
    public static final Coord RIGHT = new Coord(1, 0);

    public Coord(long r, long c) {
        x = r;
        y = c;
    }

    public Coord(int r, int c) {
        x = r;
        y = c;
    }

    public Coord() {
        x = 0;
        y = 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coord other = (Coord) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    // adds values of o to self
    public void sumSelf(Coord o) {
        x += o.x;
        y += o.y;
    }

    public void sumSelf(int x, int y) {
        this.x += x;
        this.y += y;
    }

    // returns a new coordinate that is the result of this + o
    public Coord sum(Coord... o) {
        Coord coord = new Coord(x, y);
        for (Coord c : o) {
            coord.sumSelf(c);
        }
        ;
        return coord;
    }

    public Coord sum(int x, int y) {
        return new Coord(this.x + x, this.y + y);
    }

    public Coord set(Coord coord) {
        this.x = (long) coord.x;
        this.y = (long) coord.y;
        return this;
    }

    public Coord set(int x, int y) {
        this.x = (long) x;
        this.y = (long) y;
        return this;
    }

    // returns Manhattan distance to coord o
    public int dist(Coord o) {
        return Math.abs((int) x - (int) o.x) + Math.abs((int) y - (int) o.y);
    }

    // returns copy of self
    public Coord copy() {
        return new Coord(x, y);
    }

    // returns version with x and y swapped
    public Coord invert() {
        return new Coord(y, x);
    }

    public ArrayList<Coord> directNeighbors() {
        ArrayList<Coord> list = new ArrayList<Coord>();
        for (int yOff = -1; yOff < 2; yOff++) {
            for (int xOff = -1; xOff < 2; xOff++) {
                // if not diagonal or self
                if (xOff == 0 ^ yOff == 0) {
                    list.add(new Coord(x + xOff, y + yOff));
                }
            }
        }
        return list;
    }

    public ArrayList<Coord> allNeighbors() {
        ArrayList<Coord> list = new ArrayList<Coord>();
        for (int yOff = -1; yOff < 2; yOff++) {
            for (int xOff = -1; xOff < 2; xOff++) {
                // if not self
                if (!(xOff == 0 && yOff == 0)) {
                    list.add(new Coord(x + xOff, y + yOff));
                }
            }
        }
        return list;
    }

    // orders coordinates in left-to-right, top-to-bottom order
    @Override
    public int compareTo(Coord o) {
        if (this.equals(o))
            return 0;
        else if (o.y > this.y)
            return -1;
        else if (o.y < this.y)
            return 1;
        else
            return (o.x > this.x ? -1 : 1);
    }

    public Coord left() {
        return new Coord(y, -x);
    }

    public Coord right() {
        return new Coord(-y, x);
    }
}