package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	// Read input from a file named "input.txt" in the same directory
	data, err := os.ReadFile("input.txt")
	if err != nil {
		panic(err)
	}
	input := string(data)

	part1Result := part1(input)
	part2Result := part2(input)

	fmt.Printf("Part 1: %d\n", part1Result)
	fmt.Printf("Part 2: %d\n", part2Result)
}

func part1(input string) int {

	matrix := make([][]string, 0)
	bounds := struct {
		rows int
		col  int
	}{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		row := make([]string, 0)
		fields := strings.Fields(line)
		for _, field := range fields {
			row = append(row, field)
			bounds.col = len(fields)
		}
		matrix = append(matrix, row)
	}
	bounds.rows = len(matrix)

	final := int64(0)
	for i := 0; i < bounds.col; i++ {
		total := int64(0)
		operation := matrix[bounds.rows-1][i]
		if operation == "*" {
			total = 1
		}
		for j := 0; j < bounds.rows-1; j++ {
			// Perform the operation on each element
			val, _ := strconv.ParseInt(matrix[j][i], 10, 64)
			switch operation {
			case "+":
				total += val
			case "-":
				total -= val
			case "*":
				total *= val
			case "/":
				total /= val
			}

		}
		final += total
	}

	return int(final)
}

func part2(input string) int {

	slicedInput := make([][]string, 0)
	for _, line := range strings.Split(input, "\n") {
		row := make([]string, 0)
		for _, char := range strings.Split(line, "") {
			row = append(row, char)
		}
		slicedInput = append(slicedInput, row)
	}

	//transpose the input matrix
	transposed := make([][]string, 0)
	columns := len(slicedInput[0])
	rows := len(slicedInput)
	operations := strings.Fields(strings.TrimSpace(strings.Join(slicedInput[rows-1], " ")))

	for i := 0; i < columns; i++ {
		newRow := make([]string, 0)
		for j := 0; j < rows-1; j++ {
			newRow = append(newRow, slicedInput[j][i])
		}
		transposed = append(transposed, newRow)
	}

	digits := make([][]int64, 0)
	collector := make([]int64, 0)
	for idx, numStr := range transposed {
		num := strings.TrimSpace(strings.Join(numStr, ""))
		if len(num) == 0 {
			digits = append(digits, collector)
			collector = make([]int64, 0)
			continue
		}
		val, _ := strconv.Atoi(num)
		collector = append(collector, int64(val))
		if idx == len(transposed)-1 {
			digits = append(digits, collector)
		}
	}

	final := int64(0)
	for idx, values := range digits {
		operation := operations[idx]
		total := int64(0)
		if operation == "*" {
			total = 1
		}
		for _, val := range values {

			switch operation {
			case "+":
				total += val
			case "-":
				total -= val
			case "*":
				total *= val
			case "/":
				total /= val
			}
		}
		final += total
	}

	return int(final)
}
