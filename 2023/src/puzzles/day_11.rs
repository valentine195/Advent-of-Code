use crate::input;
use std::fmt::Display;

pub fn run() {
    let input: Vec<String> = input::read_day_input(11);

    println!(
        "** Part 1 Final: {:?}",
        Universe::parse(&input).expand(2).find_dist()
    );
    println!(
        "** Part 2 Final: {:?}",
        Universe::parse(&input).expand(1000000).find_dist()
    );
}

#[derive(Debug)]
struct Universe {
    width: usize,
    height: usize,
    galaxies: Vec<Galaxy>,
}
impl Display for Universe {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let mut chars: Vec<Vec<char>> = vec![vec!['.'; self.width]; self.height];
        for y in 0..=self.width {
            for x in 0..=self.height {
                if self.galaxies.iter().any(|p| p.x == x && p.y == y) {
                    chars[y][x] = '#'
                }
            }
        }
        let mut str = String::from("");
        for line in chars {
            for char in line {
                str.push(char);
            }
            str.push_str("\n");
        }
        write!(f, "{}", str)
    }
}
impl Universe {
    fn expand(&mut self, factor: u64) -> &mut Universe {
        let mut rows: Vec<usize> = Vec::new();
        for y in 0..self.height {
            if self.galaxies.iter().any(|g| g.y == y) {
                continue;
            }
            rows.push(y);
        }
        let mut cols: Vec<usize> = Vec::new();
        for x in 0..self.width {
            if self.galaxies.iter().any(|g| g.x == x) {
                continue;
            }
            cols.push(x);
        }
        for galaxy in self.galaxies.iter_mut() {
            for (idx, row) in rows.iter().enumerate() {
                if galaxy.y > ((idx as u64 * (factor - 1)) as usize + row) {
                    *galaxy = Galaxy {
                        x: galaxy.x,
                        y: galaxy.y + (factor - 1) as usize,
                    }
                }
            }
            for (idx, col) in cols.iter().enumerate() {
                if galaxy.x > ((idx as u64 * (factor - 1)) as usize + col) {
                    *galaxy = Galaxy {
                        y: galaxy.y,
                        x: galaxy.x + (factor - 1) as usize,
                    }
                }
            }
        }
        self
    }
    fn find_dist(&self) -> i64 {
        let mut sum: i64 = 0;

        for (i, point_1) in self.galaxies.iter().enumerate() {
            for point_2 in self.galaxies[i..].iter() {
                sum += (point_1.x as i64 - point_2.x as i64).abs()
                    + (point_1.y as i64 - point_2.y as i64).abs();
            }
        }
        sum
    }
    fn parse(input: &Vec<String>) -> Self {
        let width = input[0].len();
        let height = input.len();
        let mut galaxies = Vec::new();
        for y in 0..height {
            let chars: Vec<char> = input[y].chars().into_iter().collect();
            for x in 0..width {
                if chars[x] == '.' {
                    continue;
                }
                galaxies.push(Galaxy { x, y });
            }
        }

        Universe {
            width,
            height,
            galaxies,
        }
    }
}

#[derive(Copy, Clone, Debug, Eq, PartialEq)]
struct Galaxy {
    x: usize,
    y: usize,
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....",
        );
        assert_eq!(374, Universe::parse(&input).expand(2).find_dist());
        assert_eq!(1030, Universe::parse(&input).expand(10).find_dist());
        assert_eq!(8410, Universe::parse(&input).expand(100).find_dist());
    }
}
