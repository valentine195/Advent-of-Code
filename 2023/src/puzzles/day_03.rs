use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(3);

    let sum = find_values(&input);

    println!("** Part 1 Final: {:?}", sum);
    println!("** Part 2 Final: {:?}", 0);
}

fn find_values(schematic: &Vec<String>) -> u32 {
    let mut prev: Option<&String> = None;
    let mut sum = 0;
    let mut lines = schematic.iter().peekable();

    loop {
        let curr = match lines.next() {
            Some(line) => line,
            None => break,
        };
        let next = match lines.peek() {
            Some(line) => Some(*line),
            _ => None,
        };
        if prev == None {
            let lines = vec![curr, next.unwrap()];
            sum += parse_lines(lines, true);
        } else if next == None {
            let lines = vec![prev.unwrap(), curr];
            sum += parse_lines(lines, false);
        } else {
            let lines = vec![prev.unwrap(), curr, next.unwrap()];
            sum += parse_lines(lines, false);
        }

        prev = Some(curr);
    }
    sum
}

fn parse_lines(lines: Vec<&String>, first: bool) -> u32 {
    let mut sum = 0;
    let mut num = 0;
    let mut is_valid = false;
    let mut char_vec: Vec<Vec<char>> = Vec::new();
    for line in lines {
        char_vec.push(line.chars().collect());
    }
    let chars = if first { &char_vec[0] } else { &char_vec[1] };
    for i in 0..chars.len() {
        if chars[i].is_digit(10) {
            // Add the digit to the number
            num = num * 10 + chars[i].to_digit(10).expect("is digit");

            for &x in &[-1, 0, 1] {
                for chars in &char_vec {
                    if (i as isize + x).is_positive() && ((i as isize + x) as usize) < chars.len() {
                        let index = (i as isize + x) as usize;
                        if !chars[index].is_digit(10) && chars[index] != '.' {
                            is_valid = true;
                        }
                    }
                }
            }
        } else {
            if is_valid {
                sum += num;
            }
            is_valid = false;
            num = 0;
        }
    }
    if is_valid {
        sum += num;
    }
    sum
}
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string(
            "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..",
        );

        assert_eq!(4361, find_values(&input))
    }
}
