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

type Tile struct {
	X int
	Y int
}
type Rectangle struct {
	min Tile
	max Tile
}

func rectFrom(a, b Tile) Rectangle {
	minX := min(a.X, b.X)
	minY := min(a.Y, b.Y)
	maxX := max(a.X, b.X) + 1
	maxY := max(a.Y, b.Y) + 1
	return Rectangle{min: Tile{X: minX, Y: minY}, max: Tile{X: maxX, Y: maxY}}
}
func (r Rectangle) area() int {
	return (r.max.X - r.min.X) * (r.max.Y - r.min.Y)
}

func (r Rectangle) touches(other Rectangle) bool {
	return r.min.X < other.max.X && r.max.X > other.min.X &&
		r.min.Y < other.max.Y && r.max.Y > other.min.Y
}

func (r Rectangle) inset(p int) Rectangle {
	return Rectangle{
		min: Tile{X: r.min.X + p, Y: r.min.Y + p},
		max: Tile{X: r.max.X - p, Y: r.max.Y - p},
	}
}

func part1(input string) int {

	tiles := make([]Tile, 0)
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(strings.TrimSpace(parts[0]))
		y, _ := strconv.Atoi(strings.TrimSpace(parts[1]))
		tiles = append(tiles, Tile{X: x, Y: y})
	}

	rects := make([]Rectangle, 0)
	for i := range tiles {
		for j := 0; j < i; j++ {
			rects = append(rects, rectFrom(tiles[i], tiles[j]))
		}
	}
	//maximize area of rectangle between 2 tiles
	maxArea := 0
	for _, r := range rects {
		//apparently r.max isn't inclusive???
		maxArea = max(maxArea, r.area())
	}
	return maxArea
}

func part2(input string) int {
	tiles := make([]Tile, 0)
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(strings.TrimSpace(parts[0]))
		y, _ := strconv.Atoi(strings.TrimSpace(parts[1]))
		tiles = append(tiles, Tile{X: x, Y: y})
	}

	rects := make([]Rectangle, 0)
	for i := range tiles {
		for j := 0; j < i; j++ {
			r := rectFrom(tiles[i], tiles[j])
			rects = append(rects, r)
		}
	}

	// Build edge rectangles between consecutive points (wrapping)
	edges := make([]Rectangle, 0)
	for i := 1; i < len(tiles); i++ {
		edges = append(edges, rectFrom(tiles[i-1], tiles[i]))
	}
	// wrap edge
	edges = append(edges, rectFrom(tiles[len(tiles)-1], tiles[0]))

	maxArea := 0
outer:
	for _, r := range rects {
		area := r.area()
		// interior (excluding boundary) must not overlap any edge
		for _, e := range edges {
			if e.touches(r.inset(1)) {
				// rectangle goes over an edge, skip
				continue outer
			}
		}
		maxArea = max(maxArea, area)
	}
	return maxArea
}
