use std::{collections::HashMap, fmt::Error, str::FromStr};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(15);
    let input = input::split_string_by(&input[0][..], ',');
    println!("** Part 1 Final: {:?}", get_hash(&input));
    println!("** Part 2 Final: {:?}", Facility::parse(&input).power());
}

fn hash_str(str: &str) -> u32 {
    let mut current = 0;

    for ch in str.bytes() {
        current += ch as u32;
        current *= 17;
        current = current % 256;
    }

    current
}
fn get_hash(lines: &Vec<String>) -> u32 {
    lines.iter().fold(0, |acc, x| acc + hash_str(x))
}

#[derive(Debug, PartialEq)]
enum Operation {
    REMOVE,
    INSERT,
}
impl Operation {
    fn char(&self) -> char {
        if self == &Operation::INSERT {
            '='
        } else {
            '-'
        }
    }
}
#[derive(Debug, PartialEq)]
struct Lens {
    label: String,
    hash: u32,
    lens: Option<u32>,
    op: Operation,
}
impl FromStr for Lens {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let op = if s.contains('-') {
            Operation::REMOVE
        } else {
            Operation::INSERT
        };

        let [label, lens] = s.split(op.char()).collect::<Vec<&str>>()[..] else {
            panic!("couldn't split")
        };

        Ok(Lens {
            op,
            label: label.to_string(),
            hash: hash_str(&label),
            lens: if lens.len() > 0 {
                Some(lens.parse::<u32>().unwrap())
            } else {
                None
            },
        })
    }
}

#[derive(Debug)]
struct Box {
    lenses: Vec<Lens>,
}
impl Box {
    fn create() -> Self {
        Box { lenses: Vec::new() }
    }
    fn handle(&mut self, lens: Lens) {
        match lens.op {
            Operation::REMOVE => self.remove(lens),
            Operation::INSERT => self.insert(lens),
        };
    }
    fn insert(&mut self, lens: Lens) {
        if self.lenses.iter().any(|l| l.label == lens.label) {
            let idx = self
                .lenses
                .iter()
                .position(|l| l.label == lens.label)
                .unwrap();
            self.lenses[idx] = lens;
        } else {
            self.lenses.push(lens);
        }
    }
    fn remove(&mut self, lens: Lens) {
        if self.lenses.iter().any(|l| l.label == lens.label) {
            let idx = self
                .lenses
                .iter()
                .position(|l| l.label == lens.label)
                .unwrap();
            self.lenses.remove(idx);
        }
    }
}

#[derive(Debug)]
struct Facility {
    boxes: HashMap<u32, Box>,
}
impl Facility {
    fn parse(input: &Vec<String>) -> Self {
        let mut boxes = HashMap::new();

        for def in input {
            let lens = def.parse::<Lens>().unwrap_or_else(|_| panic!("bad lens"));
            let key = lens.hash;
            let _box = boxes.entry(key).or_insert(Box::create());
            _box.handle(lens);
            if _box.lenses.is_empty() {
                boxes.remove(&key);
            }
        }

        Facility { boxes }
    }
    fn power(&self) -> u32 {
        let mut power = 0;
        for (num, _box) in self.boxes.iter() {
            for (idx, lens) in _box.lenses.iter().enumerate() {
                power += (num + 1) * (idx as u32 + 1) * lens.lens.unwrap();
            }
        }
        power
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> =
            input::split_string_by("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7", ',');

        assert_eq!(1320, get_hash(&input));
        assert_eq!(145, Facility::parse(&input).power());
    }
}
