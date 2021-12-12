import { readFileSync } from "fs";
import path from "path";

export const run = (
    cb: (data: string) => any,
    input: string,
    test?: string
) => {
    if (test && process.env.NODE_ENV?.trim() == "test") {
        cb(test);
    } else {
        cb(input);
    }
};

export const sum = (value: Array<number> | Map<any, number> | Set<number>) => {
    const array = Array.isArray(value)
        ? value
        : value instanceof Map
        ? [...value.values()]
        : [...value];

    if (!array.length) return 0;
    return array.reduce((a, b) => a + b);
};
export const mult = (value: Array<number> | Map<any, number> | Set<number>) => {
    const array = Array.isArray(value)
        ? value
        : value instanceof Map
        ? [...value.values()]
        : [...value];
    if (!array.length) return 0;
    return array.reduce((a, b) => a * b);
};

export const read = (day: number) =>
    readFileSync(path.resolve(`inputs/day${day}.txt`))
        .toString()
        .trim();
