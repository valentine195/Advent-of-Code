use std::{collections::HashMap, fmt::Error};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(12);
    if input.is_empty() {
        panic!("No input read")
    }
    println!("** Part 1 Final: {:?}", Field::parse(&input, 1).calculate());
    println!("** Part 2 Final: {:?}", Field::parse(&input, 5).calculate());
}

struct Field {
    springs: Vec<SpringRow>,
}
impl Field {
    fn parse(input: &Vec<String>, fold: usize) -> Self {
        let mut vec = Vec::new();

        for line in input {
            vec.push(SpringRow::parse(line, fold).unwrap());
        }

        Field { springs: vec }
    }
    fn calculate(&mut self) -> u64 {
        let mut sum = 0;

        for spring in self.springs.iter_mut() {
            sum += spring.get_ways(spring.row.clone(), 0);
        }

        sum
    }
}

#[derive(Debug)]
struct SpringRow {
    cache: HashMap<(String, usize), u64>,
    row: String,
    groups: Vec<usize>,
}

impl SpringRow {
    fn parse(s: &str, fold: usize) -> Result<Self, Error> {
        let [row, groups] = s.split_whitespace().collect::<Vec<&str>>()[..] else {
            return Err(Error);
        };
        let row = vec![row].repeat(fold).join("?");

        let groups = groups
            .split(",")
            .map(|k| k.parse::<usize>().unwrap())
            .collect::<Vec<usize>>()
            .repeat(fold);

        Ok(SpringRow {
            cache: HashMap::new(),
            row,
            groups,
        })
    }
    fn get_ways(&mut self, line: String, block: usize) -> u64 {
        if self.cache.contains_key(&(line.clone(), block)) {
            return *self.cache.get(&(line, block)).unwrap();
        }
        let groups = &self.groups[block..];

        if line.is_empty() {
            //consumed the line. if there are no more groups to check then we're good.
            return if groups.is_empty() { 1 } else { 0 };
        }

        if groups.is_empty() && line.contains('#') {
            //no groups left but there are still broken springs...
            return 0;
        }
        let perms = match line.chars().nth(0).unwrap() {
            //good spring, move on
            '.' => self.get_ways(line[1..].to_string(), block),
            '?' => {
                //check both options
                self.get_ways(format!("#{}", line[1..].to_string()), block)
                    + self.get_ways(format!(".{}", line[1..].to_string()), block)
            }
            '#' => {
                //group to check
                let group = groups[0];
                if group < line.len() + 1 && line[0..group].chars().all(|c| c != '.') {
                    //the group can fit in the remaining line
                    if group == line.len() {
                        //group will consume the rest of the line..
                        if groups[1..].is_empty() {
                            //no more groups to check, so this is a valid config
                            1
                        } else {
                            //there are more groups to check but we're out of space
                            0
                        }
                    } else {
                        //this character is the character immediately *following* the group
                        //line = ###.?.?### etc, group = 3
                        //###.
                        //...^
                        //this *must* be a good spring for it to be a valid config.
                        match line.chars().nth(group).unwrap_or('X') {
                            '?' => {
                                //in order for this to be a valid config, this *has* to be a good spring
                                self.get_ways(
                                    format!(".{}", line[group + 1..].to_string()),
                                    block + 1,
                                )
                            }
                            '.' => {
                                //consumed a good group of springs
                                self.get_ways(line[group + 1..].to_string(), block + 1)
                            }
                            _ => 0,
                        }
                    }
                } else {
                    //group cannot fit in this space
                    0
                }
            }
            _ => panic!("invalid character"),
        };
        self.cache.insert((line, block), perms);
        return perms;
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn one() {
        let mut spring = SpringRow::parse(&"???.### 1,1,3", 1).unwrap();
        assert_eq!(1, spring.get_ways(spring.row.clone(), 0));
    }
    #[test]
    fn two() {
        let mut spring = SpringRow::parse(&".??..??...?##. 1,1,3", 1).unwrap();
        assert_eq!(4, spring.get_ways(spring.row.clone(), 0));
    }

    #[test]
    fn part_two() {
        let mut spring = SpringRow::parse(&".??..??...?##. 1,1,3", 5).unwrap();
        assert_eq!(16384, spring.get_ways(spring.row.clone(), 0));
    }

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1",
        );
        let mut field = Field::parse(&input, 1);

        assert_eq!(21, field.calculate());
    }
}
