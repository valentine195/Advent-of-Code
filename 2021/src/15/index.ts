import { Input } from "../advent";
import { test } from "../helper";
import FastPriorityQueue from "fastpriorityqueue";

const testInput = `1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581`;

type Location = [x: number, y: number];

const start = new Date();
const build = async () => {
    const input = test()
        ? testInput
        : await (await Input.get({ day: 15 })).all();
    let lines = input.trim().split(/\n/);
    let src = lines.map((line) => [...line].map((v) => Number(v)));
    //grid 1
    new Grid(src);

    let map = Array.from({ length: src.length * 5 }, () =>
        Array.from({ length: src[0].length * 5 }, () => 0)
    );

    for (let y = 0; y < map.length; y++) {
        for (let x = 0; x < map[0].length; x++) {
            map[y][x] =
                ((src[y % src.length][x % src[0].length] +
                    Math.floor(y / src.length) +
                    Math.floor(x / src[0].length) -
                    1) %
                    9) +
                1;
        }
    }
    //grid2
    new Grid(map);

    console.log(Date.now().valueOf() - start.valueOf());
};

class Queue {
    private queue = new FastPriorityQueue<[Location, number]>(
        (a, b) => a[1] < b[1]
    );
    get size() {
        return this.queue.size;
    }
    set(location: Location, priority: number) {
        this.queue.add([location, priority]);
    }
    get() {
        return this.queue.poll()!;
    }
}
class Grid {
    columns: number;
    rows: number;
    path: Location[] = [];
    queue = new Queue();
    totalCost: number = 0;
    cost: Array<Array<number>>;

    constructor(public grid: Array<Array<number>>) {
        this.columns = this.grid[0].length;
        this.rows = this.grid.length;
        this.cost = this.grid.map((v) => v.map((n) => Infinity));
        this.traverse();
    }

    neighbors([x, y]: Location): Location[] {
        return [
            [x - 1, y],
            [x + 1, y],
            [x, y - 1],
            [x, y + 1]
        ];
    }

    get end(): Location {
        return [this.rows - 1, this.columns - 1];
    }
    get start(): Location {
        return [0, 0];
    }
    traverse() {
        this.queue.set(this.start, 0);
        this.cost[0][0] = 0;
        while (this.queue.size) {
            const [[x, y], risk] = this.queue.get();
            if (this.isEqual([x, y], [this.end[0], this.end[1]])) {
                console.log("RISK:", risk);
                break;
            }
            for (const [dx, dy] of this.neighbors([x, y])) {
                if (dx < 0 || dx >= this.columns || dy < 0 || dy >= this.rows) {
                    continue;
                }
                const cost = risk + this.grid[dy][dx];

                if (cost < this.cost[dy][dx]) {
                    this.cost[dy][dx] = cost;
                    this.queue.set([dx, dy], cost);
                }
            }
        }
    }
    isEqual(a: Location, b: Location) {
        return a[0] == b[0] && a[1] == b[1];
    }
}

build();
