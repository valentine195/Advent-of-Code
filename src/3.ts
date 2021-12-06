import { power } from "./inputs";

const bits: number[] = [];

for (const output of power) {
    output.split("").forEach((s, i) => (bits[i] = (bits[i] ?? 0) + Number(s)));
}

console.log(bits);

const gamma = bits.reduce(
    (acc, cur) => (cur > power.length / 2 ? `${acc}1` : `${acc}0`),
    ``
);
const epsilon = bits.reduce(
    (acc, cur) => (cur > power.length / 2 ? `${acc}0` : `${acc}1`),
    ``
);

//part 1
console.log(parseInt(gamma, 2) * parseInt(epsilon, 2));

let oxygen = [...power];
let co2 = [...power];

let index = 0;

while (oxygen.length > 1 && index < power[0].length) {
    const filtered = power.filter((v) => oxygen.includes(v));
    const common =
        filtered.reduce((a, b) => a + Number(b[index]), 0) >=
        filtered.length / 2
            ? 1
            : 0;

    oxygen = oxygen.filter((v) => Number(v[index]) == common);
    index++;
}
index = 0;
while (co2.length > 1 && index < power[0].length) {
    const filtered = power.filter((v) => co2.includes(v));
    const common =
        filtered.reduce((a, b) => a + Number(b[index]), 0) >=
        filtered.length / 2
            ? 1
            : 0;

    co2 = co2.filter((v) => Number(v[index]) != common);
    index++;
}
console.log(parseInt(oxygen[0], 2) * parseInt(co2[0], 2));
