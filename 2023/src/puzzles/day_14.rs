use std::collections::HashMap;

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(14);

    println!("** Part 1 Final: {:?}", Platform::parse(&input));
    println!("** Part 2 Final: {:?}", 0);
}

struct Platform {}
impl Platform {
    fn parse(input: &Vec<String>) -> u32 {
        //store the latest height
        //row, count
        let mut stones: Vec<usize> = vec![0; input[0].len()];
        //col, **next** height
        //this is the height a stone will roll to (aka the ROW)
        let mut column_heights: Vec<usize> = input[0]
            .chars()
            .enumerate()
            .map(|(i, c)| match c {
                '.' => 0,
                'O' => {
                    stones[0] += 1;
                    1
                }
                '#' => 1,
                _ => panic!("bad char"),
            })
            .collect();

        for idx in 1..input.len() {
            for (col, c) in input[idx].chars().enumerate() {
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

        assert_eq!(136, Platform::parse(&input));
    }
}
