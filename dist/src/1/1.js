"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.result = void 0;
const inputs_1 = require("../inputs");
const result1 = inputs_1.depths.reduce((acc, curr, i, a) => (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc), 0);
const chunked = inputs_1.depths.map((v, i, arr) => arr.slice(i, i + 3).reduce((a, b) => a + b));
exports.result = chunked.reduce((acc, curr, i, a) => (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc), 0);
