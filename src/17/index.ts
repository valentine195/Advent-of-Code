import { Input } from "../advent";
import { test } from "../helper";

class Probe {
    max = 0;
    x: [number, number];
    y: [number, number];
    possible = 0;
    constructor(input: string) {
        const [, x1, x2] = input.match(/x=(-?\d+)\.\.(-?\d+)/) ?? [];
        const [, y1, y2] = input.match(/y=(-?\d+)\.\.(-?\d+)/) ?? [];

        this.x = [
            Math.min(Number(x1), Number(x2)),
            Math.max(Number(x1), Number(x2))
        ];
        this.y = [
            Math.min(Number(y1), Number(y2)),
            Math.max(Number(y1), Number(y2))
        ];

        for (let i = 0; i < Math.abs(this.y[0]); i++) {
            this.max += i;
        }
        console.log("PART 1: ", this.max);

        let minX = 0;
        while (this.triangle(minX) < this.x[0]) {
            minX++;
        }
        const maxX = this.x[1];

        const minY = this.y[0];
        const maxY = Math.abs(this.y[0]) - 1;

        const bounds = [
            Array.from({ length: maxX - minX + 1 }, (_, key) => key + minX),
            Array.from({ length: maxY - minY + 1 }, (_, key) => key + minY)
        ];

        for (const x of bounds[0]) {
            for (const y of bounds[1]) {
                if (this.step([x, y], [0, 0])) {
                    this.possible++;
                }
            }
        }
        console.log("PART 2:", this.possible);
    }
    triangle(number: number) {
        return (number * (number + 1)) / 2;
    }
    inc(
        velocity: number[],
        position: number[]
    ): [velocity: number[], position: number[]] {
        const pos = [position[0] + velocity[0], position[1] + velocity[1]];
        const vel = [
            velocity[0] == 0
                ? 0
                : (velocity[0] < 0 ? -1 : 1) * (Math.abs(velocity[0]) - 1),
            velocity[1] - 1
        ];

        return [vel, pos];
    }
    step(velocity: number[], position: number[]): boolean {
        if (
            position[0] >= this.x[0] &&
            this.x[1] >= position[0] &&
            position[1] >= this.y[0] &&
            this.y[1] >= position[1]
        ) {
            return true;
        }
        if (position[0] > this.x[1] || position[1] < this.y[0]) {
            return false;
        }
        return this.step(...this.inc(velocity, position));
    }
}

const startTime = new Date();
const build = async () => {
    const input = test()
        ? /* "target area: x=96..125, y=-144..-98" */ "target area: x=20..30, y=-10..-5"
        : (await Input.get({ day: 17 })).all();

    const probe = new Probe(input);

    console.log("TIME(ms):", Date.now().valueOf() - startTime.valueOf());
};

build();
