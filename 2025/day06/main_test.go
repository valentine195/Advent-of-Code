package main

import (
	"testing"
)

func TestPart1(t *testing.T) {
	sampleInput := `123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   + `

	got := part1(sampleInput)
	want := 4277556

	if got != want {
		t.Errorf("part1() = %d; want %d", got, want)
	}
}
func TestPart2(t *testing.T) {
	sampleInput := `123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   + `
	got := part2(sampleInput)
	want := 3263827

	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}
}
