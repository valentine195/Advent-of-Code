use crate::input;
use core::fmt;
use std::{
    collections::{HashMap, VecDeque},
    fmt::Debug,
    str::FromStr,
};

pub fn run() {
    let input = input::read_day_input_whole(20);
    let mut communication = input.parse::<Communication>().unwrap();

    println!("** Part 1 Final: {:?}", communication.run());
    println!("** Part 2 Final: {:?}", communication.find_rx_out());
}
#[derive(Default, Debug, Clone, Copy, PartialEq)]
enum Pulse {
    #[default]
    Low,
    High,
}
impl Pulse {
    fn invert(&self) -> Pulse {
        match self {
            Pulse::Low => Pulse::High,
            Pulse::High => Pulse::Low,
        }
    }
}

#[derive(Debug, Clone)]
enum ModuleType {
    FlipFlop,
    Conjunction(HashMap<String, Pulse>),
    Broadcaster,
}
#[derive(Debug, Clone)]
struct Module {
    kind: ModuleType,
    state: Pulse,
}
impl Module {
    fn pulse(&mut self, signal: Pulse, name: String) {
        match &mut self.kind {
            ModuleType::FlipFlop => match signal {
                Pulse::Low => self.state = self.state.invert(),
                Pulse::High => {}
            },
            ModuleType::Conjunction(inputs) => {
                inputs.insert(name, signal);
                self.state = match inputs.iter().all(|(_, &p)| p == Pulse::High) {
                    true => Pulse::Low,
                    false => Pulse::High,
                }
            }
            ModuleType::Broadcaster => self.state = signal,
        }
    }
    fn add_input(&mut self, name: String) {
        match &mut self.kind {
            ModuleType::Conjunction(inputs) => {
                inputs.insert(name, Pulse::Low);
            }
            _ => {}
        };
    }
    fn should_continue(&self, signal: Pulse) -> bool {
        match self.kind {
            ModuleType::FlipFlop => match signal {
                Pulse::Low => true,
                Pulse::High => false,
            },
            _ => true,
        }
    }
}

type Modules = HashMap<String, Module>;
type Targets = HashMap<String, Vec<String>>;

#[derive(Debug)]
struct Communication {
    modules: Modules,
    targets: Targets,
}
impl Communication {
    fn run(&mut self) -> i32 {
        let mut low = 0;
        let mut high = 0;
        for _ in 0..1000 {
            let mut queue = VecDeque::from([("broadcaster", Pulse::default())]);
            //pulse the broadcaster
            low += 1;
            while let Some((name, signal)) = queue.pop_front() {
                match self.targets.get(name) {
                    Some(targets) => {
                        for next in targets {
                            match signal {
                                Pulse::Low => low += 1,
                                Pulse::High => high += 1,
                            }

                            match self.modules.get_mut(next) {
                                Some(target) => {
                                    if !target.should_continue(signal) {
                                        continue;
                                    }
                                    target.pulse(signal, name.to_string());
                                    queue.push_back((next, target.state));
                                }
                                None => continue,
                            }
                        }
                    }
                    None => continue,
                }
            }
        }
        low * high
    }

    fn find_rx_out(&mut self) -> i128 {
        let last = self
            .targets
            .iter()
            .find_map(|(name, targets)| {
                if targets.contains(&"rx".to_string()) {
                    Some(name)
                } else {
                    None
                }
            })
            .unwrap();

        //find each loop that outputs into
        let end_loops = self.targets.iter().filter_map(|(name, targets)| {
            if targets.contains(last) {
                Some(name)
            } else {
                None
            }
        });
        let mut loops: Vec<i128> = Vec::new();
        for target in end_loops {
            loops.push(self.run_loop(&mut self.modules.clone(), target))
        }

        loops.iter().fold(1, |acc, c| num_integer::lcm(acc, *c))
    }
    fn run_loop(&self, modules: &mut Modules, target: &str) -> i128 {
        let mut count = 0;
        'main_loop: loop {
            count += 1;
            let mut queue = VecDeque::from([("broadcaster", Pulse::default())]);
            queue.push_back(("broadcaster", Pulse::default()));
            //pulse the broadcaster
            while let Some((name, signal)) = queue.pop_front() {
                match self.targets.get(name) {
                    Some(targets) => {
                        for next in targets {
                            match modules.get_mut(next) {
                                Some(t) => {
                                    if !t.should_continue(signal) {
                                        continue;
                                    }
                                    if next == target && t.state == Pulse::High {
                                        break 'main_loop;
                                    }
                                    t.pulse(signal, name.to_string());
                                    queue.push_back((next, t.state));
                                }
                                None => continue,
                            }
                        }
                    }
                    None => continue,
                }
            }
        }
        count
    }
}
impl FromStr for Communication {
    type Err = fmt::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut communication = Communication {
            modules: HashMap::new(),
            targets: HashMap::new(),
        };
        s.trim().lines().for_each(|line| {
            let (src, dest) = line.trim().split_once(" -> ").unwrap();
            let (module, name): (Module, &str) = match src.chars().collect::<Vec<char>>()[0] {
                'b' => (
                    Module {
                        kind: ModuleType::Broadcaster,
                        state: Pulse::Low,
                    },
                    "broadcaster",
                ),
                '%' => (
                    Module {
                        kind: ModuleType::FlipFlop,
                        state: Pulse::Low,
                    },
                    &src[1..],
                ),
                '&' => (
                    Module {
                        kind: ModuleType::Conjunction(HashMap::new()),
                        state: Pulse::Low,
                    },
                    &src[1..],
                ),
                _ => unreachable!(),
            };
            let targets = dest.split(", ").map(|s| s.to_string()).collect();
            communication
                .modules
                .insert(name.trim().to_string(), module);
            communication
                .targets
                .insert(name.trim().to_string(), targets);
        });
        for (name, targets) in communication.targets.iter() {
            for target in targets {
                communication
                    .modules
                    .get_mut(target)
                    .map(|module| module.add_input(name.clone()));
            }
        }

        Ok(communication)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a",
        );

        let mut communication = input.join("\n").parse::<Communication>().unwrap();
        assert_eq!(32000000, communication.run());
    }
    #[test]
    fn test_interesting() {
        let input: Vec<String> = input::split_string(
            "broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output",
        );

        let mut communication = input.join("\n").parse::<Communication>().unwrap();

        assert_eq!(11687500, communication.run());
    }
}
