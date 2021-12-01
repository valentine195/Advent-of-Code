const inputs = require('./inputs');

const result = inputs.reduce(
    (acc, curr, i, a) => (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc),
    0
);

console.log(result);