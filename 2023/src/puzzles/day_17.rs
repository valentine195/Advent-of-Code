use std::{
    cmp::Reverse,
    collections::{BinaryHeap, HashMap},
};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(17);

    println!("** Part 1 Final: {:?}", City::parse(&input).traverse(1, 3));
    println!("** Part 2 Final: {:?}", City::parse(&input).traverse(4, 10));
}

type Point = (usize, usize);

#[derive(Debug, PartialEq, Eq, PartialOrd, Ord, Hash, Clone, Copy)]
enum Direction {
    Up,
    Down,
    Left,
    Right,
    None,
}
impl Direction {
    fn directions() -> Vec<Self> {
        vec![
            Direction::Up,
            Direction::Down,
            Direction::Left,
            Direction::Right,
        ]
    }
    fn coords(&self) -> Option<(isize, isize)> {
        match self {
            Direction::Up => Some((0, -1)),
            Direction::Down => Some((0, 1)),
            Direction::Left => Some((-1, 0)),
            Direction::Right => Some((1, 0)),
            Direction::None => None,
        }
    }
    fn turns(&self) -> Vec<Self> {
        match self {
            Direction::Up | Direction::Down => vec![Direction::Left, Direction::Right],
            Direction::Left | Direction::Right => vec![Direction::Up, Direction::Down],
            Direction::None => Direction::directions(),
        }
    }
    fn offset(&self, offset: usize) -> Option<(isize, isize)> {
        let Some(coords) = self.coords() else {
            return None;
        };

        Some((coords.0 * offset as isize, coords.1 * offset as isize))
    }
}

struct City {
    grid: HashMap<Point, i64>,
    bounds: (usize, usize),
    height: usize,
    width: usize,
}
impl City {
    fn parse(input: &Vec<String>) -> Self {
        let mut grid = HashMap::new();

        let height = input.len();
        let width = input[0].len();
        let mut bounds = (0, 0);

        for (row, line) in input.iter().enumerate() {
            for (col, char) in line.bytes().enumerate() {
                grid.insert((col, row), (char - b'0') as i64);
                bounds = (row, col);
            }
        }
        City {
            grid,
            width,
            height,
            bounds,
        }
    }
    fn next_point(&self, loc: Point, offset: Option<(isize, isize)>) -> Option<Point> {
        let Some(offset) = offset else {
            return None;
        };
        Some((
            match loc.0.checked_add_signed(offset.0) {
                Some(p) => {
                    if p >= self.width {
                        return None;
                    } else {
                        p
                    }
                }
                None => return None,
            },
            match loc.1.checked_add_signed(offset.1) {
                Some(p) => {
                    if p >= self.height {
                        return None;
                    } else {
                        p
                    }
                }
                None => return None,
            },
        ))
    }
    fn traverse(&self, min: usize, max: usize) -> i64 {
        let mut dists: HashMap<(Point, Direction), i64> = HashMap::new();
        let mut queue: BinaryHeap<Reverse<(i64, Point, Direction)>> = BinaryHeap::new();
        queue.push(Reverse((0, (0, 0), Direction::None)));
        while let Some(Reverse((heat_loss, loc, dir))) = queue.pop() {
            if loc == self.bounds {
                return heat_loss;
            }
            if dists.get(&(loc, dir)).is_some_and(|&other| heat_loss > other) {
                continue;
            }
            //carry each turn as far as it can go and add each point to queue
            for turn in dir.turns() {
                let mut next_loss = heat_loss;
                for dist in 1..=max {
                    let Some(point) = self.next_point(loc, turn.offset(dist)) else {
                        continue;
                    };
                    next_loss += self.grid.get(&point).unwrap();
                    if dist < min {
                        continue;
                    }
                    if next_loss < *dists.get(&(point, turn)).unwrap_or(&i64::MAX) {
                        dists.insert((point, turn), next_loss);
                        queue.push(Reverse((next_loss, point, turn)))
                    }
                }
            }
        }
        return 0;
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533",
        );

        assert_eq!(102, City::parse(&input).traverse(1, 3));
    }
}
