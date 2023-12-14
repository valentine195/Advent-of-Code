use std::{collections::HashMap, fmt::Display};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(14);

    println!(
        "** Part 1 Final: {:?}",
        Platform::parse(&input).roll().load_at(0)
    );
    println!(
        "** Part 2 Final: {:?}",
        Platform::parse(&input).load_at(1000000000)
    );
}

enum Direction {
    North,
    South,
    West,
    East,
}

struct Platform {
    grid: Vec<Vec<char>>,
}
impl Display for Platform {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let mut str = String::from("");
        for line in self.grid.iter() {
            for char in line {
                str.push(*char);
            }
            str.push_str("\n");
        }
        write!(f, "{}", str)
    }
}
impl Platform {
    #[deprecated]
    fn part_one(&self) -> u32 {
        //store the latest height
        //row, count
        let mut stones: Vec<usize> = vec![0; self.grid[0].len()];
        //col, **next** height
        //this is the height a stone will roll to (aka the ROW)
        let mut column_heights: Vec<usize> = self.grid[0]
            .iter()
            .map(|c| match c {
                '.' => 0,
                'O' => {
                    stones[0] += 1;
                    1
                }
                '#' => 1,
                _ => panic!("bad char"),
            })
            .collect();

        for idx in 1..self.grid.len() {
            for (col, c) in self.grid[idx].iter().enumerate() {
                match c {
                    'O' => {
                        let height = column_heights[col];
                        stones[height] += 1;
                        column_heights[col] += 1;
                    }
                    '#' => {
                        column_heights[col] = idx + 1;
                    }
                    _ => {}
                }
            }
        }
        let mut weight = 0;
        for (row, count) in stones.iter().enumerate() {
            weight += count * (stones.len() - row);
        }

        weight as u32
    }

    fn parse(input: &Vec<String>) -> Self {
        Platform {
            grid: input.iter().map(|v| v.chars().collect()).collect(),
        }
    }
    fn load_at(&mut self, cycles: i32) -> u32 {
        let mut seen = HashMap::new();
        for i in 0..cycles - 1 {
            self.cycle();
            if let Some(prev) = seen.insert(self.grid.clone(), i) {
                if (cycles - i) % (i - prev) == 0 {
                    break;
                }
            }
        }
        self.find_load() as u32
    }
    fn find_load(&self) -> usize {
        let mut sum = 0;

        for (idx, line) in self.grid.iter().enumerate() {
            sum += (self.grid.len() - idx) * line.iter().filter(|&&c| c == 'O').count();
        }

        sum
    }
    fn cycle(&mut self) {
        //north
        self.roll();
        self.rotate();
        //west
        self.roll();
        self.rotate();
        //south
        self.roll();
        self.rotate();
        //east
        self.roll();
        self.rotate();
    }

    fn roll(&mut self) -> &mut Platform {
        let mut column_heights: Vec<usize> = self.grid[0]
            .iter()
            .map(|c| match c {
                '.' => 0,
                'O' => 1,
                '#' => 1,
                _ => panic!("bad char"),
            })
            .collect();
        for idx in 1..self.grid.len() {
            for (col, c) in self.grid[idx].clone().iter().enumerate() {
                match c {
                    'O' => {
                        self.grid[idx][col] = '.';
                        self.grid[column_heights[col]][col] = 'O';
                        column_heights[col] += 1;
                    }
                    '#' => {
                        column_heights[col] = idx + 1;
                    }
                    _ => {}
                }
            }
        }
        self
    }

    //rotate 2d grid 90 degrees
    fn rotate(&mut self) {
        let mut rotated = vec![vec!['.'; self.grid.len()]; self.grid[0].len()];
        for r in 0..self.grid.len() {
            for c in 0..self.grid[0].len() {
                rotated[c][self.grid.len() - 1 - r] = self.grid[r][c];
            }
        }
        self.grid = rotated;
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....",
        );

        assert_eq!(136, Platform::parse(&input).roll().load_at(0));
        assert_eq!(64, Platform::parse(&input).load_at(1000000000));
    }
}
