use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(18);

    println!("** Part 1 Final: {:?}", shoelace(&input, false));
    println!("** Part 2 Final: {:?}", shoelace(&input, true));
}

#[derive(Debug, PartialEq, Eq)]
enum Direction {
    Right,
    Left,
    Down,
    Up,
}
impl Direction {
    fn from(s: &str) -> Self {
        match s {
            "R" => Direction::Right,
            "L" => Direction::Left,
            "U" => Direction::Up,
            "D" => Direction::Down,
            _ => unreachable!(),
        }
    }
    fn from_char(s: &char) -> Self {
        match s {
            '0' => Direction::Right,
            '1' => Direction::Down,
            '2' => Direction::Left,
            '3' => Direction::Up,
            _ => unreachable!(),
        }
    }
}

#[derive(Debug, PartialEq, Eq)]
struct Step {
    dir: Direction,
    steps: isize,
}
impl Step {
    fn offset(&self, point: Point) -> Point {
        match self.dir {
            Direction::Right => (point.0 + self.steps, point.1),
            Direction::Left => (point.0 - self.steps, point.1),
            Direction::Down => (point.0, point.1 + self.steps),
            Direction::Up => (point.0, point.1 - self.steps),
        }
    }
    fn parse(s: &str, part_2: bool) -> Result<Self, std::fmt::Error> {
        let [dir, steps, color] = s.split_whitespace().collect::<Vec<&str>>()[..] else {
            return Err(std::fmt::Error);
        };
        let color: Vec<char> = color.chars().collect();
        let mut color = color[2..color.len() - 1].to_vec();

        if part_2 {
            Ok(Step {
                dir: Direction::from_char(&color.pop().unwrap()),
                steps: isize::from_str_radix(&color.into_iter().collect::<String>()[..], 16)
                    .unwrap(),
            })
        } else {
            Ok(Step {
                dir: Direction::from(dir),
                steps: steps.parse::<isize>().unwrap(),
            })
        }
    }
}

type Point = (isize, isize);

fn shoelace(input: &Vec<String>, part_2: bool) -> i64 {
    let mut area = 0;

    let mut corner: Point = (0, 0);
    let mut boundary_points: i64 = 0;

    for line in input {
        let Ok(step) = Step::parse(line, part_2) else {
            panic!("whaaaa x2")
        };
        let offset = step.offset(corner);
        boundary_points += step.steps as i64;
        area += (corner.0 * offset.1) as i64 - (corner.1 * offset.0) as i64;
        corner = offset;
    }

    (area + boundary_points) / 2 + 1
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)",
        );

        assert_eq!(62, shoelace(&input, false));
        assert_eq!(952408144115, shoelace(&input, true));
    }
}
