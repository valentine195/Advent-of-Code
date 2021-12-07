import { readFileSync } from "fs";

const input = readFileSync("src/7/input.txt").toString().trim();

/* const input = `16,1,2,0,4,2,7,1,2,14`; */

const data = input.split(",").map((v) => Number(v));

const positions = new Map();

for (const position of data) {
    positions.set(position, (positions.get(position) ?? 0) + 1);
}

const min = Math.min(...data);
const max = Math.max(...data);

const moves = [];

for (let i = min; i <= max; i++) {
    let fuel = 0;
    for (const [position, crabs] of positions) {
        fuel += Math.abs(position - i) * crabs;
    }
    moves.push(fuel);
}

const cheapest = Math.min(...moves);
console.log("🚀 ~ file: 1.ts ~ line 27 ~ cheapest", cheapest);
