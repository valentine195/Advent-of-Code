"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var inputs_1 = require("./inputs");
var commands = inputs_1.inputs.map(function (input) { return input.split(" "); });
var aim = 0;
var horizontal = 0;
var depth = 0;
for (var _i = 0, commands_1 = commands; _i < commands_1.length; _i++) {
    var _a = commands_1[_i], command = _a[0], value = _a[1];
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
console.log(depth, horizontal, depth * horizontal);
