use std::{
    alloc::System,
    collections::{BTreeSet, HashSet},
    fmt::Error,
    str::FromStr,
};

use crate::input;

pub fn run() {
    let input = input::read_day_input(5);
    let garden = Garden::build(&input);
    println!("** Part 1 Final: {:?}", garden.find_min());
    println!("** Part 2 Final: {:?}", garden.find_location());
}

#[derive(Debug)]
struct MapEntry {
    source: u64,
    dest: u64,
    range: u64,
}
impl MapEntry {
    fn process_seed(&self, seed: Seed, inverted: bool) -> Option<u64> {
        let source = if inverted { self.dest } else { self.source };
        if (source..(source + self.range)).contains(&seed) {
            let val = if inverted {
                seed - self.dest + self.source
            } else {
                seed - self.source + self.dest
            };
            Some(val)
        } else {
            None
        }
    }
    fn end(&self) -> u64 {
        self.source + self.range
    }
}
impl FromStr for MapEntry {
    type Err = Error;

    fn from_str(line: &str) -> Result<Self, Self::Err> {
        let nums: Vec<u64> = line
            .split(" ")
            .into_iter()
            .map(|c| c.parse::<u64>().unwrap())
            .collect();

        let [dest, source, range] = nums[..] else {
            return Err(Error);
        };
        Ok(MapEntry {
            source,
            dest,
            range,
        })
    }
}

#[derive(Debug)]
struct Map {
    entries: Vec<MapEntry>,
}
impl Map {
    fn process_seed(&self, seed: Seed, inverted: bool) -> u64 {
        for entry in self.entries.iter() {
            match entry.process_seed(seed, inverted) {
                Some(dest) => {
                    return dest;
                }
                None => continue,
            }
        }
        seed
    }
    fn find_bounds(&self, range: &(u64, u64)) -> Vec<(u64, u64)> {
        let mut slices = BTreeSet::new();
        for entry in &self.entries {
            if range.1 < entry.source || range.0 > entry.end() {
                continue;
            }
            if entry.source > range.0 {
                slices.insert(entry.source);
            }
            if entry.end() < range.1 {
                slices.insert(entry.end());
            }
        }
        slices.insert(range.1);
        let mut output = Vec::new();
        let mut current = range.0;

        for position in slices {
            let next = self.process_seed(current, false);
            output.push((next, next + position - current));
            current = position;
        }
        output
    }
}

struct Garden {
    maps: Vec<Map>,
    seeds: Seeds,
}
impl Garden {
    fn build(input: &Vec<String>) -> Self {
        let mut garden = Garden {
            maps: Vec::new(),
            seeds: input[0]
                .split(": ")
                .last()
                .expect("string")
                .split_whitespace()
                .map(|v| v.parse::<u64>().expect("number"))
                .collect(),
        };

        let mut map = Map {
            entries: Vec::new(),
        };
        for line in &input[3..] {
            let start = match line.chars().next() {
                Some(c) => c,
                None => {
                    continue;
                }
            };

            if !start.is_numeric() && map.entries.len() > 0 {
                garden.maps.push(map);
                map = Map {
                    entries: Vec::new(),
                };
                continue;
            }
            match line.parse::<MapEntry>() {
                Ok(entry) => map.entries.push(entry),
                Err(_) => panic!("uh oh"),
            }
        }

        if map.entries.len() > 0 {
            garden.maps.push(map);
        }

        garden
    }
    fn process_seed(&self, seed: Seed) -> u64 {
        let mut loc = seed;

        for map in self.maps.iter() {
            loc = map.process_seed(loc, false)
        }

        loc
    }
    fn process_backwards(&self, seed: Seed) -> u64 {
        let mut loc = seed;

        for map in self.maps.iter().rev() {
            loc = map.process_seed(loc, true)
        }

        loc
    }

    fn find_min(&self) -> u64 {
        self.seeds
            .iter()
            .map(|s| self.process_seed(*s))
            .min()
            .unwrap()
    }

    fn seed_ranges(&self) -> Vec<(u64, u64)> {
        self.seeds
            .chunks(2)
            .map(|range| (range[0], range[0] + range[1] - 1))
            .collect()
    }

    fn find_location(&self) -> u64 {
        let mut current: Vec<(u64, u64)> = self.seed_ranges();
        let mut future = Vec::new();

        for map in &self.maps {
            for range in current {
                let converted = map.find_bounds(&range);
                future.extend(converted);
            }
            current = future;
            future = Vec::new();
        }

        current.iter().map(|range| range.0).min().unwrap()
    }
}

type Seed = u64;

type Seeds = Vec<Seed>;

/* fn parse_map(input: &Vec<String>) -> (Seeds, Garden) {
    let seeds: Seeds = input[0]
        .split(": ")
        .last()
        .expect("string")
        .split_whitespace()
        .map(|v| v.parse::<u64>().expect("number"))
        .collect();

    let mut garden: Garden = Garden { maps: Vec::new() };

    let mut map = Map {
        entries: Vec::new(),
    };
    for line in &input[3..] {
        let start = match line.chars().next() {
            Some(c) => c,
            None => {
                continue;
            }
        };

        if !start.is_numeric() && map.entries.len() > 0 {
            garden.maps.push(map);
            map = Map {
                entries: Vec::new(),
            };
            continue;
        }
        match line.parse::<MapEntry>() {
            Ok(entry) => map.entries.push(entry),
            Err(_) => panic!("uh oh"),
        }
    }

    if map.entries.len() > 0 {
        garden.maps.push(map);
    }

    (seeds, garden)
}
 */
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4",
        );
        let garden = Garden::build(&input);

        assert_eq!(82, garden.process_seed(79));
        assert_eq!(43, garden.process_seed(14));
        assert_eq!(86, garden.process_seed(55));
        assert_eq!(35, garden.process_seed(13));
        assert_eq!(35, garden.find_min());

        assert_eq!(46, garden.find_location());
    }
}
