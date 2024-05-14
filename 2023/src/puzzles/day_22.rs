use std::{collections::BinaryHeap, str::FromStr};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(22);
    let mut stack = Stack::from(input);

    stack.cascade();

    println!("** Part 1 Final: {:?}", stack.count_removable());
    println!("** Part 2 Final: {:?}", stack.blow_it_up());
}

#[derive(Debug, Clone, Copy)]
struct Point {
    x: usize,
    y: usize,
    z: usize,
}
impl FromStr for Point {
    type Err = std::fmt::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let [x, y, z] = s
            .trim()
            .split(',')
            .map(|str| str.parse::<usize>().unwrap())
            .collect::<Vec<usize>>()[..]
        else {
            panic!("help")
        };

        Ok(Point { x, y, z })
    }
}

enum Direction {
    Up,
    Down,
}

#[derive(Debug, Clone)]
struct Brick {
    start: Point,
    end: Point,
    supports: Vec<usize>,
    supported_by: Vec<usize>,
}
impl Brick {
    fn move_by(&mut self, dir: Direction, by: usize) {
        match dir {
            Direction::Up => {
                self.start.z += by;
                self.end.z += by;
            }
            Direction::Down => {
                if self.start.z - by > 0 {
                    self.start.z -= by;
                    self.end.z -= by;
                }
            }
        }
    }
    fn move_to(&mut self, z: usize) {
        let delta = self.end.z - self.start.z;
        self.start.z = z;
        self.end.z = z + delta;
    }
    fn collides_with(&self, other: &Brick) -> bool {
        ((self.start.x >= other.start.x && self.start.x <= other.end.x)
            || (self.end.x >= other.start.x && self.end.x <= other.end.x)
            || (other.start.x >= self.start.x && other.start.x <= self.end.x)
            || (other.end.x >= self.start.x && other.end.x <= self.end.x))
            && ((self.start.y >= other.start.y && self.start.y <= other.end.y)
                || (self.end.y >= other.start.y && self.end.y <= other.end.y)
                || (other.start.y >= self.start.y && other.start.y <= self.end.y)
                || (other.end.y >= self.start.y && other.end.y <= self.end.y))
            && ((self.start.z >= other.start.z && self.start.z <= other.end.z)
                || (self.end.z >= other.start.z && self.end.z <= other.end.z)
                || (other.start.z >= self.start.z && other.start.z <= self.end.z)
                || (other.end.z >= self.start.z && other.end.z <= self.end.z))
    }
}
impl FromStr for Brick {
    type Err = std::fmt::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let [start, end] = s
            .trim()
            .split('~')
            .map(|str| str.parse::<Point>().unwrap())
            .collect::<Vec<Point>>()[..]
        else {
            panic!("help");
        };
        Ok(Brick {
            start: if start.z <= end.z { start } else { end },
            end: if start.z <= end.z { end } else { start },
            supports: Vec::new(),
            supported_by: Vec::new(),
        })
    }
}

#[derive(Debug)]
struct Stack {
    bricks: Vec<Brick>,
}
impl Stack {
    fn from(input: Vec<String>) -> Self {
        let mut bricks = input
            .iter()
            .map(|line| line.parse::<Brick>().unwrap())
            .collect::<Vec<Brick>>();
        bricks.sort_by_key(|brick| brick.start.z);
        Stack { bricks }
    }
    fn cascade(&mut self) -> &mut Self {
        let mut bottom = 1;
        let mut moved: Vec<Brick> = Vec::new();
        for brick in self.bricks.iter_mut() {
            brick.move_to(bottom + 1);

            loop {
                let mut keep_trying = true;
                brick.move_by(Direction::Down, 1);
                let len = moved.len();
                for (index, other) in moved.iter_mut().enumerate().rev() {
                    if other.end.z < brick.start.z {
                        continue;
                    }

                    if brick.collides_with(other) {
                        keep_trying = false;
                        other.supports.push(len);
                        brick.supported_by.push(index);
                    }
                }
                if !keep_trying {
                    brick.move_by(Direction::Up, 1);
                    break;
                }
                if brick.start.z == 1 {
                    break;
                }
            }

            bottom = std::cmp::max(brick.end.z, bottom);

            moved.push(brick.clone());
        }
        self.bricks = moved;
        self
    }
    fn count_removable(&self) -> usize {

        let mut sum = 0;

        for brick in self.bricks.iter() {
            if brick.supports.iter().all(|&index| self.bricks[index].supported_by.len() > 1) {
                sum += 1;
            }
        }

        sum
    }
    fn blow_it_up(&self) -> usize {
        let mut sum = 0;

        for i in (0..self.bricks.len()).rev() {
            if self.bricks[i].supports.is_empty() {
                continue;
            }

            let mut fallen_indexes: Vec<usize> = Vec::new();
            let mut queue = BinaryHeap::<(usize, usize)>::new();

            queue.push((i, self.bricks[i].end.z));

            while !queue.is_empty() {
                let (index, _) = queue.pop().unwrap();

                fallen_indexes.push(index);

                let brick = &self.bricks[index];

                for brick_index in &brick.supports {
                    let supported_brick = &self.bricks[*brick_index];

                    if supported_brick.supported_by.iter().all(|idx| {
                        fallen_indexes
                            .iter()
                            .rev()
                            .collect::<Vec<&usize>>()
                            .contains(&idx)
                    }) {
                        queue.push((*brick_index, supported_brick.end.z));
                    }
                }
            }

            sum += fallen_indexes.len() - 1;
        }

        sum
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9",
        );

        assert_eq!(5, Stack::from(input).cascade().count_removable());
    }
}
