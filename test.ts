//@ts-nocheck
import FastPriorityQueue from "fastpriorityqueue";
import { Input } from "./src/advent";
import { read, run, test } from "./src/helper";
type Location = [x: number, y: number];
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
const start = new Date();
const build = async () => {
    const input = test()
        ? testInput
        : await (await Input.get({ day: 15 })).all();
    const map: Map<Location, number> = new Map();
    input.split("\n").forEach((v, row) => {
        v.trim()
            .split("")
            .forEach((n, col) => map.set([row, col], Number(n)));
    });

    /* const grid = new Grid(map);
    const map = input.split("\n").map((v) =>
        v
            .trim()
            .split("")
            .map((n) => Number(n))
    );

    const grid = new Grid(map); */

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
        return this.queue.poll()![0];
    }
}

class Grid {
    columns: number;
    rows: number;
    path: Location[] = [];
    queue = new Queue();
    totalCost: number = 0;
    getRisk(point: Location) {
        return [...this.grid.entries()].find(
            ([[x, y], risk]) => x == point[0] && y == point[1]
        )![1];
    }
    constructor(public grid: Map<Location, number>) {
        this.columns = [...this.grid.keys()].filter(([x, y]) => y == 0).length;
        this.rows = [...this.grid.keys()].filter(([x, y]) => x == 0).length;

        this.traverse();
    }
    neighbors(location: Location): Location[] {
        return [...this.grid.keys()].filter((point) => {
            return (
                (point[1] == location[1] &&
                    Math.abs(point[0] - location[0]) == 1) ||
                (point[0] == location[0] &&
                    Math.abs(point[1] - location[1]) == 1)
            );
        });
    }

    get end(): Location {
        return [...this.grid.keys()].find(
            ([x, y]) => x == this.columns - 1 && y == this.rows - 1
        )!;
    }
    get start(): Location {
        return [...this.grid.keys()].find(([x, y]) => x == 0 && y == 0)!;
    }
    from: Map<Location, Location | undefined> = new Map();
    cost: Map<Location, number> = new Map();
    traverse() {
        this.queue.set(this.start, 0);
        this.from.set(this.start, undefined);
        this.cost.set(this.start, 0);
        console.log("finding");
        while (this.queue.size) {
            const current = this.queue.get();
            if (this.isEqual(current, this.end)) break;
            for (const next of this.neighbors(current)) {
                const cost =
                    (this.cost.get(current) ?? Infinity) +
                    this.getRisk(current);

                if (!this.cost.has(next) || cost < this.cost.get(next)!) {
                    this.cost.set(next, cost);
                    this.queue.set(next, cost + this.heuristic(next));

                    this.from.set(next, current);
                }
            }
        }
        this.reconstruct();
        this.draw();
        console.log(this.totalCost);
    }
    reconstruct() {
        console.log("reconstructing");
        let current = this.end;

        while (current != this.start) {
            this.path.push(current);
            current = this.from.get(current)!;
            this.totalCost += this.getRisk(current);
        }
        this.path.push(this.start);
        this.path.reverse();
    }
    isEqual(a: Location, b: Location) {
        return a[0] == b[0] && a[1] == b[1];
    }
    distance(from: Location, to: Location) {
        return Math.abs(to[0] - from[0]) + Math.abs(to[1] - from[1]);
    }
    heuristic(from: Location) {
        return this.distance(from, this.end) + this.getRisk(from);
    }
    draw() {
        let total: string[][] = [];
        let arr: string[] = [];
        for (const location of this.grid.keys()) {
            if (this.path.includes(location)) {
                arr.push(`${this.grid.get(location)}`);
            } else {
                arr.push(" ");
            }
            if (location[1] == this.columns - 1) {
                total.push(arr);
                arr = [];
            }
        }
        console.log(`\n\n${total.map((a) => a.join("")).join("\n")}`);
    }
}
