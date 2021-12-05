import { readFile, readFileSync } from "fs";

const data = readFileSync("src/4_input.txt").toString().split(/\s+\n/);

/* console.log(data); */

const numbers = data.shift()?.split(",") ?? [];
const boards = [...data].map((b) => {
    const rows = b.split("\n").map((r) => r.split(" ").filter((r) => r));
    const columns = rows[0].map((v, i) => rows.map((row) => row[i]));
    const all = b.split(/(\n|\s)/).filter((v) => v.trim() && v);
    return {
        rows,
        columns,
        all
    };
});
//part 1
let board, winner, drawn: string[];
for (let i = 0; i < numbers.length; i++) {
    drawn = numbers?.slice(0, i + 1);
    board = boards.find(
        (b) =>
            b.columns.find((n) => n.every((v) => drawn.includes(v))) ||
            b.rows.find((n) => n.every((v) => drawn.includes(v)))
    );
    if (board) {
        winner = numbers[i];
        break;
    }
}

let unmarked = board?.all.filter((v) => !drawn?.includes(v));
let sum = unmarked?.reduce((a, b) => a + Number(b), 0);
let total = (sum ?? 0) * Number(winner);
console.log("🚀 ~ file: 4.ts ~ line 36 ~ total", total);

//part 2
