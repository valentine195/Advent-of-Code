package main

import (
	"testing"
)

func TestPart1(t *testing.T) {
	sampleInput := `7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3`

	got := part1(sampleInput)
	want := 50

	if got != want {
		t.Errorf("part1() = %d; want %d", got, want)
	}
}
func TestPart2(t *testing.T) {
	sampleInput := `7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3`
	got := part2(sampleInput)
	want := 24

	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}
}
