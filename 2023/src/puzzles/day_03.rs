use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(3);

    let mut sum = find_values(&input, true);

    println!("** Part 1 Final: {:?}", sum);
    sum = find_values(&input, false);
    println!("** Part 2 Final: {:?}", sum);
}

fn find_values(schematic: &Vec<String>, part1: bool) -> u32 {
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

        let mut lines: Vec<&String> = Vec::new();
        if !prev.is_none() {
            lines.push(prev.unwrap());
        }
        lines.push(curr);
        if !next.is_none() {
            lines.push(next.unwrap());
        }

        if part1 {
            sum += parse_lines(lines, prev.is_none());
        } else {
            sum += parse_gears(lines, prev.is_none());
        }

        prev = Some(curr);
    }
    sum
}

fn parse_lines(lines: Vec<&String>, first: bool) -> u32 {
    let mut sum = 0;
    let mut num = 0;
    let mut is_valid = false;
    let mut vec: Vec<Vec<char>> = Vec::new();
    for line in lines {
        vec.push(line.chars().collect());
    }
    let chars = if first { &vec[0] } else { &vec[1] };
    for i in 0..chars.len() {
        if chars[i].is_digit(10) {
            // Add the digit to the number
            num = num * 10 + chars[i].to_digit(10).expect("is digit");

            for &x in &[-1, 0, 1] {
                for chars in &vec {
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

type Point = (usize, i32);
fn get_offsets() -> Vec<Point> {
    //start rows at 0 to index a vec
    vec![
        (0, -1),
        (0, 0),
        (0, 1),
        (1, -1),
        (1, 1),
        (2, -1),
        (2, 0),
        (2, 1),
    ]
}

fn parse_gears(lines: Vec<&String>, first: bool) -> u32 {
    let mut vec: Vec<Vec<char>> = Vec::new();
    for line in lines {
        vec.push(line.chars().collect());
    }
    let chars = if first { &vec[0] } else { &vec[1] };
    let offsets = get_offsets();
    let mut idx = 0;
    let mut sum = 0;
    for char in chars {
        let mut indices: Vec<(isize, isize)> = Vec::new();
        if *char == '*' {
            //indices of x,y coords
            //y -1..1
            for offset in &offsets {
                let row = offset.0;
                if row > vec.len() {
                    continue;
                }

                let col = idx + offset.1;
                let offset_chars = &vec[row];

                if col >= 0
                    && col < offset_chars.len() as i32
                    && offset_chars[col as usize].is_numeric()
                    && (offset.1 == -1
                        || (offset.1 >= 0 && !offset_chars[(col - 1) as usize].is_numeric()))
                {
                    //this is a new number;
                    indices.push((row as isize, col as isize));
                    if indices.len() > 2 {
                        break;
                    }
                }
            }
        }

        idx += 1;
        if indices.len() != 2 {
            continue;
        }
        let mut num: u32 = 1;
        for index in indices {
            let mut left = index.1;
            let mut right = index.1;
            let chars = &vec[index.0 as usize];
            while left > 0 && chars[(left - 1) as usize].is_numeric() {
                left = left - 1;
            }
            while right < (chars.len() - 1) as isize && chars[(right + 1) as usize].is_numeric() {
                right = right + 1;
            }
            let slice = chars[(left as usize)..=(right as usize)]
                .to_vec()
                .iter()
                .collect::<String>()
                .parse::<u32>()
                .unwrap();
            num *= slice;
        }
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

        assert_eq!(4361, find_values(&input, true));
        assert_eq!(467835, find_values(&input, false))
    }
}
