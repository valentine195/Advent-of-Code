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
var _a, _b;
Object.defineProperty(exports, "__esModule", { value: true });
var fs_1 = require("fs");
var data = (0, fs_1.readFileSync)("src/4_input.txt").toString().split(/\s+\n/);
/* console.log(data); */
var numbers = (_b = (_a = data.shift()) === null || _a === void 0 ? void 0 : _a.split(",")) !== null && _b !== void 0 ? _b : [];
var boards = __spreadArray([], data, true).map(function (b) {
    var rows = b.split("\n").map(function (r) { return r.split(" ").filter(function (r) { return r; }); });
    var columns = rows[0].map(function (v, i) { return rows.map(function (row) { return row[i]; }); });
    var all = b.split(/(\n|\s)/).filter(function (v) { return v.trim() && v; });
    return {
        rows: rows,
        columns: columns,
        all: all
    };
});
//part 1
var board, winner, drawn;
for (var i = 0; i < numbers.length; i++) {
    drawn = numbers === null || numbers === void 0 ? void 0 : numbers.slice(0, i + 1);
    board = boards.find(function (b) {
        return b.columns.find(function (n) { return n.every(function (v) { return drawn.includes(v); }); }) ||
            b.rows.find(function (n) { return n.every(function (v) { return drawn.includes(v); }); });
    });
    if (board) {
        winner = numbers[i];
        break;
    }
}
var unmarked = board === null || board === void 0 ? void 0 : board.all.filter(function (v) { return !(drawn === null || drawn === void 0 ? void 0 : drawn.includes(v)); });
var sum = unmarked === null || unmarked === void 0 ? void 0 : unmarked.reduce(function (a, b) { return a + Number(b); }, 0);
var total = (sum !== null && sum !== void 0 ? sum : 0) * Number(winner);
console.log("🚀 ~ file: 4.ts ~ line 36 ~ total", total);
//part 2
