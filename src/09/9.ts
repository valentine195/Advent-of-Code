import { cp } from "fs";
import { read, run, sum } from "../helper";

const input = read(9);

const testInput = `2199943210
3987894921
9856789892
8767896789
9899965678`;

const start = (input: string) => {
    const data = input.split("\n").map((v) => v.trim().split(""));
    const rows = data.map((row) =>
        row.map((v, i, arr) => {
            return i == 0
                ? v < arr[i + 1]
                : v < arr[i - 1] && v < (arr[i + 1] ?? Infinity);
        })
    );
    const actual = data.map((row, col) =>
        row.map((v, i) => {
            if (!rows[col][i]) return false;
            if (col == 0) return v < data[col + 1][i];
            return v < data[col - 1][i] && v < (data[col + 1]?.[i] ?? Infinity);
        })
    );

    const risk = actual.map((row, col) =>
        row.reduce((a, bool, index) => {
            return a + (bool ? Number(data[col][index]) + 1 : 0);
        }, 0)
    );
    console.log("🚀 ~ file: 9.ts ~ line 35 ~ risk", risk, sum(risk));
};

/* run(start, input, testInput); */
interface Point {
    x: number;
    y: number;
    value: number;
}
const start2 = (input: string) => {
    const data = input.split("\n").map((v) =>
        v
            .trim()
            .split("")
            .map((n) => Number(n))
    );

    const map: Point[][] = data.map((row, index) =>
        row.map((n, col) => {
            return { y: index, x: col, value: n };
        })
    );
    const inv: Point[][] = [];
    for (let i = 0; i < map[0].length; i++) {
        let column = [];
        for (const row of map) {
            column.push(row[i]);
        }
        inv.push(column);
    }
    const flat = map.flat();

    const rows = data.map((row) =>
        row.map((v, i, arr) => {
            return i == 0
                ? v < arr[i + 1]
                : v < arr[i - 1] && v < (arr[i + 1] ?? Infinity);
        })
    );
    const lows: Point[] = data
        .map((row, col) =>
            row.map((v, i) => {
                if (!rows[col][i]) return false;
                if (col == 0) return v < data[col + 1][i];
                return (
                    v < data[col - 1][i] && v < (data[col + 1]?.[i] ?? Infinity)
                );
            })
        )
        .flat()
        .map((bool, i) => (bool ? flat[i] : null))
        .filter((v) => v) as Point[];

    const getRowsAndColumns = (low: Point, main: Set<Point>) => {
        //constrain row;
        let left = map[low.y]
            .slice(0, low.x)
            .map((v) => v.value)
            .lastIndexOf(9);
        const right =
            map[low.y].slice(low.x).findIndex((v) => v.value == 9) > -1
                ? map[low.y].slice(low.x).findIndex((v) => v.value == 9) + low.x
                : map[low.y].length;
        const row = map[low.y].slice(left + 1, right);

        for (const col of row) {
            main.add(col);
            const top = inv[col.x]
                .slice(0, col.y)
                .map((v) => v.value)
                .lastIndexOf(9);
            const bottom =
                inv[col.x].slice(col.y).findIndex((v) => v.value == 9) > -1
                    ? inv[col.x].slice(col.y).findIndex((v) => v.value == 9) +
                      col.y
                    : inv[col.x].length;
            const column = inv[col.x]
                .slice(top + 1, bottom)
                .filter((v) => !main.has(v));
            column.forEach((c) => {
                main.add(c);
                getRowsAndColumns(c, main);
            });
        }
        return main;
    };

    const basins = lows.map((low) => getRowsAndColumns(low, new Set()));
    console.log(
        "🚀 ~ file: 9.ts ~ line 126 ~ basins",
        basins
            .map((v) => v.size)
            .sort((a, b) => b - a)
            .slice(0, 3)
            .reduce((a, b) => a * b)
    );
};

run(start2, input, testInput);
