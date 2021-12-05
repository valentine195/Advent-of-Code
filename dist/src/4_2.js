"use strict";
//@ts-nocheck
var fs = require("fs");
var data = fs.readFileSync("src/4_input.txt").toString().trim().split("\n\n");
var numbersToDraw = data[0]
    .trim()
    .split(",")
    .map(function (x) { return parseInt(x); });
var boards = data
    .slice(1)
    .map(parseDataToRowArrays)
    .map(convertToBoardObject);
var scores = [];
var _loop_1 = function (i) {
    var drawnNumbers = numbersToDraw.slice(0, i + 1);
    scores.push.apply(scores, boards
        .filter(function (board) { return isBoardComplete(board, drawnNumbers); })
        .map(function (x) { return removeBoardFromStorage(x, boards); })
        .map(function (x) { return computeScore(x, drawnNumbers); })
        .sort()
        .reverse());
};
for (var i = 5; boards.length != 0 || numbersToDraw.length != i; i++) {
    _loop_1(i);
}
console.log(scores[scores.length - 1]);
function removeBoardFromStorage(board, boardStorage) {
    var idx = boardStorage.indexOf(board);
    boardStorage.splice(idx, 1);
    return board;
}
function parseDataToRowArrays(data) {
    return data.split("\n").map(function (row) {
        return row
            .trim()
            .split(/\s+/g)
            .map(function (x) { return parseInt(x); });
    });
}
function convertToBoardObject(rows) {
    return {
        rows: rows,
        cols: rows[0].map(function (_, i) { return rows.map(function (row) { return row[i]; }); })
    };
}
function areAllElementsInDrawnNumbers(elementArr, drawnNumbers) {
    return elementArr.every(function (element) { return drawnNumbers.includes(element); });
}
function isBoardComplete(board, drawnNumbers) {
    var matchingRows = board.rows.some(function (row) {
        return areAllElementsInDrawnNumbers(row, drawnNumbers);
    });
    var matchingCols = board.cols.some(function (col) {
        return areAllElementsInDrawnNumbers(col, drawnNumbers);
    });
    return matchingCols || matchingRows;
}
function computeScore(board, drawnNumbers) {
    return (board.rows
        .flatMap(function (row) { return row; })
        .flatMap(function (element) { return element; })
        .filter(function (element) { return !drawnNumbers.includes(element); })
        .reduce(function (a, b) { return a + b; }) * drawnNumbers[drawnNumbers.length - 1]);
}
