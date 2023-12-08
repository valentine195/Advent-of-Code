use std::collections::HashMap;

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(8);

    let map = Map::build(&input);

    println!(
        "** Part 1 Final: {:?}",
        map.traverse(String::from("AAA"), true)
    );
    println!("** Part 2 Final: {:?}", map.traverse_all());
}

#[derive(Debug)]
struct Map {
    map: HashMap<String, Node>,
    directions: Vec<char>,
    positions: Vec<String>,
}
impl Map {
    fn build(input: &Vec<String>) -> Self {
        let mut map = Map {
            map: HashMap::new(),
            directions: Vec::new(),
            positions: Vec::new(),
        };

        for char in input[0].chars() {
            map.directions.push(char);
        }

        for line in input[2..].iter() {
            let (name, node) = Node::parse(line);
            if name.ends_with("A") {
                map.positions.push(name.clone());
            }
            map.map.insert(name, node);
        }

        map
    }
    fn traverse(&self, position: String, part_one: bool) -> u64 {
        let mut steps = 0;
        let mut dir_index: usize = 0;
        let mut next = position.clone();

        while !Map::is_end(&next, part_one) {
            let node = match self.map.get(&next) {
                Some(node) => node,
                None => panic!("what the hell"),
            };

            next = match self.directions[dir_index] {
                'L' => node.0.clone(),

                'R' => node.1.clone(),

                _ => panic!("where did this direction come from??"),
            };
            steps += 1;
            dir_index = self.get_next_dir(dir_index);
        }

        steps
    }
    fn traverse_all(&self) -> u64 {
        let mut steps = Vec::new();

        for position in self.positions.iter() {
            steps.push(self.traverse(position.to_owned(), false));
        }

        steps.iter().fold(1, |acc, c| num_integer::lcm(acc, *c))
    }
    fn is_end(next: &String, part_one: bool) -> bool {
        (!part_one && next.ends_with("Z")) || (part_one && next == "ZZZ")
    }
    fn get_next_dir(&self, index: usize) -> usize {
        (index + 1) % self.directions.len()
    }
}
#[derive(Debug)]
struct Node(String, String);
impl Node {
    fn parse(s: &str) -> (String, Self) {
        let [name, nodes] = s.split(" = ").collect::<Vec<&str>>()[..] else {
            panic!("couldn't split");
        };
        let [left, right] = nodes[1..nodes.len() - 1].split(", ").collect::<Vec<&str>>()[..] else {
            panic!("couldn't split nodes");
        };

        (name.to_string(), Node(left.to_string(), right.to_string()))
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part_1() {
        let input: Vec<String> = input::split_string(
            "RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)",
        );

        let map = Map::build(&input);
        assert_eq!(2, map.traverse(String::from("AAA"), true));
    }
    #[test]
    fn part_2() {
        let input: Vec<String> = input::split_string(
            "LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)",
        );

        let map = Map::build(&input);
        assert_eq!(6, map.traverse_all());
    }
}
