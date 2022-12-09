package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.List;

import com.jeremy.aoc2022.Day;

enum Direction {
    Up, Down, Left, Right
}

class TreeGrid {

    TreeGrid(List<String> MATCHES) {
        for (String row : MATCHES) {
            grid.add(row.chars().boxed().toList());
        }
    }

    public List<List<Integer>> grid = new ArrayList<>();

    public boolean isVisible(int row, int column) {
        Integer height = grid.get(row).get(column);
        return isVisibleInDirection(height, row, column, Direction.Up) ||
                isVisibleInDirection(height, row, column, Direction.Down) ||
                isVisibleInDirection(height, row, column, Direction.Left) ||
                isVisibleInDirection(height, row, column, Direction.Right);
    }

    public boolean isVisibleInDirection(int height, int row, int column, Direction direction) {
        if (row == 0 || row == rows() - 1 || column == 0 || column == columns() - 1) {
            return true;
        }
        Integer newRow = row, newColumn = column;
        switch (direction) {
            case Up: {
                newRow = row - 1;
                break;
            }
            case Down: {
                newRow = row + 1;
                break;
            }
            case Left: {
                newColumn = column - 1;
                break;
            }
            case Right: {
                newColumn = column + 1;
                break;
            }
        }

        Integer neighbor = grid.get(newRow).get(newColumn);
        if (neighbor < height) {
            return isVisibleInDirection(height, newRow, newColumn, direction);
        }
        return false;

    }

    public Integer score(int row, int column) {
        if (row == 0 || row == rows() - 1 || column == 0 || column == columns() - 1)
            return 0;
        Integer height = grid.get(row).get(column);
        Integer up = scoreInDirection(height, row, column, Direction.Up, 1);
        Integer down = scoreInDirection(height, row, column, Direction.Down, 1);
        Integer left = scoreInDirection(height, row, column, Direction.Left, 1);
        Integer right = scoreInDirection(height, row, column, Direction.Right, 1);

        return up * down * left * right;
    }

    public Integer scoreInDirection(int height, int row, int column, Direction direction, Integer score) {

        Integer newRow = row, newColumn = column;
        switch (direction) {
            case Up: {
                newRow = row - 1;
                break;
            }
            case Down: {
                newRow = row + 1;
                break;
            }
            case Left: {
                newColumn = column - 1;
                break;
            }
            case Right: {
                newColumn = column + 1;
                break;
            }
        }
        if (newRow < 0 || newRow > rows() - 1 || newColumn < 0 || newColumn > columns() - 1) {
            return 0;
        }
        Integer neighbor = grid.get(newRow).get(newColumn);
        if (neighbor < height) {
            score += scoreInDirection(height, newRow, newColumn, direction, score);
        }
        return score;

    }

    public Integer rows() {
        return grid.size();
    }

    public Integer columns() {
        return grid.get(0).size();
    }
}

public class Day8 extends Day {
    public Day8() {
        super();
    }

    public Day8(String input) {
        super(input);
    }

    TreeGrid grid;

    public void run() {
        grid = new TreeGrid(MATCHES);
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {
        if (grid == null) {
            grid = new TreeGrid(MATCHES);
        }
        int visible = 0;

        for (int row = 0; row < grid.rows(); row++) {
            for (int column = 0; column < grid.columns(); column++) {
                if (grid.isVisible(row, column)) {
                    visible++;
                }
            }
        }
        return "" + visible;

    }

    public String runPart2() {
        if (grid == null) {
            grid = new TreeGrid(MATCHES);
        }

        int maxScore = 0;
        for (int row = 0; row < grid.rows(); row++) {
            for (int column = 0; column < grid.columns(); column++) {
                maxScore = Integer.max(maxScore, grid.score(row, column));
            }
        }

        return "" + maxScore;

    }
}
