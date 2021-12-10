import { mult, read, run, sum } from "../helper";

const input = read("src/10/input.txt");

const testInput = `[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]`;

const scores = new Map([
    [")", 3],
    ["]", 57],
    ["}", 1197],
    [">", 25137]
]);
const invScores = new Map([
    ["(", 1],
    ["[", 2],
    ["{", 3],
    ["<", 4]
]);

const pairs = new Map([
    [")", "("],
    ["]", "["],
    ["}", "{"],
    [">", "<"]
]);
const invPairs = new Map([
    ["(", ")"],
    ["[", "]"],
    ["{", "}"],
    ["<", ">"]
]);

//from dcurrie on slack
function solve(remaining: string, stack: string): [string, string] {
    if (remaining.length) {
        if (invPairs.has(remaining[0])) {
            return solve(
                remaining.slice(1, remaining.length),
                stack + remaining[0]
            );
        }
        if (pairs.get(remaining[0]) == stack.at(-1)) {
            return solve(
                remaining.slice(1, remaining.length),
                stack.slice(0, stack.length - 1)
            );
        }
    }
    return [remaining, stack];
}

const start = (input: string) => {
    const results = input.split("\n").map((line) => solve(line, ""));
    console.log("🚀 ~ file: index.ts ~ line 62 ~ data", results);

    const scoresPartOne = sum(
        results.map(([remaining]) =>
            remaining.length ? scores.get(remaining[0])! : 0
        )
    );
    console.log("PART 1", scoresPartOne);

    const scoresPart2 = results
        .map(([remaining, stack]) => {
            if (remaining.length == 0) {
                return stack
                    .split("")
                    .reverse()
                    .reduce((a, b) => a * 5 + invScores.get(b)!, 0);
            }
            return 0;
        })
        .filter((v) => v > 0)
        .sort((a, b) => a - b);
    console.log("PART 2", scoresPart2[Math.floor(scoresPart2.length / 2)]);
};
run(start, input, testInput);

/* const start = (input: string) => {
    const data = input.split("\n");
    const illegals: string[] = [];
    const illegal: number[] = [];

    for (const line of data) {
        let clone = line.split("");

        for (let i = 0; i < line.length; i++) {
            if (!pairs.has(line[i])) continue;
            const last = clone
                .slice(0, i)
                .filter((v) => v.length)
                .pop();

            if (last == pairs.get(line[i])!) {
                const index = clone
                    .slice(0, i)
                    .lastIndexOf(pairs.get(line[i])!);
                clone[index] = "";
                clone[i] = "";
            } else {
                illegal.push(scores.get(line[i])!);
                illegals.push(line);
                break;
            }
        }
    }
    console.log("PART 1", sum(illegal));
    return illegals;
};

run(start, input, testInput);

const start2 = (input: string) => {
    //get illegals
    const illegals = start(input);
    const data = input.split("\n");
    const incomplete = data.filter((line) => !illegals.includes(line));

    const closures: string[][] = [];

    for (const line of incomplete) {
        let clone = line.split("");

        for (let i = 0; i < line.length; i++) {
            if (!pairs.has(line[i])) continue;
            const last = clone
                .slice(0, i)
                .filter((v) => v.length)
                .pop();

            if (last == pairs.get(line[i])!) {
                const index = clone
                    .slice(0, i)
                    .lastIndexOf(pairs.get(line[i])!);
                clone[index] = "";
                clone[i] = "";
            }
        }
        closures.push(
            clone.filter((v) => v.length && invPairs.has(v)).reverse()
        );
    }

    //calc scores;
    const closureScore = [];
    for (const closure of closures) {
        let score = 0;
        for (const symbol of closure) {
            score *= 5;
            score += invScores.get(symbol)!;
        }
        closureScore.push(score);
    }
    console.log(
        "PART 2",
        closureScore.sort((a, b) => a - b)[Math.floor(closureScore.length / 2)]
    );
};

run(start2, input, testInput);
 */
