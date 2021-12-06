import { readFileSync } from "fs";

const input = readFileSync("src/5/input.txt").toString().trim().split("\n");

/* const input = `
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
`
    .trim()
    .split("\n"); */

const data = input.map((s) =>
    s.split(" -> ").map((v) => v.split(",").map((v) => Number(v)))
);

const maxX = Math.max(...data.map((v) => [v[0][0], v[1][0]]).flat()) + 1;
const maxY = Math.max(...data.map((v) => [v[0][1], v[1][1]]).flat()) + 1;

const considered = data.filter((v) => {
    return v[0][0] == v[1][0] || v[0][1] == v[1][1];
});
const dots: number[][] = [...Array(maxX).fill(0)].map((v) => [
    ...Array(maxY).fill(0)
]);
for (const [start, end] of considered) {
    for (
        let i = Math.min(end[0], start[0]);
        i <= Math.max(end[0], start[0]);
        i++
    ) {
        if (Math.max(end[0], start[0]) == Math.min(end[0], start[0])) break;
        const dot = dots[start[1]];
        if (dot.length <= i) {
            dot.push(...[...Array(i - dot.length + 1).fill(0)]);
        }
        dot[i]++;
    }
    for (
        let i = Math.min(end[1], start[1]);
        i <= Math.max(end[1], start[1]);
        i++
    ) {
        if (Math.max(end[1], start[1]) == Math.min(end[1], start[1])) break;
        const dot = dots[i];
        if (dot.length <= start[0]) {
            dot.push(...[...Array(i - start[0] + 1).fill(0)]);
        }
        dot[start[0]]++;
    }
}

console.log(dots);
const overlaps = dots.flat().filter((v) => v > 1).length;
console.log("🚀 ~ file: 1.ts ~ line 68 ~ overlaps", overlaps);
