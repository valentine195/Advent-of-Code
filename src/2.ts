import { inputs } from "./inputs";

const commands = inputs.map((input) => input.split(" "));

let aim = 0;
let horizontal = 0;
let depth = 0;
for (const [command, value] of commands) {
    switch (command) {
        case "up": {
            aim -= Number(value);
            break;
        }
        case "down": {
            aim += Number(value);
            break;
        }
        case "forward": {
            horizontal += Number(value);
            depth += aim * Number(value);
            break;
        }
    }
}

console.log(depth * horizontal);
