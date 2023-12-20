use std::collections::{HashMap, HashSet};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(16);

    println!(
        "** Part 1 Final: {:?}",
        Map::parse(&input).energize((0, 0, '>'))
    );
    println!("** Part 2 Final: {:?}", Map::parse(&input).max_energy());
}

/**
 * Direction (.2) -> 0 ^, 1 >, 2 v, 3 <
 */
type Beam = (usize, usize, char);
struct Map {
    grid: HashMap<(usize, usize), char>,
    width: usize,
    height: usize,
}
impl Map {
    fn parse(input: &Vec<String>) -> Self {
        let mut grid = HashMap::new();

        let height = input.len();
        let width = input[0].len();

        for (row, line) in input.iter().enumerate() {
            for (col, char) in line.chars().enumerate() {
                grid.insert((col, row), char);
            }
        }
        Map {
            grid,
            width,
            height,
        }
    }
    fn check_next(&self, beam: &Beam) -> Option<Beam> {
        let vector = match beam.2 {
            '^' => (0, -1),
            'v' => (0, 1),
            '<' => (-1, 0),
            '>' => (1, 0),
            _ => panic!("unreachable"),
        };
        let next_x = beam.0.checked_add_signed(vector.0)?;
        let next_y = beam.1.checked_add_signed(vector.1)?;

        if next_x >= self.width || next_y >= self.height {
            None
        } else {
            Some((next_x, next_y, beam.2))
        }
    }
    fn energize(&mut self, start: Beam) -> usize {
        let mut visited: HashSet<(usize, usize)> = HashSet::new();
        let mut to_visit: Vec<Beam> = vec![];
        let mut loop_check: HashSet<Beam> = HashSet::new();
        to_visit.push(start);

        while let Some(beam) = to_visit.pop() {
            if !loop_check.insert(beam) {
                continue;
            }
            visited.insert((beam.0, beam.1));

            let next: Vec<Beam> = match self.grid.get(&(beam.0, beam.1)) {
                Some('.') => vec![(beam.0, beam.1, beam.2)],
                Some('x') => match beam.2 {
                    '^' => vec![(beam.0, beam.1, '<')],
                    'v' => vec![(beam.0, beam.1, '>')],
                    '<' => vec![(beam.0, beam.1, '^')],
                    '>' => vec![(beam.0, beam.1, 'v')],
                    _ => panic!("unreachable"),
                },
                Some('/') => match beam.2 {
                    '^' => vec![(beam.0, beam.1, '>')],
                    'v' => vec![(beam.0, beam.1, '<')],
                    '<' => vec![(beam.0, beam.1, 'v')],
                    '>' => vec![(beam.0, beam.1, '^')],
                    _ => panic!("unreachable"),
                },
                Some('-') => match beam.2 {
                    '^' | 'v' => vec![(beam.0, beam.1, '<'), (beam.0, beam.1, '>')],
                    '<' | '>' => vec![(beam.0, beam.1, beam.2)],
                    _ => panic!("unreachable"),
                },
                Some('|') => match beam.2 {
                    '^' | 'v' => vec![(beam.0, beam.1, beam.2)],
                    '<' | '>' => vec![(beam.0, beam.1, 'v'), (beam.0, beam.1, '^')],
                    _ => panic!("unreachable"),
                },
                None => continue,
                _ => panic!("uhhh"),
            };
            for possible_beam in next {
                if let Some(next_location) = self.check_next(&possible_beam) {
                    to_visit.push(next_location);
                }
            }

            /* self.display(beams.clone()); */
        }
        visited.len()
    }
    fn max_energy(&mut self) -> usize {
        let mut max = 0;

        for y in 0..self.height {
            max = max.max(self.energize((0, y, '>')));
            max = max.max(self.energize((self.width - 1, y, '<')));
        }
        for x in 0..self.width {
            max = max.max(self.energize((x, 0, 'v')));
            max = max.max(self.energize((x, self.height - 1, '^')));
        }

        max
    }
    fn display(&self, beams: Vec<Beam>) {
        for row in 0..self.width {
            let mut str = String::from("");
            for col in 0..self.height {
                let possible_beams = beams
                    .iter()
                    .filter(|b| b.0 == col && b.1 == row)
                    .collect::<Vec<&Beam>>();
                str.push(match possible_beams.len() {
                    0 => *self.grid.get(&(col, row)).unwrap(),
                    1 => possible_beams[0].2,
                    _ => char::from_digit(possible_beams.len() as u32, 10).unwrap(),
                });
            }
            println!("{str}");
        }
        println!("---");
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            ".|...x....
            |.-.x.....
            .....|-...
            ........|.
            ..........
            .........x
            ..../.xx..
            .-.-/..|..
            .|....-|.x
            ..//.|....",
        );

        assert_eq!(51, Map::parse(&input).max_energy());
    }
}
