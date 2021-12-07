"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const fs_1 = require("fs");
const input = (0, fs_1.readFileSync)("src/5/input.txt").toString().trim().split("\n");
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
const data = input.map((s) => s.split(" -> ").map((v) => v.split(",").map((v) => Number(v))));
const maxX = Math.max(...data.map((v) => [v[0][0], v[1][0]]).flat()) + 1;
const maxY = Math.max(...data.map((v) => [v[0][1], v[1][1]]).flat()) + 1;
const considered = data;
const dots = [...Array(maxX).fill(0)].map((v) => [
    ...Array(maxY).fill(0)
]);
for (const entry of considered) {
    const [start, end] = entry;
    if (Math.max(end[1], start[1]) == Math.min(end[1], start[1]))
        calculateHorizontal([start, end]);
    else if (Math.max(end[0], start[0]) == Math.min(end[0], start[0]))
        calculateVertical([start, end]);
    else {
        calculateDiag([start, end]);
    }
}
/* console.log(
    dots.map((v) => v.map((n) => (n == 0 ? "." : `${n}`)).join("")).join("\n")
); */
const overlaps = dots.flat().filter((v) => v > 1).length;
console.log("🚀 ~ file: 1.ts ~ line 68 ~ overlaps", overlaps);
function calculateHorizontal([start, end]) {
    for (let i = Math.min(end[0], start[0]); i <= Math.max(end[0], start[0]); i++) {
        const dot = dots[start[1]];
        if (dot.length <= i) {
            dot.push(...[...Array(i - dot.length + 1).fill(0)]);
        }
        dot[i]++;
    }
}
function calculateVertical([start, end]) {
    for (let i = Math.min(end[1], start[1]); i <= Math.max(end[1], start[1]); i++) {
        const dot = dots[i];
        if (dot.length <= start[0]) {
            dot.push(...[...Array(i - start[0] + 1).fill(0)]);
        }
        dot[start[0]]++;
    }
}
function calculateDiag(entry) {
    let diag = 0;
    const start = entry[0][1] < entry[1][1] ? entry[0] : entry[1];
    const end = entry[0][1] < entry[1][1] ? entry[1] : entry[0];
    for (let i = start[1]; i <= end[1]; i++) {
        const dot = dots[i];
        if (dot.length <= end[0]) {
            dot.push(...[...Array(i - end[0] + 1).fill(0)]);
        }
        dot[start[0] + (end[0] > start[0] ? 1 : -1) * diag]++;
        diag++;
    }
}
