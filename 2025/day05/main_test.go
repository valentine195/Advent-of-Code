package main

import (
	"testing"
)

func TestPart1(t *testing.T) {
	sampleInput := `3-5
10-14
16-20
12-18

1
5
8
11
17
32`

	got := part1(sampleInput)
	want := 3

	if got != want {
		t.Errorf("part1() = %d; want %d", got, want)
	}
}
func TestPart2(t *testing.T) {
	sampleInput := `3-5
10-14
16-20
12-18

1
5
8
11
17
32`
	got := part2(sampleInput)
	want := 14

	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}
}
