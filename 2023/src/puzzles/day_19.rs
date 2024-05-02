use std::{collections::HashMap, fmt::Error, str::FromStr};

use crate::input;

pub fn run() {
    let input: String = input::read_day_input_whole(19);

    let manager: Manager = input.parse::<Manager>().unwrap();
    let sum = manager.run();

    println!("** Part 1 Final: {:?}", sum);

    let ranges = manager.find_good_ranges();
    println!("** Part 2 Final: {:?}", ranges);
}

#[derive(Eq, Hash, PartialEq, Debug)]
enum Property {
    X,
    M,
    A,
    S,
}
impl Property {
    fn from(s: char) -> Self {
        match s {
            'x' => Property::X,
            'm' => Property::M,
            'a' => Property::A,
            's' => Property::S,
            _ => unreachable!(),
        }
    }
}

#[derive(Debug)]
struct Part {
    x: Option<u32>,
    m: Option<u32>,
    a: Option<u32>,
    s: Option<u32>,
}
impl Part {
    fn sum(&self) -> u32 {
        self.x.unwrap() + self.m.unwrap() + self.a.unwrap() + self.s.unwrap()
    }
    fn get(&self, property: &Property) -> u32 {
        match property {
            Property::X => self.x.unwrap(),
            Property::M => self.m.unwrap(),
            Property::A => self.a.unwrap(),
            Property::S => self.s.unwrap(),
        }
    }
}
// {x=787,m=2655,a=1222,s=2876}
impl FromStr for Part {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut chars = s.chars().fuse().peekable();

        let mut part = Part {
            x: None,
            a: None,
            s: None,
            m: None,
        };

        let mut value: String = String::new();
        let mut current: Property = Property::A;

        while let Some(c) = chars.next() {
            match c {
                'x' | 'a' | 'm' | 's' => {
                    current = Property::from(c);
                    value = String::new();
                }
                '0'..='9' => {
                    value.push(c);
                }
                ',' | '}' => {
                    match current {
                        Property::X => part.x = Some(value.parse::<u32>().unwrap()),
                        Property::M => part.m = Some(value.parse::<u32>().unwrap()),
                        Property::A => part.a = Some(value.parse::<u32>().unwrap()),
                        Property::S => part.s = Some(value.parse::<u32>().unwrap()),
                    }
                    //consume
                }
                _ => continue,
            }
        }

        Ok(part)
    }
}

#[derive(Debug)]
enum Operator {
    Greater,
    Less,
}
impl Operator {
    fn process(&self, value: &u32, target: &u32) -> bool {
        match self {
            Operator::Greater => value > target,
            Operator::Less => value < target,
        }
    }
    fn parse(s: char) -> Self {
        match s {
            '>' => Operator::Greater,
            '<' => Operator::Less,
            _ => unreachable!(),
        }
    }
}

#[derive(Debug)]
struct Condition {
    property: Option<Property>,
    operator: Option<Operator>,
    target: Option<u32>,
    next: String,
}

impl Condition {
    fn process(&self, part: &Part) -> bool {
        match &self.property {
            Some(property) => self
                .operator
                .as_ref()
                .unwrap()
                .process(&part.get(property), &self.target.unwrap()),
            None => true,
        }
    }
    /* fn from(s: char) -> Self {

    } */
}

#[derive(Debug)]
enum ConditionContext {
    Property,
    Operator,
    Target,
    Next,
}
// px{a<2006:qkq,m>2090:A,rfg}
impl FromStr for Condition {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if !s.contains(":") {
            Ok(Condition {
                property: None,
                operator: None,
                target: None,
                next: s.to_string(),
            })
        } else {
            let mut chars = s.chars().fuse().peekable();
            let mut context = ConditionContext::Property;
            let mut condition = Condition {
                property: None,
                operator: None,
                target: None,
                next: String::new(),
            };
            while let Some(c) = chars.next() {
                match context {
                    ConditionContext::Property => {
                        condition.property = Some(Property::from(c));
                        context = ConditionContext::Operator;
                    }
                    ConditionContext::Operator => {
                        condition.operator = Some(Operator::parse(c));
                        context = ConditionContext::Target;
                    }
                    ConditionContext::Target => {
                        let mut value = c.to_string();
                        loop {
                            let next = chars.peek();
                            if next.is_some() && next.unwrap().is_digit(10) {
                                value.push(chars.next().unwrap())
                            } else {
                                break;
                            }
                        }
                        condition.target = Some(value.parse::<u32>().unwrap());
                        context = ConditionContext::Next;
                    }
                    ConditionContext::Next => {
                        let mut value = String::new();
                        while let Some(next) = chars.peek() {
                            if next == &':' {
                                continue;
                            } else {
                                value.push(chars.next().unwrap());
                            }
                        }
                        condition.next = value;
                    }
                }
            }
            Ok(condition)
        }
    }
}

#[derive(Debug)]
struct Workflow {
    id: String,
    conditions: Vec<Condition>,
}
impl Workflow {
    fn process(&self, part: &Part) -> &str {
        for condition in self.conditions.iter() {
            if condition.process(part) {
                return &condition.next;
            }
        }
        return "R";
    }
}
enum WorkflowContext {
    ID,
    Condition,
}
impl FromStr for Workflow {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut context = WorkflowContext::ID;
        let mut id = String::new();
        let mut conditions: Vec<Condition> = Vec::new();
        let mut condition = String::new();
        for char in s.chars() {
            match char {
                '{' => {
                    context = WorkflowContext::Condition;
                }
                ',' | '}' => {
                    conditions.push(condition.parse::<Condition>().unwrap());
                    condition = String::new();
                } //end,
                _ => match context {
                    WorkflowContext::ID => id.push(char),
                    WorkflowContext::Condition => condition.push(char),
                },
            }
        }

        Ok(Workflow { id, conditions })
    }
}

#[derive(Clone, Copy, Debug)]
struct PartRange(i128, i128);
impl PartRange {
    fn value(&self) -> i128 {
        self.1 - self.0 + 1
    }
}
#[derive(Clone, Copy, Debug)]
struct AcceptableParts {
    x: PartRange,
    a: PartRange,
    m: PartRange,
    s: PartRange,
}
impl AcceptableParts {
    fn calc(&self) -> i128 {
        self.x.value() * self.a.value() * self.m.value() * self.s.value()
    }
    fn set(&mut self, property: &Property, max: bool, value: i128) {
        match (property, max) {
            (Property::X, true) => self.x.1 = value,
            (Property::X, false) => self.x.0 = value,
            (Property::M, true) => self.m.1 = value,
            (Property::M, false) => self.m.0 = value,
            (Property::A, true) => self.a.1 = value,
            (Property::A, false) => self.a.0 = value,
            (Property::S, true) => self.s.1 = value,
            (Property::S, false) => self.s.0 = value,
        }
    }
}

#[derive(Debug)]
struct Manager {
    workflows: HashMap<String, Workflow>,
    parts: Vec<Part>,
}
impl Manager {
    fn run(&self) -> u32 {
        let mut sum: u32 = 0;
        for part in self.parts.iter() {
            if self.process(part) {
                sum += part.sum();
            }
        }

        sum
    }

    fn find_good_ranges(&self) -> i128 {
        let range = AcceptableParts {
            x: PartRange(1, 4000),
            a: PartRange(1, 4000),
            s: PartRange(1, 4000),
            m: PartRange(1, 4000),
        };

        let ranges = self.process_range(&"in", range);

        let sum = ranges.iter().fold(0, |acc, r| acc + r.calc());

        sum
    }

    fn process_range(
        &self,
        target_workflow: &str,
        mut range: AcceptableParts,
    ) -> Vec<AcceptableParts> {
        let mut ranges: Vec<AcceptableParts> = Vec::new();
        match target_workflow {
            "R" => return Vec::new(),
            "A" => return vec![range],
            _ => {
                let Some(workflow) = self.workflows.get(target_workflow) else {
                    panic!()
                };
                for condition in workflow.conditions.iter() {
                    match condition.operator {
                        Some(Operator::Greater) => {
                            let mut new_range = range.clone();
                            new_range.set(
                                &condition.property.as_ref().unwrap(),
                                false,
                                condition.target.unwrap() as i128 + 1,
                            );
                            ranges.append(&mut self.process_range(&condition.next, new_range));

                            range.set(
                                &condition.property.as_ref().unwrap(),
                                true,
                                condition.target.unwrap() as i128,
                            );
                        }
                        Some(Operator::Less) => {
                            let mut new_range = range.clone();
                            new_range.set(
                                &condition.property.as_ref().unwrap(),
                                true,
                                condition.target.unwrap() as i128 - 1,
                            );
                            ranges.append(&mut self.process_range(&condition.next, new_range));

                            range.set(
                                &condition.property.as_ref().unwrap(),
                                false,
                                condition.target.unwrap() as i128,
                            );
                        }
                        None => {
                            ranges.append(&mut self.process_range(&condition.next, range));
                        }
                    }
                }
            }
        }
        ranges
    }

    fn process(&self, part: &Part) -> bool {
        let mut current: &Workflow = self.workflows.get("in").unwrap();
        loop {
            match current.process(part) {
                "A" => {
                    return true;
                }
                "R" => {
                    return false;
                }

                next => {
                    current = self.workflows.get(next).unwrap();
                    continue;
                }
            }
        }
    }
}
impl FromStr for Manager {
    type Err = Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let [workflows, parts] = s.split("\n\n").collect::<Vec<&str>>()[..] else {
            return Err(std::fmt::Error);
        };
        Ok(Manager {
            parts: parts
                .split("\n")
                .map(|s| s.parse::<Part>().unwrap())
                .collect(),
            workflows: workflows
                .split("\n")
                .map(|s| {
                    let workflow = s.parse::<Workflow>().unwrap();
                    return (workflow.id.clone(), workflow);
                })
                .collect(),
        })
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: String = "px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}"
            .to_string();

        let manager: Manager = input.parse::<Manager>().unwrap();

        let sum = manager.run();
        let ranges = manager.find_good_ranges();

        assert_eq!(19114, sum);
        assert_eq!(167409079868000, ranges);
    }
}
