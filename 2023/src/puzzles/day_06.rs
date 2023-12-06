use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(6);

    println!("** Part 1 Final: {:?}", get_ways(&input, false));
    println!("** Part 2 Final: {:?}", get_ways(&input, true));
}

fn get_ways(input: &Vec<String>, part_two: bool) -> i32 {
    let mut ways = Vec::new();
    let (times, dists) = parse(input, part_two);
    for i in 0..times.len() {
        ways.push(get_ways_for_time(times[i], dists[i]));
    }

    ways.iter().filter(|x| x > &&0).fold(1, |acc, x| acc * *x as i32)
}

fn get_ways_for_time(time: i64, dist: i64) -> i64 {
    let mids: (i64, i64) = (
        ((time as f64) / 2.0).floor() as i64,
        ((time as f64) / 2.0).ceil() as i64,
    );

    if mids.0 * mids.1 < dist {
        return 0;
    }

    let times = Vec::from_iter(1..=(time as i64));
    let (mut left, mut right) = (
        times.iter().position(|x| x == &mids.0).expect("left"),
        times.iter().position(|x| x == &mids.1).expect("right"),
    );
    //an odd number of tries will have 2 chances because the mid point is between two numbers
    let mut count = if times.len() % 2 == 0 { 1 } else { 2 };
    while times[left - 1] * times[right + 1] > dist {
        //+2 because left * right = right * left
        count += 2;
        left -= 1;
        right += 1;
    }
    count
}

fn parse_line(line: &String, part_two: bool) -> Vec<i64> {
    if part_two {
        let str: String = line
            .split(": ")
            .last()
            .expect("string")
            .split_whitespace()
            .into_iter()
            .collect::<Vec<&str>>()
            .join("");

        return vec![str.parse::<i64>().unwrap()];
    }

    line.split(": ")
        .last()
        .expect("string")
        .split_whitespace()
        .into_iter()
        .map(|str| str.parse::<i64>().unwrap())
        .collect()
}

fn parse(input: &Vec<String>, part_two: bool) -> (Vec<i64>, Vec<i64>) {
    (
        parse_line(&input[0], part_two),
        parse_line(&input[1], part_two),
    )
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "Time:      7  15   30
Distance:  9  40  200",
        );

        assert_eq!(288, get_ways(&input, false));
        assert_eq!(71503, get_ways(&input, true));
    }
}
