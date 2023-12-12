use std::{fmt::Error, str::FromStr};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(12);

    println!("** Part 1 Final: {:?}", 0);
    println!("** Part 2 Final: {:?}", 0);
}
struct SpringRow {
    row: Vec<char>,
    blocks: Vec<i32>,
}

impl FromStr for SpringRow {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let [row, blocks] = s.split_whitespace().collect::<Vec<&str>>()[..] else {
            return Err(Error);
        };

        let blocks = blocks
            .split(",")
            .map(|k| k.parse::<i32>().unwrap())
            .collect::<Vec<i32>>();

        Ok(SpringRow {
            row: row.to_string().chars().collect(),
            blocks,
        })
    }
}

fn countArrangements(input: &Vec<String>) -> i32 {
    let mut arrangements = 0;

    for line in input {
        let spring_row = line.parse::<SpringRow>().expect("good");
    }

    arrangements
}
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(".??..??...?##. 1,1,3");
        println!("{}", countArrangements(&input));
    }
}
