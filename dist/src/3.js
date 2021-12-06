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
var inputs_1 = require("./inputs");
/* const power = [
    "00100",
    "11110",
    "10110",
    "10111",
    "10101",
    "01111",
    "00111",
    "11100",
    "10000",
    "11001",
    "00010",
    "01010"
]; */
var bits = [];
for (var _i = 0, power_1 = inputs_1.power; _i < power_1.length; _i++) {
    var output = power_1[_i];
    output.split("").forEach(function (s, i) { var _a; return (bits[i] = ((_a = bits[i]) !== null && _a !== void 0 ? _a : 0) + Number(s)); });
}
console.log(bits);
var gamma = bits.reduce(function (acc, cur) { return (cur > inputs_1.power.length / 2 ? "".concat(acc, "1") : "".concat(acc, "0")); }, "");
var epsilon = bits.reduce(function (acc, cur) { return (cur > inputs_1.power.length / 2 ? "".concat(acc, "0") : "".concat(acc, "1")); }, "");
//part 1
console.log(parseInt(gamma, 2) * parseInt(epsilon, 2));
var oxygen = __spreadArray([], inputs_1.power, true);
var co2 = __spreadArray([], inputs_1.power, true);
var index = 0;
var _loop_1 = function () {
    if (oxygen.length == 1)
        return "break";
    var filtered = inputs_1.power.filter(function (v) { return oxygen.includes(v); });
    var common = filtered.reduce(function (a, b) { return a + Number(b[index]); }, 0) >=
        filtered.length / 2
        ? 1
        : 0;
    oxygen = oxygen.filter(function (v) { return Number(v[index]) == common; });
    index++;
};
while (oxygen.length > 1 && index < inputs_1.power[0].length) {
    var state_1 = _loop_1();
    if (state_1 === "break")
        break;
}
index = 0;
var _loop_2 = function () {
    if (co2.length == 1)
        return "break";
    var filtered = inputs_1.power.filter(function (v) { return co2.includes(v); });
    var common = filtered.reduce(function (a, b) { return a + Number(b[index]); }, 0) >=
        filtered.length / 2
        ? 1
        : 0;
    co2 = co2.filter(function (v) { return Number(v[index]) != common; });
    console.log("🚀 ~ file: 3.ts ~ line 63 ~ co2", co2);
    index++;
};
while (co2.length > 1 && index < inputs_1.power[0].length) {
    var state_2 = _loop_2();
    if (state_2 === "break")
        break;
}
console.log(parseInt(oxygen[0], 2) * parseInt(co2[0], 2));
