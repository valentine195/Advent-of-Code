use std::collections::HashMap;

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(10);
    let map = Map::build_map(&input);
    let (steps, area) = map.traverse();
    println!("** Part 1 Final: {:?}", steps);
    println!("** Part 2 Final: {:?}", area);
}

#[derive(Debug, PartialEq)]
enum Cardinal {
    North,
    South,
    East,
    West,
}
static CARDINAL_DIRECTIONS: [Cardinal; 4] = [
    Cardinal::North,
    Cardinal::South,
    Cardinal::West,
    Cardinal::East,
];
impl Cardinal {
    fn get_offset(&self) -> (i32, i32) {
        match self {
            Cardinal::North => (0, -1),
            Cardinal::South => (0, 1),
            Cardinal::East => (1, 0),
            Cardinal::West => (-1, 0),
        }
    }

    fn reverse(&self) -> &Self {
        match self {
            Cardinal::North => &Cardinal::South,
            Cardinal::South => &Cardinal::North,
            Cardinal::East => &Cardinal::West,
            Cardinal::West => &Cardinal::East,
        }
    }
}

#[derive(Debug, PartialEq)]
enum Shape {
    NorthSouth,
    EastWest,
    NorthEast,
    NorthWest,
    SouthWest,
    SouthEast,
    Start,
}
impl Shape {
    fn is_corner(&self) -> bool {
        self != &Shape::NorthSouth && self != &Shape::EastWest
    }
    fn from_char(char: char) -> Option<Self> {
        match char {
            'S' => Some(Shape::Start),
            '|' => Some(Shape::NorthSouth),
            '-' => Some(Shape::EastWest),
            'L' => Some(Shape::NorthEast),
            'J' => Some(Shape::NorthWest),
            'F' => Some(Shape::SouthEast),
            '7' => Some(Shape::SouthWest),
            _ => None,
        }
    }
    fn is_valid(&self, from: &Cardinal) -> bool {
        if self == &Shape::Start {
            return true;
        }
        match from {
            Cardinal::South => {
                return self == &Shape::NorthEast
                    || self == &Shape::NorthWest
                    || self == &Shape::NorthSouth
            }
            Cardinal::North => {
                return self == &Shape::NorthSouth
                    || self == &Shape::SouthEast
                    || self == &Shape::SouthWest
            }
            Cardinal::West => {
                return self == &Shape::EastWest
                    || self == &Shape::SouthEast
                    || self == &Shape::NorthEast
            }
            Cardinal::East => {
                return self == &Shape::EastWest
                    || self == &Shape::NorthWest
                    || self == &Shape::SouthWest
            }
        }
    }
}

#[derive(Debug, PartialEq)]
struct Pipe(u32, u32, Shape);
impl Pipe {
    fn is_corner(&self) -> bool {
        self.2.is_corner()
    }
    fn offset(&self, offset: (i32, i32), max: (u32, u32)) -> Option<(u32, u32)> {
        let next: (i32, i32) = (self.0 as i32 + offset.0, self.1 as i32 + offset.1);

        if next.0 < 0 || next.0 >= max.0 as i32 {
            return None;
        }
        if next.1 < 0 || next.1 >= max.1 as i32 {
            return None;
        }

        Some((next.0 as u32, next.1 as u32))
    }
    fn get_directions(&self) -> Vec<&Cardinal> {
        match self.2 {
            Shape::NorthSouth => vec![&Cardinal::North, &Cardinal::South],
            Shape::EastWest => vec![&Cardinal::East, &Cardinal::West],
            Shape::NorthEast => vec![&Cardinal::North, &Cardinal::East],
            Shape::NorthWest => vec![&Cardinal::North, &Cardinal::West],
            Shape::SouthWest => vec![&Cardinal::South, &Cardinal::West],
            Shape::SouthEast => vec![&Cardinal::South, &Cardinal::East],
            Shape::Start => CARDINAL_DIRECTIONS.iter().collect(),
        }
    }
}

#[derive(Debug)]
struct Map {
    pipes: HashMap<(u32, u32), Pipe>,
    start: (u32, u32),
    maxes: (u32, u32),
}

impl Map {
    fn build_map(input: &Vec<String>) -> Self {
        //find start..
        let mut start: Option<(u32, u32)> = None;
        let mut pipes: HashMap<(u32, u32), Pipe> = HashMap::new();

        for row in 0..input.len() {
            let line = &input[row];
            let line_chars: Vec<char> = line.chars().collect();
            for col in 0..line_chars.len() {
                let char = line_chars[col];
                match Shape::from_char(char) {
                    Some(Shape::Start) => {
                        pipes.insert(
                            (col as u32, row as u32),
                            Pipe(col as u32, row as u32, Shape::Start),
                        );
                        start = Some((col as u32, row as u32))
                    }
                    Some(dir) => {
                        pipes.insert((col as u32, row as u32), Pipe(col as u32, row as u32, dir));
                    }
                    None => continue,
                }
            }
        }
        if start.is_none() {
            panic!("uh oh, didn't find start")
        }
        Map {
            pipes,
            start: start.unwrap(),
            maxes: (input[0].len() as u32, input.len() as u32),
        }
    }

    fn traverse(&self) -> (i32, i32) {
        let mut steps = 1;
        let start = self.pipes.get(&self.start).unwrap();
        //find a valid point from the start
        let (mut next, mut moved) = match self.get_next(start, None) {
            Some(p) => p,
            None => panic!("uh oh"),
        };

        //shoelace theorem...
        let mut area: i32 = 0;
        let mut corner_pipe = start;

        while next != start {
            (next, moved) = match self.get_next(next, Some(moved)) {
                Some((pipe, moved)) => {
                    if pipe.is_corner() {
                        area += ((corner_pipe.0 * pipe.1) as i32)
                            - ((corner_pipe.1 * pipe.0) as i32) as i32;
                        corner_pipe = pipe;
                    }
                    (pipe, moved)
                }
                None => break,
            };
            steps += 1;
        }

        (steps / 2, area.abs() / 2 - steps / 2 + 1)
    }

    fn get_next<'a>(
        &'a self,
        from: &'a Pipe,
        entered: Option<&Cardinal>,
    ) -> Option<(&Pipe, &Cardinal)> {
        for dir in from.get_directions() {
            if entered.is_some_and(|e| e.reverse() == dir) {
                continue;
            }
            match from.offset(dir.get_offset(), self.maxes) {
                Some((x, y)) => {
                    match self.pipes.get(&(x, y)) {
                        Some(p) => {
                            if p.2.is_valid(dir) {
                                return Some((p, dir));
                            }
                        }
                        None => continue,
                    };
                }
                None => continue,
            }
        }
        None
    }

    /*     fn _go_into(&self, from: Pipe, steps: u32) -> Option<u32> {
        if from.0 == self.start.0 && from.1 == self.start.1 {
            return Some(steps);
        }



        Some(steps)
    } */
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "-L|F7
7S-7|
L|7||
-L-J|
L|-JF",
        );

        let mut map = Map::build_map(&input);
        let (mut steps, _) = map.traverse();
        assert_eq!(4, steps);

        map = Map::build_map(&input::split_string(
            "7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ",
        ));
        (steps, _) = map.traverse();

        assert_eq!(8, steps);
    }
    #[test]
    fn test_2() {
        let input: Vec<String> = input::split_string(
            "...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........",
        );

        let map = Map::build_map(&input);
        let (_, area) = map.traverse();
        assert_eq!(4, area);
    }
}
