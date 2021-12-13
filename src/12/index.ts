import { read, run } from "../helper";

const input = read(12);

const testInput = `start-A
start-b
A-c
A-b
b-d
A-end
b-end`;

class Cave {
    data: string[][];
    map: Map<string, Set<string>> = new Map();
    paths: Set<string> = new Set();
    constructor(input: string) {
        this.data = input.split("\n").map((v) => v.trim().split("-"));
        for (const [from, to] of this.data) {
            this.map.set(from, (this.map.get(from) ?? new Set()).add(to));
            this.map.set(to, (this.map.get(to) ?? new Set()).add(from));
        }
    }
    start() {
        console.log(this.map);
        console.log("PART 1", this.traverse("start", ["start"]));
        console.log("PART 2", this.traverse("start", ["start"], true));
    }
    traverse(room: string, seen: string[], twice = false): number {
        if (room == "end") {
            return 1;
        }
        let paths = 0;
        for (const end of this.map.get(room)!) {
            if (end.toLowerCase() == end) {
                if (!seen.includes(end)) {
                    paths += this.traverse(end, [...seen, end], twice);
                } else if (twice && !["start", "end"].includes(end)) {
                    paths += this.traverse(end, [...seen, end], false);
                }
            } else {
                paths += this.traverse(end, [...seen], twice);
            }
        }
        return paths;
    }
}

const build = (input: string) => {
    const cave = new Cave(input);
    cave.start();
};

run(build, input, testInput);
