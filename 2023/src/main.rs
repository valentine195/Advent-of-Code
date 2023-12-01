mod input;
mod puzzles;

extern crate getopts;
use getopts::Options;
use std::env;
use std::time::Instant;

fn print_usage(program: &str, opts: Options) {
    let brief = format!("Usage: {} -y 2019 -d 1", program);
    print!("{}", opts.usage(&brief));
}

fn main() {
    let now = Instant::now();
    // Code block to measure.
    {
        let args: Vec<String> = env::args().collect();
        let program = args[0].clone();

        let mut opts = Options::new();
        opts.optopt("d", "day", "select a puzzle day", "01");
        opts.optflag("h", "help", "print this help menu");

        let matches = match opts.parse(&args[1..]) {
            Ok(m) => m,
            Err(f) => {
                panic!("{}", f.to_string())
            }
        };
        if matches.opt_present("h") {
            print_usage(&program, opts);
            return;
        }
        let day = match matches.opt_str("d") {
            Some(s) => s.parse::<i32>().unwrap(),
            None => 1,
        };

        println!("{}", format!("Running 2023:day_{0:02}", day));
        puzzles::run(day);
    }

    let elapsed = now.elapsed();
    println!("Elapsed: {:.2?}", elapsed);
}
