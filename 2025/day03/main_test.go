package main

import (
	"testing"
)

func TestPart1(t *testing.T) {
	sampleInput := `987654321111111
811111111111119
234234234234278
818181911112111`

	got := part1(sampleInput)
	want := 357

	if got != want {
		t.Errorf("part1() = %d; want %d", got, want)
	}
}
func TestPart2(t *testing.T) {
	sampleInput := `987654321111111
811111111111119
234234234234278
818181911112111`
	got := part2(sampleInput)
	want := 3121910778619

	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}
}
