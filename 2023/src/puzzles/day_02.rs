use std::cmp;

use crate::input;
use regex::Regex;

pub fn run() {
    let input = input::read_day_input(2);

    println!("running day 2");

    let mut sums = sum_good_games(&input);
    println!("** Part 1 Final: {:?}", sums);

    sums = power_marbles(&input);
    println!("** Part 2 Final: {:?}", sums);
}

struct Marbles {
    red: i32,
    green: i32,
    blue: i32,
}

fn power_marbles(games: &Vec<String>) -> i32 {
    let mut sum: i32 = 0;

    for game in games {
        let mut max = Marbles {
            red: 0,
            green: 0,
            blue: 0,
        };
        for marble_list in game.split(";") {
            let marbles = extract_marbles(marble_list);
            max.red = cmp::max(max.red, marbles.red);
            max.green = cmp::max(max.green, marbles.green);
            max.blue = cmp::max(max.blue, marbles.blue);
        }
        sum += max.red * max.green * max.blue;
    }

    sum
}

fn sum_good_games(games: &Vec<String>) -> i32 {
    let mut sum: i32 = 0;
    let maxes = Marbles {
        red: 12,
        green: 13,
        blue: 14,
    };

    let regex = Regex::new(r"Game (\d+)").unwrap();

    for game in games {
        if is_good_game(&game, &maxes) {
            sum += regex
                .captures(&game)
                .unwrap()
                .get(1)
                .unwrap()
                .as_str()
                .parse::<i32>()
                .unwrap();
        }
    }
    return sum;
}

fn is_good_game(marbles: &str, maxes: &Marbles) -> bool {
    //3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    for line in marbles.split(";") {
        let marbles = extract_marbles(line);
        if marbles.red > maxes.red {
            return false;
        }
        if marbles.green > maxes.green {
            return false;
        }
        if marbles.blue > maxes.blue {
            return false;
        }
    }
    return true;
}

fn extract_marbles(m: &str) -> Marbles {
    let mut marbles = Marbles {
        red: 0,
        green: 0,
        blue: 0,
    };

    let regex = Regex::new(r"(\d+) (blue|green|red)").unwrap();
    for (_, [amt, color]) in regex.captures_iter(m).map(|c| c.extract()) {
        let marble_amt = amt.parse::<i32>().unwrap();
        match color {
            "red" => marbles.red = marble_amt,
            "green" => marbles.green = marble_amt,
            "blue" => marbles.blue = marble_amt,
            _ => panic!("What color is this??"),
        };
    }

    marbles
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green",
        );

        let sums = sum_good_games(&input);
        assert_eq!(sums, 8)
    }
}
