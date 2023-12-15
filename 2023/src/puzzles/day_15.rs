use crate::input;

pub fn run() {
    let input: Vec<String> = input::read_day_input(15);
    let input = input::split_string_by(&input[0][..], ',');
    println!("** Part 1 Final: {:?}", get_hash(&input));
    println!("** Part 2 Final: {:?}", 0);
}

fn hash_str(str: &String) -> u32 {
    let mut current = 0;

    for ch in str.bytes() {
        current += ch as u32;
        current *= 17;
        current = current % 256;
    }

    current
}
fn get_hash(lines: &Vec<String>) -> u32 {
    lines.iter().fold(0, |acc, x| acc + hash_str(x))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let input: Vec<String> = input::split_string_by("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7", ',');

        assert_eq!(1320, get_hash(&input));
    }
}
