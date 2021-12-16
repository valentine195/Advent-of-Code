import { Input } from "../advent";

class Parser {
    hex: string = "";
    constructor(public input: string) {
        for (let i = 0; i < input.length; i++) {
            this.hex += parseInt(input[i], 16).toString(2).padStart(4, "0");
        }
    }
    versions = 0;
    parse(str: string = this.hex): [count: number, value: number] {
        const version = parseInt(str.slice(0, 3), 2);
        const type = parseInt(str.slice(3, 6), 2);
        this.versions += version;

        const values = [];

        if (type == 4) {
            let increment = 6;
            let packet = "";
            while (str[increment] == "1") {
                packet += str.slice(increment + 1, increment + 5);
                increment += 5;
            }
            packet += str.slice(increment + 1, increment + 5);
            return [increment + 5, parseInt(packet, 2)];
        } else {
            let packetLength;
            const slicer = str[6] == "0" ? 22 : 18;
            let length = parseInt(str.slice(7, slicer), 2);
            if (str[6] == "0") {
                let count = 0;
                while (count < length) {
                    let packet = this.parse(str.slice(22 + count));
                    count += packet[0];
                    values.push(packet[1]);
                }
                packetLength = 22 + length;
            } else {
                let count = 18;
                for (let i = 0; i < length; i++) {
                    let packet = this.parse(str.slice(count));
                    count += packet[0];
                    values.push(packet[1]);
                }
                packetLength = count;
            }

            let value: number = 0;
            switch (type) {
                case 0:
                    value = values.reduce((a, b) => a + b);
                    break;
                case 1:
                    value = values.reduce((a, b) => a * b);
                    break;
                case 2:
                    value = Math.min(...values);
                    break;
                case 3:
                    value = Math.max(...values);
                    break;
                case 5:
                    value = values[0] > values[1] ? 1 : 0;
                    break;
                case 6:
                    value = values[0] < values[1] ? 1 : 0;
                    break;
                case 7:
                    value = values[0] == values[1] ? 1 : 0;
                    break;
            }
            return [packetLength, value];
        }
    }
}

const startTime = new Date();
const build = async () => {
    const input = (await Input.get({ day: 16 })).all();
    //const input = "D2FE28";
    const Packets = new Parser(input);
    const [, totals] = Packets.parse();
    console.log("PART 1:", Packets.versions, "\nPART 2:", totals);
    console.log("TIME(ms):", Date.now().valueOf() - startTime.valueOf());
};

build();
