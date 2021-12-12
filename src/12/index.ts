import { read, run } from "../helper";

const input = read(12);

const testInput = `start-A
start-b
A-c
A-b
b-d
A-end
b-end`;

const build = (input: string) => {
    const data = input.split("\n").map((v) => v.trim().split("-"));
    const map: Map<string, Set<string>> = new Map();
    for (const [from, to] of data) {
        map.set(from, (map.get(from) ?? new Set()).add(to));
    }
    console.log(map);
};

run(build, /* input, */ testInput);
