"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.result = void 0;
var inputs_1 = require("../src/inputs");
var result1 = inputs_1.depths.reduce(function (acc, curr, i, a) { return (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc); }, 0);
var chunked = inputs_1.depths.map(function (v, i, arr) {
    return arr.slice(i, i + 3).reduce(function (a, b) { return a + b; });
});
exports.result = chunked.reduce(function (acc, curr, i, a) { return (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc); }, 0);
