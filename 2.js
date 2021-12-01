const inputs = require("./inputs");

const chunked = inputs.map((v, i, arr) =>
    arr.slice(i, i + 3).reduce((a, b) => a + b)
);

const result = chunked.reduce(
    (acc, curr, i, a) => (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc),
    0
);

console.log(result);
