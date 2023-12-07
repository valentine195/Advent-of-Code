use core::panic;
use std::{
    collections::{BTreeMap, BTreeSet, HashMap},
    fmt::Error,
};

use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(7);
    let mut game = Game::build(&input, false);
    println!("** Part 1 Final: {:?}", game.play());
    let mut game2 = Game::build(&input, true);
    println!("** Part 2 Final: {:?}", game2.play());
}

#[derive(PartialEq, PartialOrd, Ord, Debug, Eq, Hash, Clone, Copy)]
enum TierRank {
    FiveKind = 1,
    FourKind = 2,
    FullHouse = 3,
    ThreeKind = 4,
    TwoPair = 5,
    OnePair = 6,
    HighCard = 7,
}

#[derive(Debug)]
struct Game {
    map: BTreeMap<TierRank, BTreeSet<CamelCards>>,
    count: u32,
}
impl Game {
    fn build(input: &Vec<String>, allow_jokers: bool) -> Self {
        let mut map: BTreeMap<TierRank, BTreeSet<CamelCards>> = BTreeMap::new();
        let mut count = 0;
        for line in input {
            match CamelCards::parse(line, allow_jokers) {
                Ok(card) => {
                    let vec = map.entry(card.rank).or_default();
                    vec.insert(card);
                    count += 1;
                }
                Err(_) => panic!("uh oh"),
            }
        }

        Game { map, count }
    }

    fn play(&mut self) -> u32 {
        let mut rank = self.count;
        let mut winnings = 0;
        loop {
            let (_, mut map) = match self.map.pop_first() {
                Some(next) => next,
                None => break,
            };

            loop {
                let set = match map.pop_last() {
                    Some(next) => next,
                    None => break,
                };
                winnings += rank * set.bid;
                rank -= 1;
            }
        }

        winnings
    }
}

#[derive(Debug, PartialEq, PartialOrd, Eq)]
struct CamelCards {
    rank: TierRank,
    hand: Hand,
    bid: u32,
}
impl CamelCards {
    fn parse(s: &str, allow_jokers: bool) -> Result<Self, Error> {
        let [cards, bid] = s.split_whitespace().collect::<Vec<&str>>()[..] else {
            return Err(Error);
        };

        let mut chars: HashMap<char, u32> = HashMap::new();
        let mut jokers = 0;
        for c in cards.chars() {
            if c == 'J' && allow_jokers {
                jokers += 1;
            } else {
                *chars.entry(c).or_insert(0) += 1;
            }
        }
        let max = chars.values().max().unwrap_or(&0);
        let rank: TierRank = match max + if allow_jokers { jokers } else { 0 } {
            1 => TierRank::HighCard,
            2 => {
                if chars.values().filter(|v| *v == &2).count() == 2 {
                    TierRank::TwoPair
                } else {
                    TierRank::OnePair
                }
            }
            3 => {
                if chars.values().any(|v| v == &1) {
                    TierRank::ThreeKind
                } else {
                    TierRank::FullHouse
                }
            }
            4 => TierRank::FourKind,
            5 => TierRank::FiveKind,
            _ => panic!("what the hell"),
        };
        return Ok(CamelCards {
            rank,
            hand: Hand::parse(cards, allow_jokers).unwrap(),
            bid: bid.parse::<u32>().unwrap(),
        });
    }
}

impl Ord for CamelCards {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        if self.rank != other.rank {
            return self.rank.cmp(&other.rank);
        }

        self.hand.cmp(&other.hand)
    }
}

#[derive(Debug, PartialEq, PartialOrd, Eq)]
struct Hand {
    cards: Vec<u32>,
}
impl Hand {
    fn parse(s: &str, allow_jokers: bool) -> Result<Self, Error> {
        let vec = s
            .chars()
            .map(|s| Hand::card_value(&s, allow_jokers))
            .collect();

        Ok(Hand { cards: vec })
    }
    fn card_value(card: &char, allow_jokers: bool) -> u32 {
        if card.is_numeric() {
            card.to_digit(10).unwrap()
        } else {
            match card {
                'T' => 10,
                'J' => {
                    if allow_jokers {
                        0
                    } else {
                        11
                    }
                }
                'Q' => 12,
                'K' => 13,
                'A' => 14,
                _ => panic!("no card matched for char {}", card),
            }
        }
    }
}
impl Ord for Hand {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        for idx in 0..self.cards.len() {
            if self.cards[idx] > other.cards[idx] {
                return std::cmp::Ordering::Greater;
            } else if self.cards[idx] < other.cards[idx] {
                return std::cmp::Ordering::Less;
            }
        }
        std::cmp::Ordering::Equal
    }
}
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483",
        );

        let mut game1 = Game::build(&input, false);
        assert_eq!(6440, game1.play());
        let mut game2 = Game::build(&input, true);
        assert_eq!(5905, game2.play());
    }
}
