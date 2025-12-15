package main

import (
	"testing"
)

func TestPart1(t *testing.T) {
	sampleInput := `L68
L30
R48
L5
R60
L55
L1
L99
R14
L82`

	got := part1(sampleInput)
	want := 3

	if got != want {
		t.Errorf("part1() = %d; want %d", got, want)
	}
}

func TestPart2(t *testing.T) {
	sampleInput := `L68
L30	
R48
L5
R60
L55
L1
L99
R14
L82`

	got := part2(sampleInput)
	want := 6

	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}

	got = part2(`R1000`)
	want = 10
	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}
}
