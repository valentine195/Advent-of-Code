use std::fmt::{Display, Error};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(21);

    let mut garden = Garden::build(input).unwrap();

    println!("** Part 1 Final: {:?}", garden.plot_steps(64));
    println!("** Part 2 Final: {:?}", 0);
}

//x, y
#[derive(Debug, PartialEq, Eq)]
struct Point(i32, i32);
impl Point {
    fn dirs(&self) -> Vec<Point> {
        vec![
            Point(self.0, self.1 - 1),
            Point(self.0, self.1 + 1),
            Point(self.0 - 1, self.1),
            Point(self.0 + 1, self.1),
        ]
    }
}

#[derive(Debug)]
struct Garden {
    walls: Vec<Point>,
    size: Point,
    locations: Vec<Point>,
}

impl Garden {
    fn plot_steps(&mut self, steps: usize) -> usize {
        for _ in 0..steps {
            let mut new_locations = Vec::new();
            for location in self.locations.iter() {
                for dir in location.dirs() {
                    if self.walls.contains(&dir) || new_locations.contains(&dir) {
                        continue;
                    }
                    new_locations.push(dir);
                }
            }
            self.locations = new_locations;
            /* println!("{}", self); */
        }
        self.locations.len()
    }
    fn build(lines: Vec<String>) -> Result<Self, Error> {
        let rows = lines.len() as i32;
        let cols = lines[0].len() as i32;

        let mut walls: Vec<Point> = Vec::new();
        let mut start: Option<Point> = None;
        for col in 0..=cols {
            walls.push(Point(col, -1));
            walls.push(Point(col, rows + 1));
        }
        for (row, line) in lines.iter().enumerate() {
            walls.push(Point(-1, row as i32));
            walls.push(Point(cols + 1, row as i32));

            let chars = line.chars();

            for (col, c) in chars.into_iter().enumerate() {
                if c == '#' {
                    walls.push(Point(col as i32, row as i32));
                }
                if c == 'S' {
                    start = Some(Point(col as i32, row as i32));
                }
            }
        }
        if let Some(point) = start {
            return Ok(Garden {
                walls,
                size: Point(cols, rows),
                locations: vec![point],
            });
        }
        Err(Error::default())
    }
}
impl Display for Garden {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let mut str = String::from("");
        for col in 0..self.size.0 {
            for row in 0..self.size.1 {
                if self.walls.contains(&Point(row as i32, col as i32)) {
                    str.push('#');
                } else if self.locations.contains(&Point(row as i32, col as i32)) {
                    str.push('F')
                } else {
                    str.push('.')
                }
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
            "...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........",
        );

        let mut garden = Garden::build(input).unwrap();
        assert_eq!(16, garden.plot_steps(6));
    }
}
