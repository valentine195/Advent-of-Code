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

type Roll struct {
	X int
	Y int
}

func (r *Roll) neighbors8() []Roll {
	return []Roll{
		{X: r.X - 1, Y: r.Y},
		{X: r.X + 1, Y: r.Y},
		{X: r.X, Y: r.Y - 1},
		{X: r.X, Y: r.Y + 1},
		{X: r.X - 1, Y: r.Y - 1},
		{X: r.X + 1, Y: r.Y - 1},
		{X: r.X - 1, Y: r.Y + 1},
		{X: r.X + 1, Y: r.Y + 1},
	}
}

func part1(input string) int {

	rolls := make(map[Roll]Roll)
	maxX := 0
	maxY := 0
	for y, line := range strings.Split(input, "\n") {
		// Process each line
		for x, char := range line {
			if char == '@' {
				rolls[Roll{X: x, Y: y}] = Roll{X: x, Y: y}
			}
			if x > maxX {
				maxX = x
			}
		}
		if y > maxY {
			maxY = y
		}
	}

	return removeRolls(&rolls)
}

func part2(input string) int {

	rolls := make(map[Roll]Roll)
	maxX := 0
	maxY := 0
	for y, line := range strings.Split(input, "\n") {
		// Process each line
		for x, char := range line {
			if char == '@' {
				rolls[Roll{X: x, Y: y}] = Roll{X: x, Y: y}
			}
			if x > maxX {
				maxX = x
			}
		}
		if y > maxY {
			maxY = y
		}
	}

	count := 0
	for {
		removed := removeRolls(&rolls)
		if removed == 0 {
			break
		}
		count += removed
	}

	return count
}

func removeRolls(rolls *map[Roll]Roll) int {
	/* fmt.Printf("Found rolls: %+v\n", rolls) */
	count := 0
	removeable := make([]Roll, 0)
	// count rolls that have at less than 4 neighbors
	for _, roll := range *rolls {
		neighbors := roll.neighbors8()
		neighborCount := 0

		for _, n := range neighbors {
			if _, ok := (*rolls)[n]; ok {
				neighborCount++
			}
		}
		if neighborCount < 4 {
			count++
			removeable = append(removeable, roll)
		}
	}
	for _, roll := range removeable {
		delete(*rolls, roll)
	}
	return count
}
