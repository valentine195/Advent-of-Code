package main

import (
	"fmt"
	"os"
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
	return solve(input, 2)
}

func part2(input string) int {
	return solve(input, 12)
}

func solve(input string, k int) int {
	lines := strings.Split(strings.TrimSpace(input), "\n")
	var total int64
	for _, line := range lines {
		line = strings.TrimSpace(line)

		digits := []byte(line)

		n := len(digits)
		drops := n - k
		stack := make([]byte, 0, k)

		for _, d := range digits {
			// maintain decreasing stack while we can drop and top < d
			for drops > 0 && len(stack) > 0 && stack[len(stack)-1] < d {
				stack = stack[:len(stack)-1]
				drops--
			}
			stack = append(stack, d)
		}

		//remove remaining drops from the end
		if drops > 0 {
			stack = stack[:len(stack)-drops]
		}

		var val int64
		for _, b := range stack {
			val = val*10 + int64(b-'0')
		}
		total += val
	}
	return int(total)
}
