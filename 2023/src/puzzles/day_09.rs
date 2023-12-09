use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(9);

    println!("** Part 1 Final: {:?}", extrapolate(&input));
    println!("** Part 2 Final: {:?}", extrapolate_back(&input));
}

fn parse_series(line: &String) -> Vec<i32> {
    line.split_whitespace()
        .into_iter()
        .map(|v| v.parse::<i32>().unwrap())
        .collect()
}

fn extrapolate(input: &Vec<String>) -> i32 {
    let mut sum = 0;
    for line in input {
        let mut diffs = parse_series(line);

        let mut next = *diffs.last().unwrap();
        while diffs.iter().any(|&v| v != 0) {
            diffs = diffs.windows(2).map(|w| w[1] - w[0]).collect();
            let last = *diffs.last().unwrap();
            next += last;
        }
        sum += next;
    }
    sum
}

fn extrapolate_back(input: &Vec<String>) -> i32 {
    let mut sum = 0;
    for line in input {
        let mut diffs = parse_series(line);
        let mut next = *diffs.first().unwrap();
        //10 - (3 - (0 - (2 - 0))) = 5
        //10 - 3 + 0 - 2 + 0 = 5
        //alternate signs
        let mut sign = -1;
        while diffs.iter().any(|&diff| diff != 0) {
            diffs = diffs
                .windows(2)
                .map(|window| window[1] - window[0])
                .collect();
            let first = *diffs.first().unwrap();
            next += first * sign;
            sign = if sign == 1 { -1 } else { 1 };
        }
        sum += next;
    }
    sum
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45",
        );

        assert_eq!(114, extrapolate(&input));
        assert_eq!(2, extrapolate_back(&input));
    }
}
