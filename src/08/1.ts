import { read, run, sum } from "../helper";

const input = read(8);

const testInput = `be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce`;

/* const testInput = `acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf`; */

const start = (input: string) => {
    const data = input.split("\n");

    const digits = new Map([
        [0, ["a", "b", "c", "e", "f", "g"]],
        [1, ["c", "f"]],
        [2, ["a", "c", "d", "e", "g"]],
        [3, ["a", "c", "d", "f", "g"]],
        [4, ["b", "c", "d", "f"]],
        [5, ["a", "b", "d", "f", "g"]],
        [6, ["a", "b", "d", "e", "f", "g"]],
        [7, ["a", "c", "f"]],
        [8, ["a", "b", "c", "d", "e", "f", "g"]],
        [9, ["a", "b", "c", "d", "f", "g"]]
    ]);

    const lengths = new Map();
    for (const [digit, letters] of digits) {
        lengths.set(letters.length, [
            ...(lengths.get(letters.length) ?? []),
            digit
        ]);
    }
    const counts = new Map([...digits.entries()].map((v) => [v[0], 0]));

    const toMatch = data.map((v) => v.split("|").pop()?.trim().split(" "));

    toMatch.forEach((str) => {
        str?.forEach((letters) => {
            if (
                lengths.has(letters.length) &&
                lengths.get(letters.length).length == 1
            ) {
                counts.set(
                    lengths.get(letters.length),
                    (counts.get(lengths.get(letters.length)) ?? 0) + 1
                );
            }
        });
    });
    console.log("🚀 ~ file: 1.ts ~ line 55 ~ counts", counts, sum(counts));
};

//run(start, input, testInput);

const sort = (str: string = "") => {
    return str.split("").sort().join("");
};
const includes = (str: string, rem: string = "") => {
    const split = str.split("");
    return rem.split("").every((v) => split.includes(v));
};
const remove = (str: string, rem: string = "") => {
    return [...str.split("")].filter((v) => ![...rem.split("")].includes(v));
};
const start2 = (input: string) => {
    const data = input.split("\n").map((v) =>
        v
            .trim()
            .split("|")
            .map((v) => v.trim().split(" "))
    );

    const values = [];
    for (let [original, output] of data) {
        let signal = [...original].map((v) => sort(v));
        output = [...output].map((v) => sort(v));

        const map = new Map([
            [0, ""],
            [1, signal.find((v) => v.length == 2)],
            [2, ""],
            [3, ""],
            [4, signal.find((v) => v.length == 4)],
            [5, ""],
            [6, ""],
            [7, signal.find((v) => v.length == 3)],
            [8, signal.find((v) => v.length == 7)]
        ]);

        signal = signal.filter((v) => ![...map.values()].includes(v));

        map.set(
            3,
            signal.find(
                (v) => v.length == 5 && remove(v, map.get(7)).length == 2
            )
        );
        signal = signal.filter((v) => ![...map.values()].includes(v));
        map.set(
            9,
            signal.find(
                (v) => v.length == 6 && remove(v, map.get(3)).length == 1
            )
        );
        signal = signal.filter((v) => ![...map.values()].includes(v));
        map.set(
            0,
            signal.find(
                (v) =>
                    v.length == 6 &&
                    includes(v, map.get(1)) &&
                    remove(v, map.get(1)).length == 4
            )
        );
        signal = signal.filter((v) => ![...map.values()].includes(v));
        map.set(
            6,
            signal.find(
                (v) => v.length == 6 && remove(v, map.get(3)).length == 2
            )
        );
        signal = signal.filter((v) => ![...map.values()].includes(v));
        map.set(
            5,
            signal.find(
                (v) => v.length == 5 && remove(v, map.get(9)).length == 0
            )
        );
        signal = signal.filter((v) => ![...map.values()].includes(v));
        map.set(
            2,
            signal.find((v) => v.length == 5)
        );

        const inv = new Map([...map.entries()].map((m) => [m[1], m[0]]));

        const value = output.map((v) => inv.get(v));

        values.push(Number(value.join("")));
    }
    console.log(sum(values));
};

run(start2, input, testInput);
