"use strict";
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
Object.defineProperty(exports, "__esModule", { value: true });
var fs_1 = require("fs");
var input = (0, fs_1.readFileSync)("src/5/input.txt").toString().trim().split("\n");
/* const input = `
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
`
    .trim()
    .split("\n"); */
var data = input.map(function (s) {
    return s.split(" -> ").map(function (v) { return v.split(",").map(function (v) { return Number(v); }); });
});
var maxX = Math.max.apply(Math, data.map(function (v) { return [v[0][0], v[1][0]]; }).flat()) + 1;
var maxY = Math.max.apply(Math, data.map(function (v) { return [v[0][1], v[1][1]]; }).flat()) + 1;
var considered = data; /* .filter((v) => {
    return v[0][0] == v[1][0] || v[0][1] == v[1][1];
}); */
var dots = __spreadArray([], Array(maxX).fill(0), true).map(function (v) { return __spreadArray([], Array(maxY).fill(0), true); });
for (var _i = 0, considered_1 = considered; _i < considered_1.length; _i++) {
    var entry = considered_1[_i];
    var start = entry[0], end = entry[1];
    if (Math.max(end[1], start[1]) == Math.min(end[1], start[1]))
        calculateHorizontal([start, end]);
    else if (Math.max(end[0], start[0]) == Math.min(end[0], start[0]))
        calculateVertical([start, end]);
    else {
        calculateDiag([start, end]);
    }
}
/* console.log(
    dots.map((v) => v.map((n) => (n == 0 ? "." : `${n}`)).join("")).join("\n")
); */
var overlaps = dots.flat().filter(function (v) { return v > 1; }).length;
console.log("🚀 ~ file: 1.ts ~ line 68 ~ overlaps", overlaps);
function calculateHorizontal(_a) {
    var start = _a[0], end = _a[1];
    for (var i = Math.min(end[0], start[0]); i <= Math.max(end[0], start[0]); i++) {
        var dot = dots[start[1]];
        if (dot.length <= i) {
            dot.push.apply(dot, __spreadArray([], Array(i - dot.length + 1).fill(0), true));
        }
        dot[i]++;
    }
}
function calculateVertical(_a) {
    var start = _a[0], end = _a[1];
    for (var i = Math.min(end[1], start[1]); i <= Math.max(end[1], start[1]); i++) {
        var dot = dots[i];
        if (dot.length <= start[0]) {
            dot.push.apply(dot, __spreadArray([], Array(i - start[0] + 1).fill(0), true));
        }
        dot[start[0]]++;
    }
}
function calculateDiag(entry) {
    var diag = 0;
    var start = entry[0][1] < entry[1][1] ? entry[0] : entry[1];
    var end = entry[0][1] < entry[1][1] ? entry[1] : entry[0];
    console.log("🚀 ~ file: 2.ts ~ line 89 ~ end", start, end);
    for (var i = start[1]; i <= end[1]; i++) {
        var dot = dots[i];
        if (dot.length <= end[0]) {
            dot.push.apply(dot, __spreadArray([], Array(i - end[0] + 1).fill(0), true));
        }
        dot[start[0] + (end[0] > start[0] ? 1 : -1) * diag]++;
        diag++;
    }
}
