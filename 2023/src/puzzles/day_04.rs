use std::collections::HashSet;

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(4);

    let sum = play_games(&input);
    println!("** Part 1 Final: {:?}", sum);
    let cards = scratch_cards(&input);
    println!("** Part 2 Final: {:?}", cards);
}

fn scratch_cards(input: &Vec<String>) -> i32 {
    let mut cards = vec![1; input.len()];

    for idx in 0..input.len() {
        let game = &input[idx];
        let count = play_game(game);
        if count > 0 {
            for add in 1..=count {
                cards[idx + add as usize] += cards[idx];
            }
        }
    }

    cards.iter().sum()
}

fn play_games(input: &Vec<String>) -> i32 {
    let mut sum = 0;
    println!("here");
    for game in input {
        let count = play_game(game);
        sum += if count > 0 { 2i32.pow(count - 1) } else { 0 };
    }

    sum
}

fn play_game(game: &str) -> u32 {
    let mut count: u32 = 0;
    let (winning, numbers) = get_numbers(game.split(": ").last().expect("numbers").trim());

    for number in numbers {
        if winning.contains(&number) {
            count += 1;
        }
    }

    count
}

fn get_numbers(game: &str) -> (HashSet<i32>, Vec<i32>) {
    let mut set = HashSet::new();
    let mut vec = Vec::new();

    let split = game.split(" ");

    let mut winning = true;
    for group in split {
        if group == "|" {
            winning = false;
            continue;
        }
        let val = match group.trim().parse::<i32>() {
            Ok(val) => val,
            Err(_) => continue,
        };

        if winning {
            set.insert(val);
        } else {
            vec.push(val);
        }
    }

    (set, vec)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11",
        );

        let sum = play_games(&input);

        assert_eq!(13, sum);

        let cards = scratch_cards(&input);

        assert_eq!(30, cards);
    }
}
