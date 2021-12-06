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
var inputs_1 = require("../inputs");
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
    var filtered = inputs_1.power.filter(function (v) { return oxygen.includes(v); });
    var common = filtered.reduce(function (a, b) { return a + Number(b[index]); }, 0) >=
        filtered.length / 2
        ? 1
        : 0;
    oxygen = oxygen.filter(function (v) { return Number(v[index]) == common; });
    index++;
};
while (oxygen.length > 1 && index < inputs_1.power[0].length) {
    _loop_1();
}
index = 0;
var _loop_2 = function () {
    var filtered = inputs_1.power.filter(function (v) { return co2.includes(v); });
    var common = filtered.reduce(function (a, b) { return a + Number(b[index]); }, 0) >=
        filtered.length / 2
        ? 1
        : 0;
    co2 = co2.filter(function (v) { return Number(v[index]) != common; });
    index++;
};
while (co2.length > 1 && index < inputs_1.power[0].length) {
    _loop_2();
}
console.log(parseInt(oxygen[0], 2) * parseInt(co2[0], 2));
