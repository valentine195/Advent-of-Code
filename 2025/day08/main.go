package main

import (
	"fmt"
	"math"
	"os"
	"slices"
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

	part1Result := part1(input, 1000)
	part2Result := part2(input)

	fmt.Printf("Part 1: %d\n", part1Result)
	fmt.Printf("Part 2: %d\n", part2Result)
}

type JunctionBox struct {
	X int
	Y int
	Z int
}

func part1(input string, connections int) int {
	// ... your code ...
	boxes := make([]JunctionBox, 0)
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(strings.TrimSpace(parts[0]))
		y, _ := strconv.Atoi(strings.TrimSpace(parts[1]))
		z, _ := strconv.Atoi(strings.TrimSpace(parts[2]))
		boxes = append(boxes, JunctionBox{X: x, Y: y, Z: z})
	}
	circuits := connect(connections, boxes)
	/* fmt.Printf("Total circuits formed: %d\n", circuits) */
	//return length of top 3 circuits multiplied together
	if len(circuits) < 3 {
		return 0
	}
	return len(circuits[0].Boxes) * len(circuits[1].Boxes) * len(circuits[2].Boxes)
}

func part2(input string) int {
	// ... your code ...
	boxes := make([]JunctionBox, 0)
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(strings.TrimSpace(parts[0]))
		y, _ := strconv.Atoi(strings.TrimSpace(parts[1]))
		z, _ := strconv.Atoi(strings.TrimSpace(parts[2]))
		boxes = append(boxes, JunctionBox{X: x, Y: y, Z: z})
	}

	circuits := make([]Circuit, 0)
	for _, box := range boxes {
		circuits = append(circuits, Circuit{Boxes: map[JunctionBox]struct{}{box: {}}})
	}
	previousMin := -1.0
	var box1, box2 JunctionBox
	for len(circuits) > 1 {
		box1, box2, previousMin = findClosestUnconnectedBoxes(boxes, previousMin)
		circuits = addBoxesToCircuits(circuits, box1, box2)
	}

	return box1.X * box2.X
}

type Circuit struct {
	Boxes map[JunctionBox]struct{}
}

func (c *Circuit) contains(box JunctionBox) bool {
	_, exists := c.Boxes[box]
	return exists

}
func (c *Circuit) addBox(box JunctionBox) {
	c.Boxes[box] = struct{}{}
}
func (c *Circuit) merge(other Circuit) {
	for box := range other.Boxes {
		c.Boxes[box] = struct{}{}
	}
}

func connect(times int, boxes []JunctionBox) []Circuit {
	circuits := make([]Circuit, 0)
	previousMin := -1.0
	for i := 0; i < times; i++ {

		box1, box2, min := findClosestUnconnectedBoxes(boxes, previousMin)
		previousMin = min

		// Update circuits based on the new closest connection
		circuits = addBoxesToCircuits(circuits, box1, box2)
	}
	//sort circuits by size

	slices.SortFunc(circuits, func(a, b Circuit) int {
		return len(b.Boxes) - len(a.Boxes)
	})
	return circuits

}

func addBoxesToCircuits(circuits []Circuit, box1, box2 JunctionBox) []Circuit {
	// determine membership of the two boxes
	circuit1Index, circuit2Index := -1, -1
	for idx, circuit := range circuits {
		if circuit.contains(box1) {
			circuit1Index = idx
		}
		if circuit.contains(box2) {
			circuit2Index = idx
		}
		if circuit1Index != -1 && circuit2Index != -1 {
			break
		}
	}

	switch {
	case circuit1Index != -1 && circuit2Index != -1 && circuit1Index != circuit2Index:
		// merge circuits and remove the second
		circuits[circuit1Index].merge(circuits[circuit2Index])
		circuits = append(circuits[:circuit2Index], circuits[circuit2Index+1:]...)
	case circuit1Index != -1:
		circuits[circuit1Index].addBox(box2)
	case circuit2Index != -1:
		circuits[circuit2Index].addBox(box1)
	default:
		// Neither in any circuit: create a new one
		circuits = append(circuits, Circuit{Boxes: map[JunctionBox]struct{}{box1: {}, box2: {}}})
	}
	return circuits
}

// this function needs to find the two closest boxes that are not already connected in the same circuit
func findClosestUnconnectedBoxes(boxes []JunctionBox, previousMin float64) (JunctionBox, JunctionBox, float64) {
	var box1, box2 JunctionBox
	minDistance := -1.0

	for i := 0; i < len(boxes); i++ {
		for j := i + 1; j < len(boxes); j++ {
			distance := calculateDistance(boxes[i], boxes[j])
			if distance <= previousMin {
				continue
			}
			if minDistance == -1 || distance < minDistance {
				// Check if the boxes are in the same circuit
				minDistance = distance
				box1 = boxes[i]
				box2 = boxes[j]

			}
		}
	}
	return box1, box2, minDistance
}

func calculateDistance(box1, box2 JunctionBox) float64 {
	dx := box1.X - box2.X
	dy := box1.Y - box2.Y
	dz := box1.Z - box2.Z
	return math.Sqrt(float64(dx*dx + dy*dy + dz*dz))
}
