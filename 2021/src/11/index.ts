import { mult, read, run, sum } from "../helper";

const input = read(11);

const testInput = `5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526`;

interface Pos {
    x: number;
    y: number;
    n: number;
}

class Flashers {
    data: Pos[];
    flashes = 0;
    stepIndex = 0;
    steps = 100;
    all: boolean = false;
    constructor(public input: string) {
        this.data = input
            .trim()
            .split("\n")
            .map((v, y) =>
                v
                    .trim()
                    .split("")
                    .map((n, x) => {
                        return { x, y, n: Number(n) };
                    })
            )
            .flat();
    }
    start() {
        //part 1;
        for (let i = 0; i < this.steps; i++) {
            this.stepIndex++;
            this.step();
            if (this.all) {
                break;
            }
        }
        console.log("PART 1:", this.flashes);
        while (!this.all) {
            this.stepIndex++;
            this.step();
        }

        console.log("PART 2:", this.stepIndex);
    }
    step() {
        this.data.forEach((pos) => {
            pos.n++;
        });
        let flashers = this.data.filter((pos) => pos.n > 9);
        while (flashers.length) {
            this.flashes += flashers.length;
            flashers.forEach((flasher) => this.incrementNeighbors(flasher));
            if (
                this.data.filter((pos) => pos.n == 0).length == this.data.length
            ) {
                this.all = true;
                break;
            }
            flashers = this.data.filter((pos) => pos.n > 9);
        }
    }
    incrementNeighbors(pos: Pos) {
        pos.n = 0;

        const neighbors = this.data.filter((p) => {
            return (
                p.n != 0 &&
                Math.abs(p.x - pos.x) <= 1 &&
                Math.abs(p.y - pos.y) <= 1
            );
        });
        neighbors.forEach((p) => {
            p.n += 1;
        });
    }
    print() {
        return this.data.reduce((a, b) => {
            let string = [a, b.n];
            if (b.x == 9) {
                string.push("\n");
            }
            return string.join("");
        }, ``);
    }
}

const build = (input: string) => {
    const flashers = new Flashers(input);
    flashers.start();
};

run(build, input, testInput);
