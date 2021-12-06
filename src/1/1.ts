import { depths } from "../inputs";

const result1 = depths.reduce(
    (acc, curr, i, a) => (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc),
    0
);

const chunked = depths.map((v, i, arr) =>
    arr.slice(i, i + 3).reduce((a, b) => a + b)
);

export const result = chunked.reduce(
    (acc, curr, i, a) => (i != 0 && a[i] > a[i - 1] ? acc + 1 : acc),
    0
);
