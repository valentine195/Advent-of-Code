"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.read = exports.sum = exports.run = void 0;
const fs_1 = require("fs");
const run = (cb, input, test) => {
    if (test && process.env.NODE_ENV?.trim() == "test") {
        cb(test);
    }
    else {
        cb(input);
    }
};
exports.run = run;
const sum = (value) => {
    const array = Array.isArray(value)
        ? value
        : value instanceof Map
            ? [...value.values()]
            : [...value];
    return array.reduce((a, b) => a + b);
};
exports.sum = sum;
const read = (file) => (0, fs_1.readFileSync)(file).toString().trim();
exports.read = read;
