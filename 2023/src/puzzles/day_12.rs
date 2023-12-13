use std::{collections::HashMap, fmt::Error, str::FromStr};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(12);

    println!("** Part 1 Final: {:?}", 0);
    println!("** Part 2 Final: {:?}", 0);
}
struct SpringRow {
    cache: HashMap<(usize, usize), i32>,
    row: Vec<char>,
    groups: Vec<usize>,
}

impl SpringRow {
    fn get_ways(&mut self, position: usize, block: usize, adder: Option<char>) -> i32 {
        /* if self.cache.contains_key(&(position, block)) {
            return *self.cache.get(&(position, block)).unwrap();
        } */
        let mut line = self.row[position..].to_vec();
        if adder.is_some() {
            line[0] = adder.unwrap();
            println!("line: {:?}, adder: {:?}", line, adder.unwrap());
        }
        println!(
            "{:?}",
            self.row
                .iter()
                .map(|c| c.to_string())
                .collect::<Vec<String>>()
                .join("")
        );
        let mut pointer = " ".repeat(position);
        pointer.push('^');
        println!("{:?}", pointer);
        let groups = &self.groups[block..];
        println!("line: {}, groups: {}", line.len(), groups.len());
        let mut val: i32 = 0;
        if line.is_empty() && groups.is_empty() {
            //both consumed, found a good one
            println!("here!!");
            val = 1;
        } else if groups.is_empty() && !line.contains(&'#') {
            //groups done but the remaining spaces can be good springs
            println!("here!!");
            val = 1;
        } else if line.len() < groups.iter().sum::<usize>() + groups.len() - 1 {
            //line is not long enough for remaining runs
            val = 0;
        } else {
            println!("char: {}", line[0]);
            val = match line[0] {
                '.' => self.get_ways(position + 1, block, None),
                '#' => {
                    if line[0..groups[0]].contains(&'.') {
                        //the group range contains a good spring and therefore cannot contain a bad one
                        0
                    } else if groups[0] < line.len() && line[groups[0] as usize] == '#' {
                        0
                    } else {
                        println!("here");
                        self.get_ways(position + block + 1, block + 1, None)
                    }
                }
                '?' => {
                    self.get_ways(position, block, Some('#'))
                        + self.get_ways(position, block, Some('.'))
                }
                _ => panic!("help"),
            };
        }

        self.cache.insert((position, block), val);
        println!("75 val: {val}");
        val
    }
}

impl FromStr for SpringRow {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let [row, groups] = s.split_whitespace().collect::<Vec<&str>>()[..] else {
            return Err(Error);
        };

        let groups = groups
            .split(",")
            .map(|k| k.parse::<usize>().unwrap())
            .collect::<Vec<usize>>();

        Ok(SpringRow {
            cache: HashMap::new(),
            row: row.to_string().chars().collect(),
            groups,
        })
    }
}

fn count_ways(input: &Vec<String>) -> i32 {
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
        let mut spring = input[0].parse::<SpringRow>().unwrap();
        println!("{}", spring.get_ways(0, 0, None));
    }
}
