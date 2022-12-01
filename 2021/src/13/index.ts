import { Input } from "../advent";
import { read, run } from "../helper";

/* const input = read(13); */
/* console.log("🚀 ~ file: index.ts ~ line 4 ~ input", input.split(/[\r\n]{2}/)); */

const testInput = `6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5`;

class Sheet {
    dots: Set<string>;
    folds: string[];
    X: number;
    Y: number;
    numberOfFolds = 0;
    constructor(input: string) {
        const [dots, folds] = input.split("\n\n");
        this.X = Math.max(
            ...dots.split("\n").map((v) => Number(v.trim().split(",")[0]))
        );
        this.Y = Math.max(
            ...dots.split("\n").map((v) => Number(v.trim().split(",")[1]))
        );
        this.dots = new Set(dots.split("\n").map((v) => v.trim()));
        console.log(this.dots.size);
        this.folds = folds.trim().split("\n");
        this.start();
    }
    async start() {
        this.fold(this.folds[0]);
        console.log("PART 1:", this.dots.size);
        let start = new Date();
        for (const fold of this.folds) {
            this.fold(fold);
        }
        this.print();
        console.log(Date.now().valueOf() - start.valueOf());
    }
    fold(command: string) {
        this.numberOfFolds++;
        const [, axis, coord] = command.match(/([xy])=(\d+)/) ?? [];
        let line = Number(coord);
        let index = axis == "y" ? 1 : 0;

        if (axis == "y") {
            this.Y = line - 1;
        } else {
            this.X = line - 1;
        }

        [...this.dots]
            .filter((v) => Number(v.split(",")[index]) > line)
            .map((v) => {
                this.dots.delete(v);
                const split = v.split(",").map((n) => Number(n));
                split[index] = line - (split[index] - line);
                return split.join(",");
            })
            .forEach((v) => {
                this.dots.add(v);
            });
    }
    print() {
        let array = [];
        for (let y = 0; y <= this.Y; y++) {
            let line = [];
            for (let x = 0; x <= this.X; x++) {
                if (this.dots.has(`${x},${y}`)) {
                    line.push("#");
                } else {
                    line.push(".");
                }
            }
            array.push(line.join(""));
        }
        console.log(array.join("\n"));
    }
}

const build = async (/* input: string */) => {
    const input = await Input.get({ day: 13 });
    const sheet = new Sheet(input.all());
};

build();

/* run(build, input, testInput); */
