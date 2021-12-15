import { Input } from "../advent";
import { read, run, test } from "../helper";
/* import { Map } from "immutable"; */

/* console.log("🚀 ~ file: index.ts ~ line 4 ~ input", input.split(/[\r\n]{2}/)); */

const testInput = `NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C`;

const start = new Date();
const build = async () => {
    const input = test()
        ? testInput
        : await (await Input.get({ day: 14 })).all();
    const [polymer, rules] = input.split("\n\n");

    let map = new Map<string, string>();
    for (let rule of rules.split("\n")) {
        let [pattern, result] = rule.split(" -> ");
        map = map.set(pattern, result);
    }

    let pattern = new Map<string, number>();

    for (let i = 0; i < polymer.length - 1; i++) {
        pattern = pattern.set(
            polymer[i] + polymer[i + 1],
            (pattern.get(polymer[i] + polymer[i + 1]) ?? 0) + 1
        );
    }

    for (let i = 0; i < 1021; i++) {
        if (i == 10) {
            console.log("PART 1: ", calculate(polymer, pattern));
        }
        pattern = parse(pattern, map);
    }
    console.log("PART 2: ", calculate(polymer, pattern));
    console.log(Date.now().valueOf() - start.valueOf());
};
function parse(
    pattern: Map<string, number>,
    rules: Map<string, string>
): Map<string, number> {
    let out = new Map<string, number>();

    for (const [key, num] of pattern) {
        if (rules.has(key)) {
            out = out.set(
                key[0] + rules.get(key)!,
                (out.get(key[0] + rules.get(key)) ?? 0) + num
            );
            out = out.set(
                rules.get(key)! + key[1],
                (out.get(rules.get(key)! + key[1]) ?? 0) + num
            );
        }
    }

    return out;
}

function calculate(polymer: string, pattern: Map<string, number>) {
    let out = new Map<string, number>();

    out = out.set(polymer[0], 1);
    out = out.set(polymer[polymer.length - 1], 1);

    for (let [k, c] of pattern) {
        out = out.set(k[0], (out.get(k[0]) ?? 0) + c);
        out = out.set(k[1], (out.get(k[1]) ?? 0) + c);
    }

    let min = Math.min(...out.values()); /* out.minBy((v) => v)! */
    let max = Math.max(...out.values());

    return (max - min) / 2;
}

build();
