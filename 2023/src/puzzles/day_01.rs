use crate::input;
use regex::Regex;

pub fn run() {
    println!("running day 1");
    let input = input::read_day_input(1, true);

    println!("** Part 1 Final: {:?}", get_sums(&input, true));

    println!("** Part 2 Final: {:?}", get_sums(&input, false));
}

fn get_sums(input: &Vec<String>, part1: bool) -> i32 {
    let mut sum = 0;

    let start = if part1 {
        Regex::new(r"^.*?(\d)").unwrap()
    } else {
        Regex::new(r"^.*?(\d|one|two|three|four|five|six|seven|eight|nine)").unwrap()
    };
    let end = if part1 {
        Regex::new(r"^.*(\d).*?$").unwrap()
    } else {
        Regex::new(r"^.*(\d|one|two|three|four|five|six|seven|eight|nine).*?$").unwrap()
    };
    for line in input {
        let start_cap = start.captures(&line).unwrap();
        let end_cap = end.captures(&line).unwrap();

        let n1 = start_cap.get(1).unwrap().as_str();
        let n2 = match end_cap.get(1) {
            Some(_) => end_cap.get(1).unwrap().as_str(),
            None => &n1,
        };
        sum += get_value(n1) * 10 + get_value(n2);
    }

    sum
}

fn get_value(input: &str) -> i32 {
    match input {
        "one" => 1,
        "two" => 2,
        "three" => 3,
        "four" => 4,
        "five" => 5,
        "six" => 6,
        "seven" => 7,
        "eight" => 8,
        "nine" => 9,
        _ => input.parse::<i32>().unwrap(),
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet",
        );
        let sums = get_sums(&input, true);
        assert_eq!(sums, 142)
    }
}
