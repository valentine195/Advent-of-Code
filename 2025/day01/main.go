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
	numZeros := 0
	start := 50

	for _, line := range strings.Split(input, "\n") {
		line = strings.TrimSpace(line)
		if line == "" {
			continue
		}

		dir := line[0]
		dist, _ := strconv.Atoi(line[1:])

		switch dir {
		case 'L':
			start -= dist
		case 'R':
			start += dist
		}
		start %= 100
		if start < 0 {
			start += 100
		}

		if start == 0 {
			numZeros++
		}
	}
	return numZeros
}

func part2(input string) int {
	numZeros := 0
	start := 50
	prev := start
	for _, line := range strings.Split(input, "\n") {
		line = strings.TrimSpace(line)
		if line == "" {
			continue
		}

		dir := line[0]
		dist, _ := strconv.Atoi(line[1:])
		prev = start
		offset := 0

		switch dir {
		case 'L':
			offset = prev % 100
			if offset == 0 {
				offset = 100
			}

			start = prev - dist
		case 'R':
			offset = (100 - prev) % 100
			if offset == 0 {
				offset = 100
			}

			start = prev + dist
		}
		start %= 100
		if start < 0 {
			start += 100
		}
		if dist >= offset {
			numZeros += 1 + (dist-offset)/100
		}
	}
	return numZeros
}
