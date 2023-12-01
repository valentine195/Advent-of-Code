use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(1, true);
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = puzzle_input::split_string(
            "1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet",
        );

        let (m1, m2, m3) = count_calories(&input);
        assert_eq!(m1, 24000);
        assert_eq!(m2, 11000);
        assert_eq!(m3, 10000);
        assert_eq!(m1 + m2 + m3, 45000);
    }
}
