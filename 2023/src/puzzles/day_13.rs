use std::collections::HashMap;

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(13);
    let valley = Valley::parse(&input);
    println!("** Part 1 Final: {:?}", valley.reflect(0));
    println!("** Part 2 Final: {:?}", valley.reflect(1));
}

#[derive(Debug)]
struct Valley {
    grids: Vec<Grid>,
}
impl Valley {
    fn reflect(&self, allowed_smudges: u8) -> u32 {
        self.grids
            .iter()
            .map(|g| g.try_reflect(allowed_smudges))
            .sum()
    }
    fn parse(input: &Vec<String>) -> Self {
        let mut grids = Vec::new();
        let mut grid = Vec::new();
        let mut iter = input.iter();
        loop {
            let line = match iter.next() {
                Some(l) => l,
                None => {
                    if !grid.is_empty() {
                        grids.push(Grid::parse(&grid));
                    }
                    break;
                }
            };
            if line.trim().is_empty() {
                grids.push(Grid::parse(&grid));
                grid = Vec::new();
            } else {
                grid.push(line.clone());
            }
        }

        Valley { grids }
    }
}

#[derive(Debug)]
struct Grid {
    width: usize,
    height: usize,
    cells: HashMap<(usize, usize), char>,
}
impl Grid {
    fn parse(input: &Vec<String>) -> Self {
        let width = input[0].len();
        let height = input.len();
        let mut cells = HashMap::new();

        for row in 0..height {
            let mut chars = input[row].chars();
            for col in 0..width {
                cells.insert((col, row), chars.next().unwrap());
            }
        }

        Grid {
            width,
            height,
            cells,
        }
    }
    fn try_reflect(&self, allowed_smudges: u8) -> u32 {
        //cols first...
        'col: for col in 1..self.width {
            let mut left = (col - 1) as isize;
            let mut right = col;
            let mut smudges = 0;
            while left >= 0 && right < self.width {
                for row in 0..self.height {
                    let left_val = self.cells.get(&(left as usize, row)).unwrap();
                    let right_val = self.cells.get(&(right, row)).unwrap();

                    if left_val != right_val {
                        smudges += 1;
                        if smudges > allowed_smudges {
                            continue 'col;
                        }
                    }
                }
                left -= 1;
                right += 1;
            }
            if smudges == allowed_smudges {
                return col as u32;
            }
        }

        'row: for row in 1..self.height {
            let mut top = (row - 1) as isize;
            let mut bottom = row;
            let mut smudges = 0;
            while top >= 0 && bottom < self.height {
                for col in 0..self.width {
                    let top_val = self.cells.get(&(col, top as usize)).unwrap();
                    let bottom_val = self.cells.get(&(col, bottom)).unwrap();
                    if top_val != bottom_val {
                        smudges += 1;
                        if smudges > allowed_smudges {
                            continue 'row;
                        }
                    }
                }
                top -= 1;
                bottom += 1;
            }
            if smudges == allowed_smudges {
                return (row as u32) * 100;
            }
        }

        0
    }
}

impl std::fmt::Display for Grid {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let mut str = String::from("");
        for row in 0..self.height {
            for col in 0..self.width {
                str.push(*self.cells.get(&(col, row)).unwrap());
            }
            str.push_str("\n");
        }

        write!(f, "{}", str)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "#.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#",
        );
        let valley = Valley::parse(&input);
        assert_eq!(405, valley.reflect(0));
        assert_eq!(400, valley.reflect(1));
    }
}
