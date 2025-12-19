package main

import (
	"fmt"
	"os"
	"strings"
	"time"
)

type Point struct {
	X int
	Y int
}

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

	splitters := make(map[Point]struct{})
	beams := make(map[Point]struct{})
	fieldSize := len(strings.Split(input, "\n")) - 1
	for y, line := range strings.Split(input, "\n") {
		// Process each line
		for x, char := range line {
			switch char {
			case 'S':
				beams[Point{X: x, Y: y + 1}] = struct{}{}
			case '^':
				splitters[Point{X: x, Y: y}] = struct{}{}
			}
		}
	}

	splits := 0
out:
	for {
		// Simulate the movement of the beams
		newBeams := make(map[Point]struct{})
		for beam := range beams {
			if beam.Y+1 > fieldSize {
				break out
			}
			// Move the beam down
			newPoint := Point{X: beam.X, Y: beam.Y + 1}
			if _, exists := beams[newPoint]; exists {
				continue
			}
			if _, exists := splitters[newPoint]; exists {
				splits++
				// Split the beam
				if _, exists := newBeams[Point{X: newPoint.X - 1, Y: newPoint.Y}]; !exists {
					newBeams[Point{X: newPoint.X - 1, Y: newPoint.Y}] = struct{}{}
				}
				if _, exists := newBeams[Point{X: newPoint.X + 1, Y: newPoint.Y}]; !exists {
					newBeams[Point{X: newPoint.X + 1, Y: newPoint.Y}] = struct{}{}
				}
			} else {
				newBeams[newPoint] = struct{}{}
			}
		}
		beams = newBeams
		/* printField(input, beams) */
	}

	return splits
}

func part2(input string) int {
	// Build grid similarly to part1 style
	lines := strings.Split(input, "\n")

	height := len(lines)
	width := len(lines[0])

	splitters := make(map[Point]struct{})
	start := Point{-1, -1}
	for y, line := range lines {
		for x, ch := range line {
			switch ch {
			case 'S':
				start = Point{X: x, Y: y}
			case '^':
				splitters[Point{X: x, Y: y}] = struct{}{}
			}
		}
	}

	// memoize number of paths from a given Point to the bottom
	memo := make(map[Point]int)

	var dfs func(point Point) int
	dfs = func(point Point) int {
		if point.Y == height-1 {
			return 1
		}
		if num, ok := memo[point]; ok {
			return num
		}
		if point.Y+1 >= height {
			return 0
		}
		totalPaths := 0
		below := Point{X: point.X, Y: point.Y + 1}
		if _, isSplit := splitters[below]; isSplit {
			if point.X-1 >= 0 {
				totalPaths += dfs(Point{X: point.X - 1, Y: below.Y})
			}
			if point.X+1 < width {
				totalPaths += dfs(Point{X: point.X + 1, Y: below.Y})
			}
		} else {
			totalPaths = dfs(below)
		}
		memo[point] = totalPaths
		return totalPaths
	}

	return dfs(start)
}

func printField(input string, beams map[Point]struct{}) {
	fmt.Print("\033[H\033[2J")
	fmt.Println("\n=====================")
	lines := strings.Split(input, "\n")
	for y, line := range lines {
		for x, char := range line {
			if _, exists := beams[Point{X: x, Y: y}]; exists {
				fmt.Print("|")
			} else {
				fmt.Print(string(char))
			}
		}
		fmt.Println()
	}
	fmt.Println("\n=====================")
	time.Sleep(500 * time.Millisecond)
}
