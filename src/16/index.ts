import { Input } from "../advent";

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

function getBinary(...str: string[]) {
    return [...str].map((h) => hex.get(h)).join("");
}

const hex = new Map([
    ["0", "0000"],
    ["1", "0001"],
    ["2", "0010"],
    ["3", "0011"],
    ["4", "0100"],
    ["5", "0101"],
    ["6", "0110"],
    ["7", "0111"],
    ["8", "1000"],
    ["9", "1001"],
    ["A", "1010"],
    ["B", "1011"],
    ["C", "1100"],
    ["D", "1101"],
    ["E", "1110"],
    ["F", "1111"]
]);
const bin = new Map([
    ["0000", "0"],
    ["0001", "1"],
    ["0010", "2"],
    ["0011", "3"],
    ["0100", "4"],
    ["0101", "5"],
    ["0110", "6"],
    ["0111", "7"],
    ["1000", "8"],
    ["1001", "9"],
    ["1010", "A"],
    ["1011", "B"],
    ["1100", "C"],
    ["1101", "D"],
    ["1110", "E"],
    ["1111", "F"]
]);

class Parser {
    hex: string[];
    constructor(hex: string) {
        this.hex = hex.split("");
    }
    versions = 0;
    parse(bits: string[] = this.hex) {
        const bit = [];
        while (bit.length < 6) {
            bit.push(...this.getChar(bits));
        }
        const { type, version } = this.test(bit.join(""));
        this.versions += Number(version);
        switch (type) {
            case "4": {
                this.getLiteral(bit);
                break;
            }
            default: {
                this.getOperator(bit);
            }
        }
    }
    getOperator(bits: string[]) {
        if (bits.length < 7) {
            bits.push(...this.getChar());
        }
        const length = bits[6];
        console.log("88", length);
        if (length == "0") {
            while (bits.length < 22) {
                bits.push(...this.getChar());
            }
            console.log("🚀 ~ file: index.ts ~ line 91 ~ bits", bits);
            const length = parseInt(bits.slice(7, 22).join(""), 2);
            const sub = [...bits.slice(22)];
            while (sub.length < length) {
                sub.push(...this.getChar());
            }
            console.log("🚀 ~ file: index.ts ~ line 93 ~ sub", sub);
        }
    }
    getChar(bits: string[] = this.hex) {
        const characters = getBinary(...bits.slice(0, 1));
        this.hex = this.hex.slice(1);
        return characters;
    }
    getLiteral(bits: string[]) {
        bits.push(...this.getChar());
        switch (bits[6 + 5 * Math.floor((bits.length - 6) / 5)]) {
            case "0": {
                if (
                    bits.slice(6 + 5 * Math.floor((bits.length - 6) / 5))
                        .length < 5
                ) {
                    bits.push(...this.getChar());
                }
                break;
            }
            default: {
                bits = this.getLiteral(bits);
            }
        }
        return bits;
    }
    test(hex: string) {
        const version = bin.get(hex.slice(0, 3).padStart(4, "0"));
        const type = bin.get(hex.slice(3, 6).padStart(4, "0"));

        return { version, type };
    }
}

const startTime = new Date();
const build = async () => {
    const input = (await Input.get({ day: 16 })).all();

    const parser = new Parser(/* input */ "38006F45291200");

    parser.parse();

    console.log(Date.now().valueOf() - startTime.valueOf());
};

function literal(input: string) {}

build();
