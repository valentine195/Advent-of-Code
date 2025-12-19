package main

import (
	"fmt"
	"image"
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

	points := make([]image.Point, 0)
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(strings.TrimSpace(parts[0]))
		y, _ := strconv.Atoi(strings.TrimSpace(parts[1]))
		points = append(points, image.Point{X: x, Y: y})
	}

	rects := make([]image.Rectangle, 0, len(points)*(len(points)-1)/2)
	for i := range points {
		for j := 0; j < i; j++ {
			r := image.Rectangle{Min: points[i], Max: points[j]}.Canon()
			rects = append(rects, r)
		}
	}
	//maximize area of rectangle between 2 tiles
	maxArea := 0
	for _, r := range rects {
		//apparently r.Max isn't inclusive???
		r.Max = r.Max.Add(image.Point{X: 1, Y: 1})
		maxArea = max(maxArea, r.Dx()*r.Dy())
	}
	return maxArea
}

func part2(input string) int {
	points := make([]image.Point, 0)
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(strings.TrimSpace(parts[0]))
		y, _ := strconv.Atoi(strings.TrimSpace(parts[1]))
		points = append(points, image.Point{X: x, Y: y})
	}

	//all possible rectangles
	rects := make([]image.Rectangle, 0, len(points)*(len(points)-1)/2)
	for i := range points {
		for j := 0; j < i; j++ {
			r := image.Rectangle{Min: points[i], Max: points[j]}.Canon()
			rects = append(rects, r)
		}
	}
	// Build edge rectangles between consecutive points (wrapping)
	edges := make([]image.Rectangle, 0, len(points))
	for i := 1; i < len(points); i++ {
		e := image.Rectangle{Min: points[i-1], Max: points[i]}.Canon()
		edges = append(edges, e)
	}

	// wrap edge
	edges = append(edges, image.Rectangle{Min: points[len(points)-1], Max: points[0]}.Canon())

	maxArea := 0
outer:
	for _, r := range rects {
		// inclusive tile counting by expanding Max by (1,1) dumb
		r.Max = r.Max.Add(image.Point{X: 1, Y: 1})
		area := r.Dx() * r.Dy()

		// interior (excluding boundary) must not overlap any edge
		inner := r.Inset(1)
		for _, e := range edges {
			ee := e
			ee.Max = ee.Max.Add(image.Point{X: 1, Y: 1}) //ugh
			if ee.Overlaps(inner) {
				// rectangle goes over an edge, skip
				continue outer
			}
		}
		maxArea = max(maxArea, area)
	}
	return maxArea
}
