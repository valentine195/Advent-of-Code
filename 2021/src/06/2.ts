import { readFileSync } from "fs";

const input = readFileSync("src/6/input.txt").toString().trim();

let data = input.split(",").map((v) => Number(v));

const map = new Map([...new Array(9).keys()].map((k) => [k, 0]));

for (const fish of data) {
    map.set(fish, (map.get(fish) ?? 0) + 1);
}

for (let i = 0; i < 256; i++) {
    const zero = map.get(0) ?? 0;
    map.set(7, (map.get(7) ?? 0) + zero);
    for (const [key, value] of [...map.entries()]) {
        if (key == 0) continue;
        map.set(key - 1, value);
    }
    map.set(8, zero);
}
console.log(
    map,
    [...map.values()].reduce((a, b) => a + b)
);
